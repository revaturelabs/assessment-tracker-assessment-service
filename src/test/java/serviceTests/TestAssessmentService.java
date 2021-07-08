

package serviceTests;
import dao.AssessmentDAOImpl;
import models.Assessment;
import models.AssessmentType;
import models.Grade;
import models.Note;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import services.AssessmentService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestAssessmentService {

    @Mock
    AssessmentDAOImpl adao;

    @InjectMocks
    AssessmentService assessmentService;

    @Before
    public void setup(){
        // Initialize the class to be tested
        assessmentService = new AssessmentService();
        MockitoAnnotations.openMocks(this);

    }

    //----------------------------------------------------------------------

    @Test
    public void testGetAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment());
        Mockito.when(adao.getAssessments()).thenReturn(assessments);
        List<Assessment> newAssessments =assessmentService.getAssessments();
        Assert.assertFalse(newAssessments.isEmpty());

    }

    // @Test
    // public void testGetAssessmentsSQLException() {
    //     Mockito.when(adao.getAssessments()).thenThrow(SQLException.class);
    //     assessmentService.getAssessments();
    // }

    //----------------------------------------------------------------------

    @Test
    public void testGetAssessmentsByTraineeIdNotEmpty() {
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment());
        Mockito.when(adao.getAssessmentsByTraineeId(2)).thenReturn(assessments);
        List<Assessment> newAssessments =assessmentService.getAssessmentsByTraineeId(2);
        Assert.assertFalse(newAssessments.isEmpty());

    }
    @Test
    public void testGetAssessmentsByTraineeIdEmpty() {
        List<Assessment> assessments = new ArrayList<>();
        Mockito.when(adao.getAssessmentsByTraineeId(2)).thenReturn(assessments);
        List<Assessment> newAssessments =assessmentService.getAssessmentsByTraineeId(2);
        Assert.assertTrue(newAssessments.isEmpty());

    }

    // @Test
    // public void testGetAssessmentsByTraineeIdSqlException() {
    //     Mockito.when(adao.getAssessmentsByTraineeId(2)).thenThrow(SQLException.class);
    //     assessmentService.getAssessmentsByTraineeId(2);
    // }

    //----------------------------------------------------------------------

    @Test
    public void testGetWeekAssessmentsNotEmpty() {
        List<Grade> grades = new ArrayList<>();
        grades.add(new Grade());
        Mockito.when(adao.getGradesForWeek(1, "something")).thenReturn(grades);
        List<Grade> newGrades =assessmentService.getGradesForWeek(1, "something");
        Assert.assertFalse(newGrades.isEmpty());

    }

    @Test
    public void testGetWeekAssessmentsEmpty() {
        List<Grade> grades = new ArrayList<>();
        Mockito.when(adao.getGradesForWeek(1, "something")).thenReturn(grades);
        List<Grade> newGrades =assessmentService.getGradesForWeek(1, "something");
        Assert.assertTrue(newGrades.isEmpty());

    }

    // @Test
    // public void testGetWeekAssessmentsEmptySqlException() {
    //     Mockito.when(adao.getGradesForWeek(2, "something")).thenThrow(SQLException.class);
    //     assessmentService.getGradesForWeek(2, "something");
    // }

    //----------------------------------------------------------------------

    @Test
    public void testCreateAssessment() {
        Assessment assessment = new Assessment();
        Mockito.when(adao.createAssessment(assessment)).thenReturn(assessment);
        Assessment newAssessment =assessmentService.createAssessment(assessment);
        Assert.assertSame(assessment, newAssessment);
    }

    // @Test
    // public void testCreateAssessmentSQLException() {
    //     Assessment assessment = new Assessment();
    //     Mockito.when(adao.createAssessment(assessment)).thenThrow(SQLException.class);
    //     assessmentService.createAssessment(assessment);
    // }

    //----------------------------------------------------------------------

//    @Test
//    public void testAdjustWeight() throws Exception {
//        Assessment assessment = new Assessment();
//        Mockito.when(adao.createAssessment(assessment)).thenReturn(assessment);
//        Assessment newAssessment =adao.createAssessment(assessment);
//        Assert.assertTrue(assessment==newAssessment);
//    }

    //----------------------------------------------------------------------

    @Test
    public void testCreateAssessmentType() {
        AssessmentType assessmentType = new AssessmentType();
        Mockito.when(adao.createAssessmentType(assessmentType.getName(),assessmentType.getDefaultWeight())).thenReturn(assessmentType);
        AssessmentType newAssessmentType =assessmentService.createAssessmentType(assessmentType);
        Assert.assertSame(newAssessmentType, assessmentType);
    }

    // @Test
    // public void testCreateAssessmentTypeSQLException() {
    //     AssessmentType assessmentType = new AssessmentType();
    //     Mockito.when(adao.createAssessmentType(assessmentType.getName(),assessmentType.getDefaultWeight())).thenThrow(SQLException.class);
    //     assessmentService.createAssessmentType(assessmentType);

    // }

    //----------------------------------------------------------------------

    @Test
    public void testGetNotesForTrainee()  {
        List<Note> notes =new ArrayList<>();
            Mockito.when(adao.getNotesForTrainee(1, "test")).thenReturn(notes);
            List<Note> newNotes = assessmentService.getNotesForTrainee(1, "test");
            Assert.assertSame(notes, newNotes);
    }

//       This method has no exception
//    @Test(expectedExceptions= SQLException.class)
//    public void testGetNotesForTraineeSQLException() throws Exception {
//        Mockito.when(adao.getNotesForTrainee(1,"test")).thenThrow(SQLException.class);
//        assessmentService.getNotesForTrainee(1,"test");
//
//    }

    //----------------------------------------------------------------------
//    @Test
//    public void testupdateTypeForAssessment() throws Exception {
//        Assessment assessment = new Assessment();
//        Mockito.when(adao.createAssessment(assessment)).thenReturn(assessment);
//        Assessment newAssessment =adao.createAssessment(assessment);
//        Assert.assertTrue(assessment==newAssessment);
//    }

    //----------------------------------------------------------------------

    @Test
    public void testInsertGrade() {
        Grade grade =new Grade();
        Mockito.when(adao.insertGrade(grade)).thenReturn(grade);
        Grade newGrade =assessmentService.insertGrade(grade);
        Assert.assertSame(grade, newGrade);
    }

    //----------------------------------------------------------------------

    @Test
    public void testGetBatchWeek() {
        List<Assessment> assessments = new ArrayList<>();
        Mockito.when(adao.getBatchWeek(1,"test")).thenReturn(assessments);
        List<Assessment>  newAssessments =assessmentService.getBatchWeek(1,"test");
        Assert.assertSame(newAssessments, assessments);
    }

    //----------------------------------------------------------------------

}




