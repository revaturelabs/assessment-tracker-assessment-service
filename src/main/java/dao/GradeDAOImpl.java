package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;
import util.ConnectionDB;

public class GradeDAOImpl implements GradeDAO{

    @Override
    public Grade createGrade(Grade grade) throws DuplicateResource {
        String sql = "INSERT INTO grades VALUES (DEFAULT,?,?,?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());
            ps.setDouble(2, grade.getScore());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildGrade(rs);
            }
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000) //This need to be changed for the exact code that has issues with forign keys
                throw new DuplicateResource(String.format("Grade with assessment id %d and associate id %d already exists", grade.getAssessmentId(), grade.getAssociateId()));
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Grade> getGrades() {
        String sql = "SELECT * FROM categories";
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
            }
            else
                throw new ResourceNotFound(String.format("Grade with assessment id %d and associate id %d couldn't be found", assessmentId, associateId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Grade updateGrade(Grade grade) throws ResourceNotFound, ResourceUnchangable{
        String sql = "UPDATE grades SET score=? WHERE assessment_id=? and associate_id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, grade.getScore());
            ps.setInt(2, grade.getAssessmentId());
            ps.setInt(3, grade.getAssociateId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildGrade(rs);
            }
            else
                throw new ResourceNotFound(String.format("Grade with id %d couldn't be found", grade.getGradeId()));
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000) //This need to be changed for the exact code that has issues with forign keys
                throw new ResourceUnchangable(String.format("Grade with id %d has dependents that prevent modification", grade.getGradeId()));
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
            if (rowsChanged > 0) {
                return;
            }
            else{
                throw new ResourceNotFound(String.format("Grade with id %d couldn't be found", id));
            }
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000) //This need to be changed for the exact code that has issues with forign keys
                throw new ResourceUnchangable(String.format("Grade with id %d has dependents that prevent deletion", id));
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
}
