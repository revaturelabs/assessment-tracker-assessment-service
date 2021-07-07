package dao;

import models.Assessment;
import models.Grade;
import models.Note;
import models.AssessmentType;
import java.util.List;

public interface AssessmentDAO {
     List<Assessment> getAssessments();

     List<Assessment> getAssessmentsByTraineeId(int traineeId);

     List<Grade> getGradesForWeek(int traineeId, String weekId);

     List<Assessment> getBatchWeek(int batchId, String weekId);

     Assessment createAssessment(Assessment a);

     boolean adjustWeight(int assessmentId, int weight);

     AssessmentType createAssessmentType(String name, int defaultWeight);

     boolean assignAssessmentType(int assessmentId, int typeId);

     List<Note> getNotesForTrainee(int id, String weekId);

     Grade insertGrade(Grade grade);
    
     Grade getGradeForAssociate(int associateId, int assessmentId);

     Grade updateGrade(Grade grade);

}
