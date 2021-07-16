package services;

import java.util.List;

import dao.GradeDAO;
import dtos.WeekAvg;
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
    public List<Grade> getGrades(){
        return this.gradeDAO.getGrades();
    }

    @Override
    public Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound {
        return this.gradeDAO.getGrade(assessmentId, associateId);
    }

    @Override
    public Grade updateGrade(Grade grade) throws ResourceNotFound, InvalidValue, DuplicateResource {
        if(grade.getScore() < 0)
            throw new InvalidValue("Grade score cannot be negative");
        return this.gradeDAO.updateGrade(grade);
    }

    @Override
    public Grade insertGrade(Grade grade) throws InvalidValue, DuplicateResource {
        try {
            getGrade(grade.getAssessmentId(), grade.getAssociateId());
            return updateGrade(grade);
        } catch (ResourceNotFound e) {
            return createGrade(grade);
        }
    }

    @Override
    public void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable {
        this.gradeDAO.deleteGrade(id);
    }

    @Override
    public List<Grade> getGradesForWeek(int associateId, int weekId){
        return this.gradeDAO.getGradesForWeek(associateId, weekId);
    }

    @Override
    public double getAverageGrade(int assessmentId) throws ResourceNotFound {
        return this.gradeDAO.getAverageGrade(assessmentId);
    }

    @Override
    public List<Grade> getGrades(int batchId, int weekId) {
        return this.gradeDAO.getGrades(batchId, weekId);
    }

    @Override
    public List<WeekAvg> getWeekAvg(int batchId, int weekId) { return this.gradeDAO.getAvgWeek(batchId, weekId);}
}
