package services;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;
import java.util.List;

public interface AssessmentService {

    Assessment createAssessment(Assessment assessment) throws InvalidValue;

    List<Assessment> getAssessments();

    Assessment getAssessment(int id) throws ResourceNotFound, InvalidValue;

    Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable, InvalidValue;

    void deleteAssessment(int id) throws ResourceNotFound, ResourceUnchangable;
    
    Assessment adjustWeight(int id, int weight) throws ResourceNotFound, ResourceUnchangable, InvalidValue;

    Assessment assignAssessmentType(int id, int typeId) throws ResourceNotFound, ResourceUnchangable, InvalidValue;

    List<Assessment> getBatchWeek(int batchId, String weekId);

    List<Assessment> getAssessmentsByAssociateId(int associateId);
}
