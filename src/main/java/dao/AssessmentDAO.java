package dao;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import models.Assessment;
import models.Grade;
import models.Note;
import models.AssessmentType;

import java.sql.SQLException;
import java.util.List;

public interface AssessmentDAO {

     Assessment createAssessment(Assessment a) throws InvalidValue;

     Assessment getAssessmentById(int assessmentId) throws ResourceNotFound, InvalidValue;

     List<Assessment> getAssessments();

     boolean adjustWeight(int assessmentId, int weight) throws InvalidValue, ResourceNotFound;

     boolean assignAssessmentType(int assessmentId, int typeId) throws SQLException, ResourceNotFound, InvalidValue;

     boolean deleteAssessment(int assessmentId) throws ResourceNotFound;

     List<Assessment> getAssessmentsByTraineeId(int traineeId);

     List<Assessment> getBatchWeek(int batchId, String weekId);

     //BUG - To extract
     List<Grade> getGradesForWeek(int traineeId, String weekId);

     AssessmentType createAssessmentType(String name, int defaultWeight);

     List<Note> getNotesForTrainee(int id, int weekId);

     Grade insertGrade(Grade grade);
    
     Grade getGradeForAssociate(int associateId, int assessmentId);

     Grade updateGrade(Grade grade);

}
