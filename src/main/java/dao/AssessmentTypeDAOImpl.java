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
import models.AssessmentType;
import util.ConnectionDB;

public class AssessmentTypeDAOImpl implements AssessmentTypeDAO{

    @Override
    public AssessmentType createAssessmentType(AssessmentType assessmentType) throws DuplicateResource{
        String sql = "INSERT INTO types values (default, ?, ?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, assessmentType.getName());
            ps.setInt(2, assessmentType.getDefaultWeight());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildType(rs);
            }
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000)
                throw new DuplicateResource(String.format("Assessment type with name %s already exists", assessmentType.getName()));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AssessmentType> getAssessmentTypes() {
        String sql = "SELECT * FROM types";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<AssessmentType> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(buildType(rs));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public AssessmentType getAssessmentType(int id) throws ResourceNotFound{
        String sql = "SELECT * FROM types WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return buildType(rs);
            else
                throw new ResourceNotFound(String.format("Assessment type with id %d couldn't be found", id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AssessmentType updateAssessmentType(AssessmentType assessmentType) throws ResourceNotFound{
        String sql = "UPDATE types SET name=? default_weight=? WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, assessmentType.getName());
            ps.setInt(2, assessmentType.getDefaultWeight());
            ps.setInt(2, assessmentType.getTypeId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                return buildType(rs);
            else
                throw new ResourceNotFound(String.format("Assessment type with id %d couldn't be found", assessmentType.getTypeId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAssessmentType(int id) throws ResourceNotFound, ResourceUnchangable{
        String sql = "DELETE FROM types WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsChanged = ps.executeUpdate();
            if (rowsChanged > 0) {
                return;
            }
            else{
                throw new ResourceNotFound(String.format("Assessment type with id %d couldn't be found", id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResourceUnchangable(String.format("Assessment type with id %d has dependents that prevent deletion", id));
        }
    }

    private AssessmentType buildType(ResultSet rs) throws SQLException {
        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setTypeId(rs.getInt("id"));
        assessmentType.setName(rs.getString("name"));
        assessmentType.setDefaultWeight(rs.getInt("default_weight"));
        return assessmentType;
    }
    
}
