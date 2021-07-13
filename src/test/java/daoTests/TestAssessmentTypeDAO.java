package daoTests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.AssessmentTypeDAO;
import dao.AssessmentTypeDAOImpl;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

public class TestAssessmentTypeDAO {

    private static AssessmentTypeDAO assessmentTypeDAO = new AssessmentTypeDAOImpl();
    private static AssessmentType assessmentType;

    public AssessmentType createAssessmentType(String name, int defaultWeight){
        AssessmentType assessmentType = null;
        try {
            assessmentType = assessmentTypeDAO.createAssessmentType(new AssessmentType(0, name, defaultWeight));
            Assert.assertNotNull("Error occured creating category", assessmentType);
            Assert.assertNotEquals(0, assessmentType.getTypeId());
            Assert.assertEquals(name, assessmentType.getName());
            Assert.assertEquals(defaultWeight, assessmentType.getDefaultWeight());
        } catch (DuplicateResource | InvalidValue e) {
            Assert.assertFalse(true);
        }
        return assessmentType;
    }

    public void deleteAssessmentType(AssessmentType assessmentType){
        try {
            assessmentTypeDAO.deleteAssessmentType(assessmentType.getTypeId());
            Assert.assertTrue(true);
        } catch (ResourceNotFound | ResourceUnchangable e) {
            Assert.assertFalse(true);
        }
    }

    @Before
    public void createValidAssessmentType(){
        assessmentType = createAssessmentType("Test Assessment Type", 50);
    }

    @After
    public void deleteValidAssessmentType(){
        deleteAssessmentType(assessmentType);
    }

    @Test
    public void deleteInvalidCategory(){
        try {
            assessmentTypeDAO.deleteAssessmentType(0);
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertTrue(true);
        } catch (ResourceUnchangable e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void createDuplicateAssessmentType(){
        try {
            assessmentTypeDAO.createAssessmentType(new AssessmentType(0, assessmentType.getName(), assessmentType.getDefaultWeight()));
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertTrue(true);
        } catch (InvalidValue e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void createEmptyAssessmentType(){
        try {
            assessmentTypeDAO.createAssessmentType(new AssessmentType(0, "", assessmentType.getDefaultWeight()));
            Assert.assertFalse(true);
        } catch (DuplicateResource e) {
            Assert.assertFalse(true);
        } catch (InvalidValue e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getValidAssessmentType(){
        try {
            AssessmentType returnedAssessmentType = assessmentTypeDAO.getAssessmentType(assessmentType.getTypeId());
            Assert.assertEquals(assessmentType.getTypeId(), returnedAssessmentType.getTypeId());
            Assert.assertEquals(assessmentType.getName(), returnedAssessmentType.getName());
            Assert.assertEquals(assessmentType.getDefaultWeight(), returnedAssessmentType.getDefaultWeight());
        } catch (ResourceNotFound e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void getInvalidAssessmentType() {
        try {
            assessmentTypeDAO.getAssessmentType(0);
            Assert.assertFalse(true);
        } catch (ResourceNotFound e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getAssessmentTypes() {
        List<AssessmentType> assessmentTypes = assessmentTypeDAO.getAssessmentTypes();
        boolean contains = false;
        for (int i = 0; i < assessmentTypes.size(); i++) {
            if (assessmentTypes.get(i).getTypeId() == assessmentType.getTypeId()) {
                Assert.assertEquals(assessmentType.getName(), assessmentTypes.get(i).getName());
                Assert.assertEquals(assessmentType.getDefaultWeight(), assessmentTypes.get(i).getDefaultWeight());
                contains = true;
                break;
            }
        }
        Assert.assertTrue(contains);
    }
}
