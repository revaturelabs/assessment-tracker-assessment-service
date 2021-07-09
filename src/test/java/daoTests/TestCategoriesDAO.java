package daoTests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public class TestCategoriesDAO {
    private static CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Test
    public void createValidCategory() {
        try {
            Category category = categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | DuplicateResource | ResourceUnchangable e) {
            Assert.assertFalse("Exception called when no exception should have been called", true);
        }
    }

    @Test
    public void createDuplicateCategory() { // Database must require unique names for this test to pass
        Category category = null;
        try {
            category = categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        } catch (DuplicateResource e) {
            Assert.assertFalse("Exception called when no exception should have been called", true);
        }
        try{
            categoryDAO.createCategory(new Category(0, category.getName()));
            Assert.assertFalse("Exception wasn't thrown for duplicate name", true);
        }
        catch(DuplicateResource e){
        }
        try {
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse("Exception was thrown deleting a valid category", true);
        }
    }

    @Test
    public void getValidCategory() {
        Category category = null;
        try {
            category = categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
            Category newCategory = categoryDAO.getCategory(category.getCategoryId());
            Assert.assertEquals("Category wasn't created properly", category.getName(), newCategory.getName());
        } catch (ResourceNotFound | DuplicateResource e) {
            Assert.assertFalse("Exception created getting a valid category", true);
        }
        try {
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse("Exception was thrown deleting a valid category", true);
        }
    }

    @Test
    public void getInvalidCategory() {
        Category category = null;
        try {
            category = categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        } catch (DuplicateResource e) {
            Assert.assertFalse("Exception created getting a valid category", true);
        }
        try {
            categoryDAO.getCategory(0);
            Assert.assertFalse("Invalid category was retrieved", true);
        } catch (ResourceNotFound e) {
        }
        try {
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse("Exception was thrown deleting a valid category", true);
        }
    }

    @Test
    public void getCategories() {
        Category category = null;
        try {
            category = categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", "Test Category", category.getName());
        } catch (DuplicateResource e) {
            Assert.assertFalse("Exception created getting a valid category", true);
        }
        List<Category> categories = categoryDAO.getCategories();
        boolean contains = false;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryId() == category.getCategoryId()) {
                contains = true;
                break;
            }
        }
        Assert.assertTrue("Category created wasn't found in list", contains);
        try {
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse("Exception was thrown deleting a valid category", true);
        }
    }
}
