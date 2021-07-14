package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import dao.GradeDAO;
import dao.GradeDAOImpl;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import models.Grade;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

import services.GradeService;
import services.GradeServiceImpl;
import util.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestGradeDAO {
    
    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static GradeService gradeService = new GradeServiceImpl(new GradeDAOImpl());
    private static GradeDAO gradeDAO = new GradeDAOImpl();
    private static Assessment assessment;
    private static Grade gradeValid, gradeValid2;
    private static int associateId = 0;
    private static int batchId = 0;

    private static int findAssociateId(){
        String sql = "SELECT * FROM associates";
        try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            return 0;
        }
    }

    @BeforeClass
    public static void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        try {
            assessment = assessmentDAO.createAssessment(new Assessment(0, "Test Assessment 1", 1, 1, "1", 30, 2));
            gradeValid = new Grade(0, assessment.getAssessmentId(), associateId, 50);
            gradeValid2 = new Grade(0, assessment.getAssessmentId(), associateId, 50);
        } catch(InvalidValue e) {
            fail();
        }
        associateId = findAssociateId();
    }

    @Test
    public void testInsertValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        try {
            gradeValid = gradeService.insertGrade(gradeValid);
            Assert.assertNotNull("Error occured inserting grade into database", gradeValid);
            Assert.assertTrue("Grade wasn't inserted correctly into database", gradeValid.getGradeId() > 0);
            Assert.assertEquals("Grade score wasn't updated in database", 50, gradeValid.getScore(), 0);
        } catch (DuplicateResource | ResourceNotFound | InvalidValue e) {
            fail();
        }
    }

    @Test
    public void testCreateGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        //BUG - Score is always set to 0 impossible to set invalid value
        try {
            gradeService.createGrade(new Grade(0, assessment.getAssessmentId(), associateId, -1));
            fail();
        } catch (InvalidValue e) {
            //Success
        } catch (DuplicateResource e) {
            fail();
        }
    }

    @Test
    public void testUpdateValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        try {
            gradeValid2 = gradeService.createGrade(gradeValid2);
            Grade updatedGrade = gradeService.updateGrade(new Grade(gradeValid2.getGradeId(), gradeValid2.getAssessmentId(), gradeValid2.getAssociateId(), 75));
            Assert.assertNotNull("Error occured updating grade in database", updatedGrade);
            Assert.assertEquals("Grade score wasn't updated in database", 75, updatedGrade.getScore(), 0);
        } catch (InvalidValue | DuplicateResource | ResourceNotFound e) {
            fail();
        }
    }

    @Test
    public void testUpdateGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        try {
            Grade resultGrade = gradeService.updateGrade(new Grade(gradeValid.getGradeId(), assessment.getAssessmentId(), associateId, -1));
            Assert.assertNull("Invalid grade was updated into database", resultGrade);
        } catch(InvalidValue e) {
            //Success
        } catch(DuplicateResource | ResourceNotFound e) {
            fail();
        }
    }

    @Test
    public void testGetValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        try {
            Grade returnedGrade = gradeService.getGrade(gradeValid.getAssessmentId(), gradeValid.getAssociateId());
            Assert.assertNotNull("Coudln't get grade from database", returnedGrade);
            Assert.assertEquals("Grade incorrect grade returned from database", gradeValid.getScore(), returnedGrade.getScore(), 0);
            Assert.assertEquals("Grade incorrect grade returned from database", gradeValid.getGradeId(), returnedGrade.getGradeId(), 0);
        } catch (ResourceNotFound  e) {
            fail();
        }
    }

    @Test
    public void testGetInvalidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        try {
            Grade returnedGrade = gradeService.getGrade(associateId, -1);
            Assert.assertNull("Invalid grade returned from database", returnedGrade);
            fail();
        } catch (ResourceNotFound e) {
            //Success
        }

        try {
            Grade returnedGrade = gradeService.getGrade(-1, assessment.getAssessmentId());
            Assert.assertNull("Invalid grade returned from database", returnedGrade);
            fail();
        } catch (ResourceNotFound e) {
            //Success
        }
    }

    @Test
    public void testGetAverageGrade() throws ResourceNotFound {
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);


        double returnAvgrade = gradeDAO.getAverageGrade(assessment.getAssessmentId());
        Assert.assertNotEquals("Invalid average grade from database", returnAvgrade, 0);
//        returnAvgrade = gradeDAO.getAverageGrade(assessment.getAssessmentId(), assessment.getWeekId(), assessment.getBatchId());
//        Assert.assertNotEquals("Invalid average grade return from database", returnAvgrade,);
    }

    @AfterClass
    public static void cleanup() {
        try {
            gradeService.deleteGrade(gradeValid.getGradeId());
            gradeService.deleteGrade(gradeValid2.getGradeId());
            assessmentDAO.deleteAssessment(assessment.getAssessmentId());
        } catch (ResourceNotFound | ResourceUnchangable e) {
            fail();
        }
    }

}
