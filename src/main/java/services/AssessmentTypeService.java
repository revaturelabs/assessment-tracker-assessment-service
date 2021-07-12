package services;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

public interface AssessmentTypeService {
    
    AssessmentType createAssessmentType(AssessmentType assessmentType) throws DuplicateResource;

    List<AssessmentType> getAssessmentTypes();

    AssessmentType getAssessmentType(int id) throws ResourceNotFound;

    AssessmentType updateAssessmentType(AssessmentType assessmentType) throws ResourceNotFound;

    void deleteAssessmentType(int id) throws ResourceNotFound, ResourceUnchangable;
}
