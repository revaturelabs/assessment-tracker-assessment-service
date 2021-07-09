package daoTests;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import models.Assessment;
import models.AssessmentType;
import models.Grade;
import models.Note;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAssessmentDao {

    private AssessmentDAO assessmentDAO = new AssessmentDAOImpl();
    private Assessment testAssessment = new Assessment(0, "testAssessment", 1, 1, "3", 100, 1, new ArrayList<String>());
    private AssessmentType testAssessmentType = new AssessmentType(0, "testAssessmentType", 50);
    //private Grade testGrade = new Grade(0, testAssessment.getAssessmentId(), 1, 5);
    private Grade testGrade = new Grade(0, 1, 1, 5);

    public TestAssessmentDao() throws InvalidValue {
    }

    @Test
    @Order(1)
    public void testCreateAssessment() {
        Assessment assessment = assessmentDAO.createAssessment(testAssessment);
        testAssessment = assessment;
        Assert.assertTrue(assessment.getAssessmentId() != 0);
    }
    //----------------------------------------------------------------------

    @Test
    @Order(2)
    public void testGetAssessments()  {
        assessmentDAO.createAssessment(testAssessment);
        List<Assessment> assessments = assessmentDAO.getAssessments();
        Assert.assertTrue(assessments.size() >= 1);
    }

    //----------------------------------------------------------------------

    @Test
    @Order(3)
    public void testGetAssessmentsByTraineeId() {
        List<Integer> oneBatches = new ArrayList<>();
        oneBatches.add(1);
        oneBatches.add(3);
        oneBatches.add(4);
        oneBatches.add(5);
        oneBatches.add(6);
        List<Assessment> assessments = assessmentDAO.getAssessmentsByTraineeId(1);
        for (Assessment a : assessments) {
            System.out.println(a.getBatchId());
            Assert.assertTrue(oneBatches.contains(a.getBatchId()));
        }
    }

    //----------------------------------------------------------------------

    @Test
    @Order(4)
    public void testGetWeekAssessments() {
       List<Grade> grades = assessmentDAO.getGradesForWeek(1, "3");
       for (Grade g : grades) {
           Assert.assertTrue(g.getAssociateId() == 1);
       }
    }

    //----------------------------------------------------------------------

    @Test
    @Order(5)
    public void testGetBatchWeek() {
        List<Assessment> assessments = assessmentDAO.getBatchWeek(1, "3");
        for (Assessment a : assessments) {
            Assert.assertTrue(a.getWeekId().equals("3"));
        }
    }

    //----------------------------------------------------------------------

    @Test
    @Order(6)
    public void testAdjustWeightTrue() {
        Assert.assertTrue(assessmentDAO.adjustWeight(testAssessment.getAssessmentId(),20));
    }

    //----------------------------------------------------------------------

    @Test
    @Order(7)
    public void testCreateAssessmentType() {
        AssessmentType assessmentType = assessmentDAO.createAssessmentType(testAssessmentType.getName(),testAssessmentType.getDefaultWeight());
        testAssessmentType = assessmentType;
        Assert.assertTrue(assessmentType.getTypeId() != 0);
    }

    //----------------------------------------------------------------------

    @Test
    @Order(8)
    public void testGetNotesForTrainee() {
        List<Note> notes = assessmentDAO.getNotesForTrainee(1, 1);
        Assert.assertTrue(notes.get(0) != null);
    }

    //----------------------------------------------------------------------
    @Test
    @Order(9)
    public void testInsertGrade() {
        Grade grade = assessmentDAO.insertGrade(testGrade);
        Assert.assertTrue(grade.getGradeId() != 0);
    }


    //----------------------------------------------------------------------

}
