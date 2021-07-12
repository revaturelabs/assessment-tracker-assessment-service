package daoTests;

import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateAssessment {
    // Class to be tested
    private AssessmentDAOImpl adao;

    @Before
    public void setup() throws Exception {
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();
    }

    @Test
    public void testCreateAssessmentPass() {
        try {
            Assessment assessValid = new Assessment(0, "Delete now", 1, 3, "5", 20, 1);
            Assessment returnedAssessment = adao.createAssessment(assessValid);
            //Store returned value for cleanup
            assessValid = returnedAssessment;
            //Ensure correct info is in the database
            assertEquals("Delete now", returnedAssessment.getAssessmentTitle());
            assertEquals(1, returnedAssessment.getTypeId());
            assertEquals(3, returnedAssessment.getBatchId());
            assertEquals("5", returnedAssessment.getWeekId());
            assertEquals(20, returnedAssessment.getAssessmentWeight());
            assertEquals(1, returnedAssessment.getCategoryId());
            //Cleanup
            adao.deleteAssessment(assessValid.getAssessmentId());
        } catch (InvalidValue | ResourceNotFound | ResourceUnchangable e) {
            fail();
        }
    }

    @Test
    public void testNullWeekIdFail() {
        try {
            Assessment assessInvalidWeek = new Assessment(0, "Delete now", 2, 4, null, 2, 2);
            assessInvalidWeek = adao.createAssessment(assessInvalidWeek);
            //cleanup - just incase this goes through
            adao.deleteAssessment(assessInvalidWeek.getAssessmentId());
            fail();
        } catch (InvalidValue e) {
            //Success
        } catch (ResourceNotFound | ResourceUnchangable e) {
            fail();
        }
    }

    @Test
    public void testNullAssessmentTitleFail() {
        try {
            Assessment assessInvalidTitle = new Assessment(0, null, 3, 4, "6", 22, 3);
            assessInvalidTitle = adao.createAssessment(assessInvalidTitle);
            //cleanup - just incase this goes through
            adao.deleteAssessment(assessInvalidTitle.getAssessmentId());
            fail();
        } catch (InvalidValue e) {
            //Success
        } catch (ResourceNotFound | ResourceUnchangable e) {
            fail();
        }
    }
}