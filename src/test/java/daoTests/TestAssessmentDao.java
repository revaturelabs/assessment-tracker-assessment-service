package daoTests;

import dao.*;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import models.AssessmentType;
import models.Grade;
import models.Note;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.Test;
import services.GradeService;
import services.GradeServiceImpl;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAssessmentDao {

    private static AssessmentDAO assessmentDAO;
    private static AssessmentTypeDAO assessmentTypeDAO;
    private static Assessment testAssessment;
    private static AssessmentType testAssessmentType;
    private static GradeService gradeService;
    private static Grade testGrade;

    @BeforeClass
    public static void setup() {
        assessmentDAO = new AssessmentDAOImpl();
        assessmentTypeDAO = new AssessmentTypeDAOImpl();
        gradeService = new GradeServiceImpl(new GradeDAOImpl());
        try {
            testAssessment = new Assessment(0, "testAssessment", 1, 1, "3", 50, 1);
            testGrade = new Grade(0, 1, 1, 5);
        } catch(InvalidValue e) {
            fail();
        }
        testAssessmentType = new AssessmentType(0, "testAssessmentType", 50);
    }

    @Test
    @Order(1)
    public void testCreateAssessment() {
        try {
            testAssessment = assessmentDAO.createAssessment(testAssessment);
            Assert.assertNotEquals(testAssessment.getAssessmentId(), 0);
        } catch (InvalidValue e) {
            fail();
        }
    }

    @Test
    @Order(2)
    public void testGetAssessments()  {
        List<Assessment> assessments = assessmentDAO.getAssessments();
        Assert.assertTrue(assessments.size() >= 1);
    }

//    @Test
//    @Order(3)
//    public void testGetAssessmentsByTraineeId() {
//        List<Integer> oneBatches = new ArrayList<>();
//        oneBatches.add(1);
//        oneBatches.add(3);
//        oneBatches.add(4);
//        oneBatches.add(5);
//        oneBatches.add(6);
//        List<Assessment> assessments = assessmentDAO.getAssessmentsByAssociateId(1);
//        for (Assessment a : assessments) {
//            System.out.println(a.getBatchId());
//            Assert.assertTrue(oneBatches.contains(a.getBatchId()));
//        }
//    }

    // @Test
    // @Order(4)
    // public void testGetWeekAssessments() {
    //     try {
    //         List<Grade> grades = gradeService.getGradesForWeek(1, "3");
    //         for (Grade g : grades) {
    //             Assert.assertTrue(g.getAssociateId() == 1);
    //         }
    //     } catch(InvalidValue e) {
    //         fail();
    //     }
    // }

    @Test
    @Order(5)
    public void testGetBatchWeek() {
        List<Assessment> assessments = assessmentDAO.getBatchWeek(1, "3");
        for (Assessment a : assessments) {
            Assert.assertEquals(a.getWeekId(), "3");
        }
    }

    @Test
    public void testAdjustWeightTrue() {
        try {
            Assert.assertNotNull(assessmentDAO.adjustWeight(testAssessment.getAssessmentId(),20));
        } catch (InvalidValue | ResourceNotFound e) {
            fail();
        }
    }

    //BUG - This test really doesnt belong here
    //@Test
    //@Order(7)
    //public void testCreateAssessmentType() {
    //    try {
    //        testAssessmentType = assessmentTypeDAO.createAssessmentType(testAssessmentType);
    //        Assert.assertTrue(testAssessmentType.getTypeId() != 0);
    //    } catch(DuplicateResource | InvalidValue e) {
    //        fail();
    //    }
    //}

    @Test
    @Order(8)
    public void testGetNotesForTrainee() {
        List<Note> notes = assessmentDAO.getNotesForTrainee(1, 1);
        Assert.assertNotNull(notes.get(0));
    }

    @Test
    @Order(9)
    public void testInsertGrade() {
        try {
            Grade grade = gradeService.insertGrade(testGrade);
            Assert.assertNotEquals(grade.getGradeId(), 0);
        } catch (DuplicateResource | ResourceNotFound | InvalidValue e) {
            fail();
        }
    }

    @AfterClass
    public static void cleanup() {
        try {
            assessmentTypeDAO.deleteAssessmentType(testAssessmentType.getTypeId());
            assessmentDAO.deleteAssessment(testAssessment.getAssessmentId());

        } catch(ResourceNotFound | ResourceUnchangable e) {
            fail();
        }
    }
}
