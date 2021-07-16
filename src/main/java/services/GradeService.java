package services;

import java.util.List;

import dtos.WeekAvg;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;

public interface GradeService {
    
    Grade createGrade(Grade grade) throws DuplicateResource, InvalidValue;

    List<Grade> getGrades();

    Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound;

    Grade updateGrade(Grade grade) throws ResourceNotFound, InvalidValue, DuplicateResource;

    Grade insertGrade(Grade grade) throws ResourceNotFound, InvalidValue, DuplicateResource;

    void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable;

    List<Grade> getGradesForWeek(int associateId, int weekId);

    double getAverageGrade(int assessmentId) throws ResourceNotFound;

    List<Grade> getGrades(int batchId, int weekId);

    List<WeekAvg> getWeekAvg(int batchId, int weekId);
}
