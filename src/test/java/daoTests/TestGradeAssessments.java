package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import models.Assessment;
import models.Grade;
import util.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class TestGradeAssessments {
    
    private static AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private static Assessment assessment;
    private static int associateId = 0;

    private static void emptyGrades(){
        String sql = "DELETE FROM grades";
        try(PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(sql)) {
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

    @Before
    public void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        List<Assessment> assessments = assessmentDAO.getAssessments();
        if(assessments.size() > 0)
            assessment = assessments.get(0);
        else
            assessment = null;
        associateId = findAssociateId();
        emptyGrades();
    }

    @Test
    public void testInsertSingleValidGrade(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNotNull("Error occured inserting grade into database", grade);
        Assert.assertTrue("Grade wasn't inserted correctly into database", grade.getGradeId() > 0);
        Assert.assertEquals("Grade score wasn't updated in database", 50, grade.getScore(), 0);

        grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNull("Grade inserted on assessment with existing grade", grade);
    }

    @Test
    public void testInsertGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, -1));
        Assert.assertNull("Invalid grade inserted into database", grade);
    }

    @Test
    public void testUpdateValidGrade(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNotNull("Error occured inserting grade into database", grade);

        grade.setScore(75);
        grade = assessmentDAO.updateGrade(grade);
        Assert.assertNotNull("Error occured updating grade in database", grade);
        Assert.assertEquals("Grade score wasn't updated in database", 75, grade.getScore(), 0);
    }

    @Test
    public void testUpdateGradeBelowLimit(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNotNull("Invalid grade inserted into database", grade);

        grade.setScore(-1);
        grade = assessmentDAO.updateGrade(grade);
        Assert.assertNull("Invalid grade was updated into database", grade);
    }

    @Test
    public void testGetValidGrade(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNotNull("Error occured inserting grade into database", grade);

        Grade returnedGrade = assessmentDAO.getGradeForAssociate(associateId, assessment.getAssessmentId());
        Assert.assertNotNull("Coudln't get grade from database", returnedGrade);
        Assert.assertEquals("Grade incorrect grade returned from database", grade.getScore(), returnedGrade.getScore(), 0);
        Assert.assertEquals("Grade incorrect grade returned from database", grade.getGradeId(), returnedGrade.getGradeId(), 0);
    }

    @Test
    public void testGetInvalidGrade(){
        Assume.assumeTrue("Couldn't find any assessments in database", assessment != null);
        Assume.assumeTrue("Couldn't find any associates in database", associateId > 0);

        Grade grade = assessmentDAO.insertGrade(new Grade(0, assessment.getAssessmentId(), associateId, 50));
        Assert.assertNotNull("Error occured inserting grade into database", grade);

        Grade returnedGrade = assessmentDAO.getGradeForAssociate(-1, assessment.getAssessmentId());
        Assert.assertNull("Invalid grade returned from database", returnedGrade);
        returnedGrade = assessmentDAO.getGradeForAssociate(associateId, -1);
        Assert.assertNull("Invalid grade returned from database", returnedGrade);
    }

}
