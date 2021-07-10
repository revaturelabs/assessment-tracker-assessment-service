package daoTests;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import models.Assessment;

import org.eclipse.jetty.util.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class TestGetAssessments {
    // Class to be tested
    private AssessmentDAOImpl adao;
    private Assessment assessment;

    @Before
    public void setup() {
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();
        try {
            Assessment sampleAssessment1 = new Assessment(0, "Test Assessment 1", 1, 1, "1", 30, 2);
            assessment = adao.createAssessment(sampleAssessment1);
        } catch(InvalidValue e) {
            //BUG - Display something if we get here
        }
    }

    @Test
    public void testgetAssessments() {
        List<Assessment> afterCreatingAssessments = adao.getAssessments();
        // Test for correct number of Assessments
        assertTrue(afterCreatingAssessments.size() >= 1);
    }

    @Test
    public void testGetAssessmentByIdValid() {
        try {
            Assessment result = adao.getAssessmentById(assessment.getAssessmentId());
            Assert.assertNotNull(result);
        } catch(ResourceNotFound e) {
            fail();
        } catch(InvalidValue e) {
            //Success - However issue in the database
        }
    }

    @Test
    public void testGetAssessmentByIdInvalid() {
        try {
            adao.getAssessmentById(-1);
            fail();
        } catch(ResourceNotFound e) {
            //Success
        } catch(InvalidValue e) {
            fail();
        }
    }

    @After
    public void cleanup() {
        try {
            adao.deleteAssessment(assessment.getAssessmentId());
        } catch (ResourceNotFound e) {
            //
        }
    }

}
