package dao;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import util.ConnectionDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AssessmentDAOImpl implements AssessmentDAO {

    @Override
    public Assessment createAssessment(Assessment a){
        String sql = "INSERT INTO assessments VALUES (DEFAULT,?,?,?,?,?,?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getCategoryId());
            ps.setInt(2, a.getTypeId());
            ps.setString(3, a.getAssessmentTitle());
            ps.setDouble(4, a.getAssessmentWeight());
            ps.setInt(5, a.getBatchId());
            ps.setInt(6, a.getWeekId());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return buildAssessment(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Assessment getAssessment(int id) throws ResourceNotFound {
        String sql = "SELECT FROM assessments WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return buildAssessment(rs);
            else
                throw new ResourceNotFound(String.format("Assessment with id %d couldn't be found", id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable {
        String sql = "UPDATE assessments SET category_id=?, type_id=?, title=?, weight=?, batch_id=?, week=? WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, assessment.getCategoryId());
            ps.setInt(2, assessment.getTypeId());
            ps.setString(3, assessment.getAssessmentTitle());
            ps.setInt(4, assessment.getAssessmentWeight());
            ps.setInt(5, assessment.getBatchId());
            ps.setInt(6, assessment.getWeekId());
            ps.setInt(7, assessment.getAssessmentId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildAssessment(rs);
            }
            else
                throw new ResourceNotFound(String.format("Assessment with id %d couldn't be found", assessment.getAssessmentId()));
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000) //This need to be changed for the exact code that has issues with forign keys
                throw new ResourceUnchangable(String.format("Assessment with id %d has dependents that prevent modification", assessment.getAssessmentId()));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAssessment(int id) throws ResourceNotFound, ResourceUnchangable {
        String sql = "DELETE FROM assessments where id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsChanged = ps.executeUpdate();
            if (rowsChanged > 0) {
                return;
            } else {
                throw new ResourceNotFound(String.format("Assessment with id %d couldn't be found", id));
            }
        } catch (SQLException e) {
            if (e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000) // This need to be changed for the exact code that has issues with forign keys
                throw new ResourceUnchangable(String.format("Assessment with id %d has dependents that prevent deletion", id));
            e.printStackTrace();
        }
    }

    @Override
    public List<Assessment> getAssessmentsByAssociateId(int associateId) {
        String sql = "SELECT * FROM grades AS g JOIN assessments a ON "
                + "g.assessment_id = a.id WHERE associate_id = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, associateId);
            ResultSet rs = ps.executeQuery();
            List<Assessment> assessments = new ArrayList<>();
            while (rs.next()) {
                assessments.add(buildAssessment(rs));
            }
            return assessments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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

    // @Override
    // public List<Note> getNotesForTrainee(int id, int weekId) {
    // String sql = "SELECT * FROM notes WHERE associate_id=? AND week_number=?";
    // List<Note> notes = new ArrayList<>();
    // try (PreparedStatement ps =
    // ConnectionDB.getConnection().prepareStatement(sql)) {
    // ps.setInt(1, id);
    // ps.setInt(2, weekId);

    // ResultSet resultSet = ps.executeQuery();

    // while (resultSet.next()) {
    // notes.add(buildNote(resultSet));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // return notes;
    // }

    // private Note buildNote(ResultSet rs) throws SQLException {
    // return new Note(rs.getInt("id"), rs.getInt("batch_id"),
    // rs.getInt("associate_id"),
    // rs.getInt("week_number"), rs.getString("cont")
    // );
    // }

    private Assessment buildAssessment(ResultSet rs) throws SQLException {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(rs.getInt("id"));
        assessment.setAssessmentTitle(rs.getString("title"));
        assessment.setTypeId(rs.getInt("type_id"));
        assessment.setBatchId(rs.getInt("batch_id"));
        assessment.setWeekId(rs.getInt("week"));
        assessment.setAssessmentWeight(rs.getInt("weight"));
        assessment.setCategoryId(rs.getInt("category_id"));
        return assessment;
    }
}
