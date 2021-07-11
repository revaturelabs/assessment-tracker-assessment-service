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
        if(grade.getScore() < 0)
            throw new InvalidValue("Grade score cannot be negative");
        return this.gradeDAO.createGrade(grade);
    }

    @Override
    public List<Grade> getGrades() {
        return this.getGrades();
    }

    @Override
    public Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound {
        return this.getGrade(assessmentId, associateId);
    }

    @Override
    public Grade updateGrade(Grade grade) throws ResourceNotFound, ResourceUnchangable, InvalidValue{
        if(grade.getScore() < 0)
            throw new InvalidValue("Grade score cannot be negative");
        return this.updateGrade(grade);
    }

    @Override
    public void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable {
        this.deleteGrade(id);
    }
    
}
