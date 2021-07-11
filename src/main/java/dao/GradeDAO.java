package dao;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;

public interface GradeDAO {

    Grade createGrade(Grade grade) throws DuplicateResource;

    List<Grade> getGrades();

    Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound;

    Grade updateGrade(Grade grade) throws ResourceNotFound, ResourceUnchangable;

    void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable;
}
