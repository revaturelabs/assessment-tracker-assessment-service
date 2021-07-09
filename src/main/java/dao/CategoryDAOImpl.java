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
import models.Category;
import util.ConnectionDB;

public class CategoryDAOImpl implements CategoryDAO {

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
    public Category updateCategory(Category category) throws ResourceNotFound{
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

    @Override
    public Category createCategory(Category category) throws DuplicateResource{
        String sql = "INSERT INTO categories VALUES (DEFAULT,?)";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return buildCategory(rs);
            }
        } catch (SQLException e) {
            if(e.getErrorCode() >= 23000 || e.getErrorCode() <= 24000)
                throw new DuplicateResource(String.format("Category with name %s already exists", category.getName()));
            e.printStackTrace();
        }
        return null;
    }

    private Category buildCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    }
    
}
