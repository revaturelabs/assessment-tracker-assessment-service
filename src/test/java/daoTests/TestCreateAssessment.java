package daoTests;

import dao.AssessmentDAOImpl;
import models.Assessment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateAssessment {
    // Class to be tested
    private AssessmentDAOImpl adao;

    // Test Data
    private Assessment sa1, sa2, sa3, sa4;

    @Before
    public void setup() throws Exception {
        // Initialize the class to be tested
        adao = new AssessmentDAOImpl();

        ArrayList<String> sampleNote = new ArrayList<String>();
        sampleNote.add("Needs Improvement");

        // Create sample assessments"
        sa1 = new Assessment(0, "Delete later", 1, 3, "5", 20, 1, sampleNote);
        sa2 = new Assessment(0, "Delete later", 2, 4, null, 2, 2, sampleNote);
        sa3 = new Assessment(0, null, 3, 4, "6", 22, 3, sampleNote);
    }

    @Test
    public void testCreateAssessmentPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertNotNull(returnedAssessment);
    }

    @Test
    public void testCorrectAssessmentTitlePass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals("Delete later", returnedAssessment.getAssessmentTitle());
    }

    @Test
    public void testCorrectTypeIdPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals(1, returnedAssessment.getTypeId());
    }

    @Test
    public void testCorrectBatchIdPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals(3, returnedAssessment.getBatchId());
    }

    @Test
    public void testCorrectWeekIdPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals("5", returnedAssessment.getWeekId());
    }

    @Test
    public void testCorrectAssessmentWeightPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals(20, returnedAssessment.getAssessmentWeight());
    }

    @Test
    public void testCorrectCategoryIdPass() {
        Assessment returnedAssessment = adao.createAssessment(sa1);
        assertEquals(1, returnedAssessment.getCategoryId());
    }


    @Test
    public void testNullWeekIdFail() {
        Assessment returnedAssessment = adao.createAssessment(sa2);
        assertNull(returnedAssessment);
    }

    @Test
    public void testNullAssessmentTitleFail() {
        Assessment returnedAssessment = adao.createAssessment(sa3);
        assertNull(returnedAssessment);
    }

}