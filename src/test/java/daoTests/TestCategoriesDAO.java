package daoTests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import models.Category;

public class TestCategoriesDAO {
    private static CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Test
    public void createValidCategory(){
        Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNotNull("Error occured creating category", category);
        Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
        Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        categoryDAO.deleteCategory(category.getCategoryId());
    }

    @Test
    public void createDuplicateCategory(){ //Database must require unique names for this test to pass
        Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNotNull("Error occured creating category", category);
        Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
        Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        Category newCategory = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNull("Duplicate category inserted into database", newCategory);
        categoryDAO.deleteCategory(category.getCategoryId());
    }

    @Test
    public void getValidCategory(){
        Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNotNull("Error occured creating category", category);
        Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
        Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        Category newCategory = categoryDAO.getCategory(category.getCategoryId());
        Assert.assertEquals("Category wasn't created properly", category.getName(), newCategory.getName());
        categoryDAO.deleteCategory(category.getCategoryId());
    }

    @Test
    public void getInvalidCategory(){
        Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNotNull("Error occured creating category", category);
        Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
        Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        Category newCategory = categoryDAO.getCategory(0);
        Assert.assertNull("Invalid category was retrieved", newCategory);
        categoryDAO.deleteCategory(category.getCategoryId());
    }

    @Test
    public void getCategories(){
        Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
        Assert.assertNotNull("Error occured creating category", category);
        Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
        Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        List<Category> categories = categoryDAO.getCategories();
        boolean contains = false;
        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getCategoryId() == category.getCategoryId()){
                contains = true;
                break;
            }
        }
        Assert.assertTrue("Category created wasn't found in list", contains);
        categoryDAO.deleteCategory(category.getCategoryId());
    }
}
