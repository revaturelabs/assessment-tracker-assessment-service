package dao;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import models.Note;

import java.sql.SQLException;
import java.util.List;

public interface AssessmentDAO {

     Assessment createAssessment(Assessment a) throws InvalidValue;

     Assessment getAssessmentById(int assessmentId) throws ResourceNotFound, InvalidValue;

     List<Assessment> getAssessments();

     Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable, InvalidValue;

     Assessment adjustWeight(int assessmentId, int weight) throws InvalidValue, ResourceNotFound;

     boolean assignAssessmentType(int assessmentId, int typeId) throws SQLException, ResourceNotFound, InvalidValue;

     boolean deleteAssessment(int assessmentId) throws ResourceNotFound, ResourceUnchangable;

     List<Assessment> getAssessmentsByAssociateId(int traineeId);

     List<Assessment> getBatchWeek(int batchId, String weekId);

     //BUG - To extract
     List<Note> getNotesForTrainee(int id, int weekId);
}
