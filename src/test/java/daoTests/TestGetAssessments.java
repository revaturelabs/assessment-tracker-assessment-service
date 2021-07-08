package daoTests;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import models.Assessment;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class TestGetAssessments {
    // Class to be tested
    private AssessmentDAOImpl adao;

    @Before
    public void setup() {
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();
    }

    @Test
    public void testgetAssessments() {
        ArrayList<String> sampleNotes1 = new ArrayList<String>();
        sampleNotes1.add("Good Day");
        sampleNotes1.add("Bad Day");
        sampleNotes1.add("Need Feedback");
        try {
            Assessment sampleAssessment1 = new Assessment(1, "Test Assessment 1", 1, 1, "1", 30, 2, sampleNotes1);
            adao.createAssessment(sampleAssessment1);
            List<Assessment> afterCreatingAssessments = adao.getAssessments();
            // Test to for correct number of Assessments
            assertTrue(afterCreatingAssessments.size() >= 1);
        } catch(InvalidValue e) {
            fail();
        }
    }
}
