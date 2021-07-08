package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import models.Assessment;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class TestAdjustWeight {

    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static List<Assessment> assessments;

    @Before
    public void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        assessments = assessmentDAO.getAssessments();
    }

    @Test
    public void testSettingValidWeightBottomLimit(){
        Assume.assumeTrue("Couldn't get assessments from dao", assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        boolean result = assessmentDAO.adjustWeight(assessment.getAssessmentId(), 0);
        Assert.assertTrue("Valid weight was not accepted", result);
        List<Assessment> assessmentsUpdated = assessmentDAO.getAssessments();
        Assessment assessmentUpdated = null;
        for(int i = 0; i < assessmentsUpdated.size(); i++){
            if(assessmentsUpdated.get(i).getAssessmentId() == assessment.getAssessmentId()){
                assessmentUpdated = assessmentsUpdated.get(i);
            }
        }
        Assert.assertEquals("Assessment weight not updated in database", 0, assessmentUpdated.getAssessmentWeight());
    }

    @Test
    public void testSettingValidWeightTopLimit(){
        Assume.assumeTrue("Couldn't get assessments from dao", assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        boolean result = assessmentDAO.adjustWeight(assessment.getAssessmentId(), 100);
        Assert.assertTrue("Valid weight was not accepted", result);
        List<Assessment> assessmentsUpdated = assessmentDAO.getAssessments();
        Assessment assessmentUpdated = null;
        for(int i = 0; i < assessmentsUpdated.size(); i++){
            if(assessmentsUpdated.get(i).getAssessmentId() == assessment.getAssessmentId()){
                assessmentUpdated = assessmentsUpdated.get(i);
                break;
            }
        }
        Assert.assertEquals("Assessment weight not updated in database", 100, assessmentUpdated.getAssessmentWeight());
    }

    @Test
    public void testSettingBelowWeightLimit(){
        Assume.assumeTrue("Couldn't get assessments from dao", assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        boolean result = assessmentDAO.adjustWeight(assessment.getAssessmentId(), -1);
        Assert.assertFalse("Invalid weight was accepted", result);
        List<Assessment> assessmentsUpdated = assessmentDAO.getAssessments();
        Assessment assessmentUpdated = null;
        for(int i = 0; i < assessmentsUpdated.size(); i++){
            if(assessmentsUpdated.get(i).getAssessmentId() == assessment.getAssessmentId()){
                assessmentUpdated = assessmentsUpdated.get(i);
                break;
            }
        }
        Assert.assertNotEquals("Assessment weight was updated in database", -1, assessmentUpdated.getAssessmentWeight());
    }

    @Test
    public void testSettingOverWeightLimit(){
        Assume.assumeTrue("Couldn't get assessments from dao", assessments.size() > 0);
        Assessment assessment = assessments.get(0);
        boolean result =assessmentDAO.adjustWeight(assessment.getAssessmentId(), 101);
        Assert.assertFalse("Invalid weight was accepted", result);
        List<Assessment> assessmentsUpdated = assessmentDAO.getAssessments();
        Assessment assessmentUpdated = null;
        for(int i = 0; i < assessmentsUpdated.size(); i++){
            if(assessmentsUpdated.get(i).getAssessmentId() == assessment.getAssessmentId()){
                assessmentUpdated = assessmentsUpdated.get(i);
                break;
            }
        }
        Assert.assertNotEquals("Assessment weight was updated in database", 101, assessmentUpdated.getAssessmentWeight());
    }
}
