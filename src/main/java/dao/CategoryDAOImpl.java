package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;
import util.ConnectionDB;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category createCategory(Category category) throws DuplicateResource, InvalidValue{
        if(category.getName() == null || category.getName().length() == 0)
            throw new InvalidValue("Category name cannot be empty");
        String sql = "INSERT INTO categories VALUES (DEFAULT,?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildCategory(rs);
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505"))
                throw new DuplicateResource(String.format("Category with name %s already exists", category.getName()));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> getCategories() {
        String sql = "SELECT * FROM categories";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(buildCategory(rs));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Category getCategory(int categoryId)  throws ResourceNotFound{
        String sql = "SELECT * FROM categories WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return buildCategory(rs);
            else
                throw new ResourceNotFound(String.format("Category with id %d couldn't be found", categoryId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category updateCategory(Category category) throws ResourceNotFound, DuplicateResource, InvalidValue{
        if(category.getName() == null || category.getName().length() == 0)
            throw new InvalidValue("Category name cannot be empty");
        String sql = "UPDATE categories SET name=? WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getCategoryId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                return buildCategory(rs);
            else
                throw new ResourceNotFound(String.format("Category with id %d couldn't be found", category.getCategoryId()));
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505"))
                throw new DuplicateResource(String.format("Category with name %s already exists", category.getName()));
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable{
        String sql = "DELETE FROM categories WHERE id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            int rowsChanged = ps.executeUpdate();
            if (rowsChanged > 0) {
                return;
            }
            else{
                throw new ResourceNotFound(String.format("Category with id %d couldn't be found", categoryId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResourceUnchangable(String.format("Category with id %d has dependents that prevent deletion", categoryId));
        }
    }

    private Category buildCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    }

    @Override
    public Category addCategory(int assessmentId, int categoryId) throws ResourceNotFound, InvalidValue {
        String sql = "INSERT INTO categories_junction VALUES (?,?)";
        Category category = this.getCategory(categoryId);
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, assessmentId);
            ps.setInt(2, categoryId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return category;
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23503"))
                throw new InvalidValue("Assessment ID doesn't match to a valid reference");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> getCategories(int assessmentId){
        String sql = "SELECT DISTINCT * FROM categories INNER JOIN categories_junction ON categories.id = categories_junction.category_id WHERE assessment_id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);
            ResultSet rs = ps.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(buildCategory(rs));
            }
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void removeCategory(int assessmentId, int categoryId) throws ResourceNotFound {
        String sql = "DELETE FROM categories_junction WHERE assessment_id=? AND category_id=?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, assessmentId);
            ps.setInt(2, categoryId);
            int rowsChanged = ps.executeUpdate();
            if (rowsChanged > 0) {
                return;
            }
            else{
                throw new ResourceNotFound(String.format("Category with assessment id %d and category %d couldn't be found", assessmentId, categoryId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
