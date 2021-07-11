package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import models.Assessment;
import models.Grade;
import org.checkerframework.checker.units.qual.A;
import org.junit.*;
import org.junit.jupiter.api.Order;
import util.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestGradeAssessments {
    
    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static Assessment assessment;
    private static Grade gradeValid, gradeValid2, gradeValid3;
    private static int associateId = 0;

    private static void emptyGrade(Grade grade){
        if(grade == null) return;
        String sql = "DELETE FROM grades where id=?";
        try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
            ps.setInt(1, grade.getGradeId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            gradeValid = new Grade(0, assessment.getAssessmentId(), associateId, 50)
        } catch(InvalidValue e) {
            fail();
        }
        associateId = findAssociateId();
    }

    @Test
    public void testInsertValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        gradeValid = assessmentDAO.insertGrade(gradeValid);
        Assert.assertNotNull("Error occured inserting grade into database", gradeValid);
        Assert.assertTrue("Grade wasn't inserted correctly into database", gradeValid.getGradeId() > 0);
        Assert.assertEquals("Grade score wasn't updated in database", 50, gradeValid.getScore(), 0);
    }

    @Test
    public void testInsertGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        //BUG - Score is always set to 0 impossible to set invalid value
        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, -1));
        Assert.assertNull("Invalid grade inserted into database", grade);
    }

    @Test
    public void testUpdateValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        gradeValid3 = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        gradeValid3 = assessmentDAO.updateGrade(new Grade(gradeValid3.getGradeId(), gradeValid3.getAssessmentId(), gradeValid3.getAssociateId(), 75));
        Assert.assertNotNull("Error occured updating grade in database", gradeValid3);
        Assert.assertEquals("Grade score wasn't updated in database", 75, gradeValid.getScore(), 0);
    }

    @Test
    public void testUpdateGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);
        Grade resultGrade = assessmentDAO.updateGrade(new Grade(gradeValid.getGradeId(), assessment.getAssessmentId(), associateId, -1));
        Assert.assertNull("Invalid grade was updated into database", resultGrade);
    }

    @Test
    public void testGetValidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        gradeValid2 = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Grade returnedGrade = assessmentDAO.getGradeForAssociate(gradeValid2.getAssociateId(), gradeValid2.getAssessmentId());
        Assert.assertNotNull("Coudln't get grade from database", returnedGrade);
        Assert.assertEquals("Grade incorrect grade returned from database", gradeValid2.getScore(), returnedGrade.getScore(), 0);
        Assert.assertEquals("Grade incorrect grade returned from database", gradeValid2.getGradeId(), returnedGrade.getGradeId(), 0);
    }

    @Test
    public void testGetInvalidGrade(){
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade returnedGrade = assessmentDAO.getGradeForAssociate(-1, assessment.getAssessmentId());
        Assert.assertNull("Invalid grade returned from database", returnedGrade);
        returnedGrade = assessmentDAO.getGradeForAssociate(associateId, -1);
        Assert.assertNull("Invalid grade returned from database", returnedGrade);
    }

    @AfterClass
    public static void cleanup() {
        emptyGrade(gradeValid);
        emptyGrade(gradeValid2);
        emptyGrade(gradeValid3);
    }

}
