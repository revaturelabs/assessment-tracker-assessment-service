package dao;

import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import java.util.List;

public interface AssessmentDAO {

     Assessment createAssessment(Assessment assessment);

     List<Assessment> getAssessments();

     Assessment getAssessment(int id) throws ResourceNotFound;

     Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable;

     void deleteAssessment(int id) throws ResourceNotFound, ResourceUnchangable;

     List<Assessment> getAssessmentsByAssociateId(int associateId);

     // List<Note> getNotesForTrainee(int id, int weekId);
}
