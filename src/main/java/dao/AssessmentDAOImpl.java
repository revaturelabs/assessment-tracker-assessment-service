package dao;

import exceptions.ResourceNotFound;
import models.Assessment;
import models.Grade;
import models.Note;
import util.ConnectionDB;
import models.AssessmentType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidValue;

public class AssessmentDAOImpl implements AssessmentDAO {

    @Override
    public List<Assessment> getAssessments() {
        String sql = "SELECT * FROM assessments";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            List<Assessment> assessments = new ArrayList<>();

            while (rs.next()) {
                assessments.add(buildAssessment(rs));
            }

            return assessments;

        } catch (SQLException | InvalidValue e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Assessment> getAssessmentsByTraineeId(int traineeId) {
        String sql = "SELECT * FROM grades AS g JOIN assessments a ON "
                + "g.assessment_id = a.id WHERE associate_id = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {

            ps.setInt(1, traineeId);

            ResultSet rs = ps.executeQuery();

            List<Assessment> assessments = new ArrayList<>();

            while (rs.next()) {
                assessments.add(buildAssessment(rs));
            }
            return assessments;

        } catch (SQLException | InvalidValue e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<Assessment> getBatchWeek(int batchId, String weekId) {
        String sql = "SELECT * FROM assessments WHERE batch_id = ? AND week = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, batchId);
            ps.setString(2, weekId);

            ResultSet rs = ps.executeQuery();

            List<Assessment> assessments = new ArrayList<>();

            while (rs.next()) {
                assessments.add(buildAssessment(rs));
            }

            return assessments;

        } catch (SQLException | InvalidValue e) {
            e.printStackTrace();
        }

        return new ArrayList<>();

    }

    @Override
    public Grade getGradeForAssociate(int associateId, int assessmentId) {
        String sql = "SELECT g.id, g.assessment_id, g.score, g.associate_id FROM grades as g JOIN assessments a "
                + "ON g.assessment_id = a.id WHERE" + " associate_id = ? AND assessment_id = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, associateId);
            ps.setInt(2, assessmentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildGrade(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Grade updateGrade(Grade grade) {
        if (grade.getScore() < 0)
            return null;
        String sql = "UPDATE grades SET score=? WHERE assessment_id=? and associate_id=? RETURNING *";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, grade.getScore());
            ps.setInt(2, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildGrade(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Grade> getGradesForWeek(int traineeId, String weekId) {
        String sql = "SELECT g.id, g.assessment_id, g.score, g.associate_id FROM grades as g JOIN assessments a "
                + "ON g.assessment_id = a.id WHERE" + " associate_id = ? AND week = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, traineeId);
            ps.setString(2, weekId);

            ResultSet rs = ps.executeQuery();

            List<Grade> grades = new ArrayList<>();

            while (rs.next()) {
                grades.add(buildGrade(rs));
            }

            return grades;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Assessment getAssessmentById(int assessmentId) throws ResourceNotFound, InvalidValue {
        String sql = "SELECT * FROM assessments WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) return buildAssessment(rs);
            else throw new ResourceNotFound("Requested assessment with id: " + assessmentId + " could not be found");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidValue e) {
            //Invalid value in the database
            throw new InvalidValue("Something went wrong; invalid assessment data was found");
        }
        return null;
    }

    @Override
    public Assessment createAssessment(Assessment a) throws InvalidValue {
        if(a == null) throw new InvalidValue("Empty value supplied assessment cannot be created");

        String sql = "INSERT INTO assessments VALUES (DEFAULT,?,?,?,?,?,?) RETURNING *";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            a.verifyAssessment();
            ps.setInt(1, a.getCategoryId());
            ps.setInt(2, a.getTypeId());
            ps.setString(3, a.getAssessmentTitle());
            ps.setDouble(4, a.getAssessmentWeight());
            ps.setInt(5, a.getBatchId());
            ps.setString(6, a.getWeekId());

            ResultSet rs = ps.executeQuery();
            if(rs.next()) return buildAssessment(rs);
            else throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteAssessment(int assessmentId) throws ResourceNotFound {
        String sql = "DELETE FROM assessments where id = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);
            if(ps.executeUpdate() == 0) {
                throw new ResourceNotFound("The assessment with the given id was not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean adjustWeight(int assessmentId, int weight) throws InvalidValue, ResourceNotFound {
        //Ensures weight is within bounds; throws InvalidValue otherwise
        Assessment validation = new Assessment();
        validation.setAssessmentWeight(weight);
        String sql = "UPDATE assessments SET weight=? WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, weight);
            ps.setInt(2, assessmentId);

            if (ps.executeUpdate() > 0) return true;
            else throw new ResourceNotFound("The assessment with the given id was not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AssessmentType createAssessmentType(String name, int defaultWeight) {
        String sql = "INSERT INTO types values (default, ?, ?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, defaultWeight);

            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return buildType(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean assignAssessmentType(int assessmentId, int typeId) throws SQLException, ResourceNotFound, InvalidValue {
        String sql = "UPDATE assessments SET type_id=? WHERE id=? returning id";
        String sql1 = "select from types where id=?";
        String sql2 = "select from assessments where id=?";

        //try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)){
        try(PreparedStatement ps1 = ConnectionDB.getConnection().prepareStatement(sql1)) {
            ps1.setInt(1, typeId);
            ResultSet rs1 = ps1.executeQuery();
            if(!rs1.next()){
                throw new SQLException("The type with id " + typeId + " does not exist");
            }
            /*try(PreparedStatement ps2 = ConnectionDB.getConnection().prepareStatement(sql2)) {
                ps2.setInt(1, assessmentId);
                ResultSet rs2 = ps2.executeQuery();
                if(!rs2.next()){
                    throw new SQLException("The assessment with id " + assessmentId + " does not exist");
                }
                try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)){
                    ps.setInt(1,typeId);
                    ps.setInt(2, assessmentId);
                    ResultSet rs = ps.executeQuery();
                    return true;
                }
            }*/
            getAssessmentById(assessmentId);
            try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)){
                ps.setInt(1,typeId);
                ps.setInt(2, assessmentId);
                ResultSet rs = ps.executeQuery();
                return true;
            }
        }
    }

    @Override
    public List<Note> getNotesForTrainee(int id, int weekId) {
        String sql = "SELECT * FROM notes WHERE associate_id=? AND week_number=?";
        List<Note> notes = new ArrayList<>();
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, weekId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                notes.add(buildNote(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    @Override
    public Grade insertGrade(Grade grade) {
        if (grade.getScore() < 0)
            return null;
        String sql = "INSERT INTO grades VALUES (DEFAULT,?,?,?) RETURNING *";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());
            ps.setDouble(2, grade.getScore());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildGrade(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Note buildNote(ResultSet rs) throws SQLException {
        return new Note(rs.getInt("id"), rs.getInt("batch_id"), rs.getInt("associate_id"),
                rs.getInt("week_number"), rs.getString("cont")
        );
    }

    private Assessment buildAssessment(ResultSet rs) throws SQLException, InvalidValue {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(rs.getInt("id"));
        assessment.setAssessmentTitle(rs.getString("title"));
        assessment.setTypeId(rs.getInt("type_id"));
        assessment.setBatchId(rs.getInt("batch_id"));
        assessment.setWeekId(rs.getString("week"));
        assessment.setAssessmentWeight(rs.getInt("weight"));
        assessment.setCategoryId(rs.getInt("category_id"));
        return assessment;
    }

    private AssessmentType buildType(ResultSet rs) throws SQLException {
        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setTypeId(rs.getInt("id"));
        assessmentType.setName(rs.getString("name"));
        assessmentType.setDefaultWeight(rs.getInt("default_weight"));

        return assessmentType;
    }

    private Grade buildGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setGradeId(rs.getInt("id"));
        grade.setAssessmentId(rs.getInt("assessment_id"));
        grade.setAssociateId(rs.getInt("associate_id"));
        grade.setScore(rs.getDouble("score"));

        return grade;
    }
}
