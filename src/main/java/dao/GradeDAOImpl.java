package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dtos.WeekAvg;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;
import util.ConnectionDB;

public class GradeDAOImpl implements GradeDAO {

    @Override
    public Grade createGrade(Grade grade) throws DuplicateResource, InvalidValue {
        String sql = "INSERT INTO grades VALUES (DEFAULT,?,?,?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());
            ps.setDouble(2, grade.getScore());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildGrade(rs);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23514"))
                throw new DuplicateResource(
                        String.format("Grade with assessment id %d and associate id %d already exists",
                                grade.getAssessmentId(), grade.getAssociateId()));
            if (e.getSQLState().equals("23503"))
                throw new InvalidValue("The grade contains one or more references to values that do not exist");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Grade> getGrades() {
        String sql = "SELECT * FROM grades";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Grade> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(buildGrade(rs));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound {
        String sql = "SELECT * FROM grades WHERE assessment_id=? AND associate_id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);
            ps.setInt(2, associateId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildGrade(rs);
            } else
                throw new ResourceNotFound(
                        String.format("Grade with assessment id %d and associate id %d couldn't be found", assessmentId,
                                associateId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Grade updateGrade(Grade grade) throws ResourceNotFound, InvalidValue, DuplicateResource {
        String sql = "UPDATE grades SET score=? WHERE assessment_id=? AND associate_id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, grade.getScore());
            ps.setInt(2, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildGrade(rs);
            } else
                throw new ResourceNotFound(String.format("Grade with id %d couldn't be found", grade.getGradeId()));
        } catch (SQLException e) {
            if (e.getSQLState().equals("23514"))
                throw new DuplicateResource(
                        String.format("Grade with assessment id %d and associate id %d already exists",
                                grade.getAssessmentId(), grade.getAssociateId()));
            if (e.getSQLState().equals("23503"))
                throw new InvalidValue("The grade contains one or more references to values that do not exist");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable {
        String sql = "DELETE FROM grades WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsChanged = ps.executeUpdate();
            if (rowsChanged == 0) {
                throw new ResourceNotFound(String.format("Grade with id %d couldn't be found", id));
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000"))
                throw new ResourceUnchangable(
                        String.format("Grade with id %d has dependents that prevent deletion", id));
            e.printStackTrace();
        }
    }

    private Grade buildGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setGradeId(rs.getInt("id"));
        grade.setAssessmentId(rs.getInt("assessment_id"));
        grade.setAssociateId(rs.getInt("associate_id"));
        grade.setScore(rs.getDouble("score"));
        return grade;
    }

    private WeekAvg buildWeekAvg(ResultSet rs) throws SQLException {
        WeekAvg avg = new WeekAvg();
        avg.setAverageScore(rs.getDouble("avg"));
        avg.setAssessmentId(rs.getInt("assessment_id"));
        return avg;
    }

    @Override
    public List<Grade> getGradesForWeek(int associateId, int weekId) {
        String sql = "SELECT g.id, g.assessment_id, g.score, g.associate_id FROM grades as g JOIN assessments a "
                + "ON g.assessment_id = a.id WHERE" + " associate_id = ? AND week = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, associateId);
            ps.setInt(2, weekId);
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
    public double getAverageGrade(int assessmentId) throws ResourceNotFound {
        String sql = "SELECT AVG(score) AS average_score\n" + "FROM grades\n" + "WHERE assessment_id = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double average = rs.getDouble("average_score");
                if (rs.wasNull()) {
                    throw new ResourceNotFound("There are no grades for assessment with id " + assessmentId);
                }
                int twoDecimalPlace = (int) (average * 100.0);

                return twoDecimalPlace / 100.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Grade> getGrades(int batchId, int weekId) {
        String sql = "SELECT * FROM grades INNER JOIN assessments ON assessments.id = grades.assessment_id WHERE batch_id=? AND week=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, batchId);
            ps.setString(2, Integer.toString(weekId));
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
    public List<WeekAvg> getAvgWeek(int batchId, int weekId) {
        String sql = "SELECT assessment_id, avg(score) FROM grades INNER JOIN assessments ON assessments.id = grades.assessment_id WHERE batch_id=? AND week=? group by grades.assessment_id;";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, batchId);
            ps.setString(2, Integer.toString(weekId));
            ResultSet rs = ps.executeQuery();
            List<WeekAvg> avgs = new ArrayList<>();
            while (rs.next()) {
                avgs.add(buildWeekAvg(rs));
            }
            return avgs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
