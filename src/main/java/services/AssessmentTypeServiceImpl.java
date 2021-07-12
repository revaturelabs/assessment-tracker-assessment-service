package services;

import java.util.List;

import dao.AssessmentTypeDAO;
import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

public class AssessmentTypeServiceImpl implements AssessmentTypeService{

    private AssessmentTypeDAO assessmentTypeDAO;

    public AssessmentTypeServiceImpl(AssessmentTypeDAO assessmentTypeDAO){
        this.assessmentTypeDAO = assessmentTypeDAO;
    }

    @Override
    public AssessmentType createAssessmentType(AssessmentType assessmentType) throws DuplicateResource {
        return this.assessmentTypeDAO.createAssessmentType(assessmentType);
    }

    @Override
    public List<AssessmentType> getAssessmentTypes() {
        return this.assessmentTypeDAO.getAssessmentTypes();
    }

    @Override
    public AssessmentType getAssessmentType(int id) throws ResourceNotFound {
        return this.assessmentTypeDAO.getAssessmentType(id);
    }

    @Override
    public AssessmentType updateAssessmentType(AssessmentType assessmentType) throws ResourceNotFound {
        return this.assessmentTypeDAO.updateAssessmentType(assessmentType);
    }

    @Override
    public void deleteAssessmentType(int id) throws ResourceNotFound, ResourceUnchangable {
        this.assessmentTypeDAO.deleteAssessmentType(id);
    }
    
}
