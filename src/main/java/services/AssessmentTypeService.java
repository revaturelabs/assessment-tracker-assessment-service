package services;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

public interface AssessmentTypeService {
    
    AssessmentType createAssessmentType(AssessmentType assessmentType) throws DuplicateResource, InvalidValue;

    List<AssessmentType> getAssessmentTypes();

    AssessmentType getAssessmentType(int id) throws ResourceNotFound;

    AssessmentType updateAssessmentType(AssessmentType assessmentType) throws ResourceNotFound, DuplicateResource, InvalidValue;

    void deleteAssessmentType(int id) throws ResourceNotFound, ResourceUnchangable;
}
