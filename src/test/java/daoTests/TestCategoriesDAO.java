package daoTests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import dao.CategoryDAO;
import dao.CategoryDAOImpl;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import models.Category;

public class TestCategoriesDAO {
    private static CategoryDAO categoryDAO = new CategoryDAOImpl();
    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static Category category;

    public Category createCategory(String categoryName){
        Category category = null;
        try {
            category = categoryDAO.createCategory(new Category(0, categoryName));
            Assert.assertNotNull("Error occured creating category", category);
            Assert.assertTrue("Category ID not updated", category.getCategoryId() > 0);
            Assert.assertEquals("Category wasn't created properly", categoryName, category.getName());
        } catch (DuplicateResource | InvalidValue e) {
            Assert.assertFalse("Exception called creating category when no exception should have been called", true);
        }
        return category;
    }

    public void deleteCategory(Category category){
        try {
            categoryDAO.deleteCategory(category.getCategoryId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse("Exception was thrown deleting a valid category", true);
        }
    }

    @Before
    public void createValidCategory() {
        category = createCategory("Test Category");
    }

    @After
    public void deleteValidCategory(){
        deleteCategory(category);
    }

    @Test
    public void deleteInvalidCategory(){
        try {
            categoryDAO.deleteCategory(0);
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertTrue(true);
        } catch (ResourceUnchangable e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void createDuplicateCategory() {
        try {
            categoryDAO.createCategory(new Category(0, "Test Category"));
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertTrue(true);
        } catch (InvalidValue e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void createEmptyCategory() {
        try {
            categoryDAO.createCategory(new Category(0, ""));
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertFalse(true);
        } catch (InvalidValue e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getValidCategory() {
        try {
            Category returnedCategory = categoryDAO.getCategory(category.getCategoryId());
            Assert.assertEquals("Category IDs did not match", category.getCategoryId(), returnedCategory.getCategoryId());
            Assert.assertEquals("Category names did not match", category.getName(), returnedCategory.getName());
        } catch (ResourceNotFound e) {
            Assert.assertFalse("Error getting valid category from dao", true);
        }
    }

    @Test
    public void getInvalidCategory() {
        try {
            categoryDAO.getCategory(0);
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getAllCategories() {
        List<Category> categories = categoryDAO.getCategories();
        boolean contains = false;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryId() == category.getCategoryId()) {
                contains = true;
                break;
            }
        }
        Assert.assertTrue("Category created wasn't found in list", contains);
    }

    @Test
    public void updateValidCategory(){
        try {
            category.setName("New Test Category");
            Category returnedCategory = categoryDAO.updateCategory(category);
            Assert.assertNotNull("Error occured updating category", returnedCategory);
            Assert.assertEquals("Category IDs did not match", category.getCategoryId(), returnedCategory.getCategoryId());
            Assert.assertEquals("Category names did not match", category.getName(), returnedCategory.getName());
        } catch (ResourceNotFound | DuplicateResource | InvalidValue e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void updateInvalidCategory(){
        try {
            categoryDAO.updateCategory(new Category(0, "Updated Category Name"));
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertTrue(true);
        } catch (DuplicateResource e) {
            Assert.assertFalse(true);
        } catch (InvalidValue e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void updateDuplicateCategory(){
        Category newCategory = createCategory("New Test Category");
        try {
            categoryDAO.updateCategory(new Category(category.getCategoryId(), newCategory.getName()));
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertTrue(true);
        } catch (InvalidValue e) {
            Assert.assertFalse(true);
        }
        deleteCategory(newCategory);
    }

    @Test
    public void updateEmptyCategory(){
        try {
            categoryDAO.updateCategory(new Category(category.getCategoryId(), ""));
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertFalse(true);
        } catch (InvalidValue e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void addRemoveCategory(){
        List<Assessment> assessments = assessmentDAO.getAssessments();
        Assume.assumeTrue(assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        try {
            Category returnedCategory = categoryDAO.addCategory(assessment.getAssessmentId(), category.getCategoryId());
            Assert.assertEquals(category.getCategoryId(), returnedCategory.getCategoryId());
            Assert.assertEquals(category.getName(), returnedCategory.getName());
            categoryDAO.removeCategory(assessment.getAssessmentId(), returnedCategory.getCategoryId());
        } catch (ResourceNotFound | InvalidValue e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void getCategories(){
        List<Assessment> assessments = assessmentDAO.getAssessments();
        Assume.assumeTrue(assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        try {
            Category returnedCategory = categoryDAO.addCategory(assessment.getAssessmentId(), category.getCategoryId());
            Assert.assertEquals(category.getCategoryId(), returnedCategory.getCategoryId());
            Assert.assertEquals(category.getName(), returnedCategory.getName());
        } catch (ResourceNotFound | InvalidValue e) {
            Assert.assertFalse(true);
        }
        List<Category> returnedCategories = categoryDAO.getCategories(assessment.getAssessmentId());
        Assert.assertTrue(returnedCategories.size() == 1);
        try {
            categoryDAO.removeCategory(assessment.getAssessmentId(), category.getCategoryId());
        } catch (ResourceNotFound e) {
            Assert.assertFalse(true);
        }
    }
}
