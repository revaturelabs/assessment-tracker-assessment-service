package services;

import java.util.List;

import dao.GradeDAO;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;

public class GradeServiceImpl implements GradeService {

    private GradeDAO gradeDAO;

    public GradeServiceImpl(GradeDAO gradeDAO){
        this.gradeDAO = gradeDAO;
    }

    @Override
    public Grade createGrade(Grade grade) throws DuplicateResource, InvalidValue{
        return this.gradeDAO.createGrade(grade);
    }

    @Override
    public List<Grade> getGrades() {
        return this.gradeDAO.getGrades();
    }

    @Override
    public Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound {
        return this.gradeDAO.getGrade(assessmentId, associateId);
    }

    @Override
    public Grade updateGrade(Grade grade) throws ResourceNotFound, ResourceUnchangable, InvalidValue {
        return this.gradeDAO.updateGrade(grade);
    }

    public Grade insertGrade(Grade grade) throws ResourceNotFound, ResourceUnchangable, InvalidValue, DuplicateResource {
        if(getGrade(grade.getAssessmentId(), grade.getAssociateId()) != null)
            return updateGrade(grade);
        else
            return createGrade(grade);
    }

    @Override
    public void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable {
        this.gradeDAO.deleteGrade(id);
    }
    
}
