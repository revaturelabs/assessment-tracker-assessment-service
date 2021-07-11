package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import dao.AssessmentTypeDAO;
import dao.AssessmentTypeDAOImpl;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import models.AssessmentType;

import org.junit.AfterClass;
import org.junit.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignAssessmentType {

    private static AssessmentDAO assessmentDAO;
    private static AssessmentTypeDAO assessmentTypeDAO;
    private static AssessmentType assessType;
    private static Assessment assessment;

    @BeforeClass
    public static void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        assessmentTypeDAO = new AssessmentTypeDAOImpl();
        try {
            assessment = new Assessment(0, "Test Assessment 1", 1, 1, "1", 30, 2);
            assessment = assessmentDAO.createAssessment(assessment);
            assessType = new AssessmentType(0,"QC-Extra", 100);
            assessType = assessmentTypeDAO.createAssessmentType(assessType);
        } catch(InvalidValue | DuplicateResource e) {
            fail();
        }
    }

    @Test
    public void testAssignAssessmentTypeValid() {
        try {
            boolean result = assessmentDAO.assignAssessmentType(assessment.getAssessmentId(), assessType.getTypeId());
            Assert.assertTrue(result);
        } catch(SQLException | ResourceNotFound | InvalidValue e) {
            fail();
        }
    }

    @Test
    public void testAssignAssessmentInvalidAssessId() {
        try {
            assessmentDAO.assignAssessmentType(-1, assessType.getTypeId());
            fail();
        } catch(SQLException e) {
            e.printStackTrace();
            fail();
        } catch( ResourceNotFound e) {
            //Success
        } catch (InvalidValue e) {
            //BUG - Database issues
            fail();
        }
    }

    @Test
    public void testAssignAssessmentInvalidTypeId() {
        try {
            assessmentDAO.assignAssessmentType(assessment.getAssessmentId(), -1);
            fail();
        } catch(SQLException | ResourceNotFound e) {
            //Success
        } catch (InvalidValue e) {
            //BUG - Database issues
            fail();
        }
    }

    @AfterClass
    public static void cleanup() {
        try {
            assessmentDAO.deleteAssessment(assessment.getAssessmentId());
            assessmentTypeDAO.deleteAssessmentType(assessType.getTypeId());
        } catch(ResourceNotFound |  ResourceUnchangable e) {
            fail();
        }
    }

}
