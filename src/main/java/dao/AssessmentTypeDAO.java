package dao;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.AssessmentType;

public interface AssessmentTypeDAO {

    /**
     * Used to add a created assessment type to the database
     * @param assessmentType An assessment type to be added to the database
     * @return The added assessment type with updated id
     * @throws DuplicateResource There is an assessment type that matches the one passed in the database already
     * @throws InvalidValue A value in the created assessment type is invalid
     */
    AssessmentType createAssessmentType(AssessmentType assessmentType) throws DuplicateResource, InvalidValue;

    /**
     * Returns a list of all the assessment types
     * @return The list of all assessment types in the database
     */
    List<AssessmentType> getAssessmentTypes();

    /**
     * Retrieve a specific assessment type from the database using its id
     * @param id The id of the assessment type to be retrieved
     * @return The assessment type with the passed in id
     * @throws ResourceNotFound An assessment type with the given id does not exist
     */
    AssessmentType getAssessmentType(int id) throws ResourceNotFound;

    /**
     * Replaces an assessment type in the database with the given id with the given assessment type
     * @param assessmentType The assessment type that is to be updated in the database
     * @return The updated assessment type
     * @throws ResourceNotFound An assessment type with the id of the passed in assessment type does not exist
     * @throws DuplicateResource
     * @throws InvalidValue  A value in the updated assessment type is invalid
     */
    AssessmentType updateAssessmentType(AssessmentType assessmentType) throws ResourceNotFound, DuplicateResource, InvalidValue;

    /**
     * Removes an assessment type from the database with the given id
     * @param id The id of the assessment type to be deleted
     * @throws ResourceNotFound An assessment type with the given id does not exist
     * @throws ResourceUnchangable The specified assessment type has foreign key constraints preventing it from being deleted
     */
    void deleteAssessmentType(int id) throws ResourceNotFound, ResourceUnchangable;
}
