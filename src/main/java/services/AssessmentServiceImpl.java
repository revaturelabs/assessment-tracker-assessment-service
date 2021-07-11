package services;

import java.util.ArrayList;
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
        if(assessment.getAssessmentWeight() < 0)
            throw new InvalidValue("Assessment weight cannot be negative");
        return this.assessmentDAO.createAssessment(assessment);
    }

    @Override
    public List<Assessment> getAssessments() {
        return this.assessmentDAO.getAssessments();
    }

    @Override
    public Assessment getAssessment(int id) throws ResourceNotFound {
        return this.assessmentDAO.getAssessment(id);
    }

    @Override
    public Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable, InvalidValue{
        if(assessment.getAssessmentWeight() < 0)
            throw new InvalidValue("Assessment weight cannot be negative");
        return this.assessmentDAO.updateAssessment(assessment);
    }

    @Override
    public void deleteAssessment(int id) throws ResourceNotFound, ResourceUnchangable {
        this.deleteAssessment(id);
    }

    @Override
    public Assessment adjustWeight(int id, int weight) throws ResourceNotFound, ResourceUnchangable, InvalidValue {
        Assessment assessment = this.getAssessment(id);
        assessment.setAssessmentWeight(weight);
        return this.updateAssessment(assessment);
    }

    @Override
    public Assessment assignAssessmentType(int id, int typeId) throws ResourceNotFound, ResourceUnchangable, InvalidValue {
        Assessment assessment = this.getAssessment(id);
        assessment.setTypeId(typeId);
        return this.updateAssessment(assessment);
    }

    @Override
    public List<Assessment> getBatchWeek(int batchId, int weekId) {
        List<Assessment> assessmentsInGroup = new ArrayList<Assessment>();
        List<Assessment> assessments = this.getAssessments();
        for(int i = 0; i < assessments.size(); i++){
            Assessment assessment = assessments.get(i);
            if(assessment.getBatchId() == batchId && assessment.getWeekId() == weekId)
                assessmentsInGroup.add(assessment);
        }
        return assessmentsInGroup;
    }

    @Override
    public List<Assessment> getAssessmentsByAssociateId(int associateId) {
        return this.assessmentDAO.getAssessmentsByAssociateId(associateId);
    }

}
