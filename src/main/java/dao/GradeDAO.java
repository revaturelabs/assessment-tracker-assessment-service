package dao;

import java.util.List;

import dtos.WeekAvg;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Grade;

public interface GradeDAO {

    /**
     * Creates a new grade and adds it to the database
     *
     * @param grade the grade object being used to create a new one
     * @return the created grade object
     * @throws DuplicateResource if the grade for an existing assessment and associate already exists
     * @throws InvalidValue if foreign keys for the grade do not exist
     */
    Grade createGrade(Grade grade) throws DuplicateResource, InvalidValue;

    /**
     * Gets all grades in the database
     *
     * @return a list of all grades from the database
     */
    List<Grade> getGrades();

    /**
     * Gets the grade with an assessment id and associate id
     *
     * @param assessmentId the id of the assessment the grade belongs to
     * @param associateId the id of the associate the grade belongs to
     * @return the grade
     * @throws ResourceNotFound if the grade does not exist
     */
    Grade getGrade(int assessmentId, int associateId) throws ResourceNotFound;

    /**
     * Updates the score of a grade
     *
     * @param grade the grade whose score is being updated
     * @return the updated grade
     * @throws ResourceNotFound if the grade does not exist
     * @throws InvalidValue if the foreign keys do not exist
     * @throws DuplicateResource if a grade already exists with a certain id
     */
    Grade updateGrade(Grade grade) throws ResourceNotFound, InvalidValue, DuplicateResource;

    /**
     * Deletes a grade given a grade id
     *
     * @param id the id of the grade being deleted
     * @throws ResourceNotFound if the grade doesn't exist
     * @throws ResourceUnchangable if the grade has foreign key constraints
     */
    void deleteGrade(int id) throws ResourceNotFound, ResourceUnchangable;

    /**
     * Gets the grades for an associate in a given week
     *
     * @param associateId the id of the associate
     * @param weekId the id of the week
     * @return a list of all grades for an associate in a given week
     */
    List<Grade> getGradesForWeek(int associateId, int weekId);

    /**
     * Gets the average grade for an assessment
     *
     * @param assessmentId the assessment whose grades are being averaged
     * @return the average grade
     * @throws ResourceNotFound if the grade doesn't exist
     */
    double getAverageGrade(int assessmentId) throws ResourceNotFound;

    /**
     * Gets the grades for a single week in a given batch
     *
     * @param batchId the id of the batch
     * @param weekId the id of the week
     * @return a list of grades for a week in a batch
     */
    List<Grade> getGrades(int batchId, int weekId);

    /**
     * Gets all the averages for assessments in a given week for a batch
     *
     * @param batchId the id of the batch
     * @param weekId the id of the week
     * @return a list of week averages containing the average scores for assessments
     */
    List<WeekAvg> getAvgWeek(int batchId, int weekId);
}
