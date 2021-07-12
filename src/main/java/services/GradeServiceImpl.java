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
    public List<Grade> getGrades() throws InvalidValue {
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

    @Override
    public Grade insertGrade(Grade grade) throws ResourceUnchangable, InvalidValue, DuplicateResource {
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
    public List<Grade> getGradesForWeek(int associateId, String weekId) throws InvalidValue{
        return this.gradeDAO.getGradesForWeek(associateId, weekId);
    }

    @Override
    public double getAverageGrade(int assessmentId) throws ResourceNotFound {
        return gradeDAO.getAverageGrade(assessmentId);
    }

}
