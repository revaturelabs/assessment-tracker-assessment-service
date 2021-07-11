package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;

import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestAdjustWeight {

    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static Assessment assessment;

    @Before
    public void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        try {
            assessment = new Assessment(0, "Delete now", 1, 3, "5", 20, 1);
            assessment = assessmentDAO.createAssessment(assessment);
        } catch (InvalidValue e) {
            //BUG - Should display something
        }
    }

    @Test
    public void testSettingValidWeight(){
        try {
            Assert.assertTrue(assessmentDAO.adjustWeight(assessment.getAssessmentId(), 50));
        } catch (InvalidValue | ResourceNotFound e) {
            fail();
        }
    }

    @Test
    public void testSettingInvalidWeight() {
        try {
            assessmentDAO.adjustWeight(assessment.getAssessmentId(), -50);
            fail();
        } catch (InvalidValue e){
            //Success
        } catch (ResourceNotFound e) {
            fail();
        }
    }

    @Test
    public void testInvalidId() {
        try {
            assessmentDAO.adjustWeight(-1, 50);
            fail();
        } catch (InvalidValue e){
            fail();
        } catch (ResourceNotFound e) {
            //Success
        }
    }

    @After
    public void cleanup() {
        try {
            assessmentDAO.deleteAssessment(assessment.getAssessmentId());
        } catch(ResourceNotFound | ResourceUnchangable e) {
            //BUG - Should display something
        }
    }
}
