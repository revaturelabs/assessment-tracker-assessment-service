package daoTests;

import dao.AssessmentTypeDAO;
import dao.AssessmentTypeDAOImpl;
import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

import org.junit.AfterClass;
import org.junit.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateAssessmentType {

    private static AssessmentTypeDAO assessmentTypeDAO;
    private static AssessmentType AssessType1, AssessType2;

    @BeforeClass
    public static void setup() {
        assessmentTypeDAO = new AssessmentTypeDAOImpl();
    }

    @Test
    public void testCreateAssessmentType() {
        try {
            AssessType1 = new AssessmentType(0, "Test", 50);
            AssessType1 = assessmentTypeDAO.createAssessmentType(AssessType1);
            Assert.assertTrue(AssessType1.getTypeId() != 0);
        } catch(DuplicateResource e) {
            fail();
        }
    }

    @Test
    public void testCreateDupAssessmentType() {
        try {
            AssessType2 = new AssessmentType(0, "Code", 50);
            AssessType2 = assessmentTypeDAO.createAssessmentType(AssessType2);
            assessmentTypeDAO.createAssessmentType(AssessType2);
            fail();
        } catch(DuplicateResource e) {
            //Success
        }
    }

    @AfterClass
    public static void cleanup() {
        try {
            assessmentTypeDAO.deleteAssessmentType(AssessType1.getTypeId());
            assessmentTypeDAO.deleteAssessmentType(AssessType2.getTypeId());
        } catch(ResourceNotFound | ResourceUnchangable e) {
            //Should not get here
            fail();
        }
    }

}
