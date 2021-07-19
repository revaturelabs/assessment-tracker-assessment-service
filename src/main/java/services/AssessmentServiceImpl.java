package services;

import java.sql.SQLException;
import java.util.List;

import dao.AssessmentDAO;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;

public class AssessmentServiceImpl implements AssessmentService{

    private AssessmentDAO assessmentDAO;

    public AssessmentServiceImpl(AssessmentDAO assessmentDAO){
        this.assessmentDAO = assessmentDAO;
    }

    @Override
    public Assessment createAssessment(Assessment assessment) throws InvalidValue {
        return this.assessmentDAO.createAssessment(assessment);
    }

    @Override
    public List<Assessment> getAssessments() {
        return this.assessmentDAO.getAssessments();
    }

    @Override
    public Assessment getAssessment(int id) throws ResourceNotFound, InvalidValue {
        return this.assessmentDAO.getAssessmentById(id);
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable, InvalidValue{
        return this.assessmentDAO.updateAssessment(assessment);
    }

    @Override
    public void deleteAssessment(int id) throws ResourceNotFound, ResourceUnchangable {
        assessmentDAO.deleteAssessment(id);
    }

    @Override
    public Assessment adjustWeight(int id, int weight) throws ResourceNotFound, ResourceUnchangable, InvalidValue {
       return assessmentDAO.adjustWeight(id, weight);
    }

    @Override
    public Assessment assignAssessmentType(int id, int typeId) throws ResourceNotFound, SQLException, InvalidValue {
        if (assessmentDAO.assignAssessmentType(id, typeId)) {
            return assessmentDAO.getAssessmentById(id);
        } else {
            throw new ResourceNotFound("AssessmentType of id " + typeId + " doesn't exist");
        }
    }

    @Override
    public List<Assessment> getBatchWeek(int batchId, String weekId) {
        return assessmentDAO.getBatchWeek(batchId, weekId);
    }

    @Override
    public List<Assessment> getAssessmentsByAssociateId(int associateId) {
        return this.assessmentDAO.getAssessmentsByAssociateId(associateId);
    }

}

