package dao;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Assessment;

import java.sql.SQLException;
import java.util.List;

public interface AssessmentDAO {

     /**
      * Creates an assessment and adds it to the database
      *
      * @param a the assessment being passed in as a template
      * @return the created assessment
      * @throws InvalidValue if the foreign keys for the created assessment don't exist
      */
     Assessment createAssessment(Assessment a) throws InvalidValue;

     /**
      * Gets an assessment by id
      *
      * @param assessmentId the id of the assessment being retrieved
      * @return the assessment
      * @throws ResourceNotFound if the id doesn't exist
      * @throws InvalidValue if the existing information for the assessment is not valid
      */
     Assessment getAssessmentById(int assessmentId) throws ResourceNotFound, InvalidValue;

     /**
      * Gets all assessments from the database
      *
      * @return a list of assessments
      */
     List<Assessment> getAssessments();

     /**
      * Updates an assessment
      *
      * @param assessment the assessment being used to update a given assessment
      * @return the updated assessment
      * @throws ResourceNotFound if the assessment doesn't exist
      * @throws ResourceUnchangable if the updates tries to change the foreign key values
      * @throws InvalidValue if no assessment was passed as a parameter (empty body)
      */
     Assessment updateAssessment(Assessment assessment) throws ResourceNotFound, ResourceUnchangable, InvalidValue;

     /**
      * Changes the weight of an assessment
      *
      * @param assessmentId the id of the assessment
      * @param weight the new weight
      * @return the updated assessment
      * @throws InvalidValue if the weight given is not between 0 and 100
      * @throws ResourceNotFound if the assessment doesn't exist
      */
     Assessment adjustWeight(int assessmentId, int weight) throws InvalidValue, ResourceNotFound;

     /**
      * Changes the type of an assessment
      *
      * @param assessmentId the id of the assessment
      * @param typeId the new type of the assessment
      * @return true if successfully updated
      * @throws SQLException if the type id doesn't exist
      * @throws ResourceNotFound if the assessment id doesn't exist
      * @throws InvalidValue if the existing information for the assessment is invalid
      */
     boolean assignAssessmentType(int assessmentId, int typeId) throws SQLException, ResourceNotFound, InvalidValue;

     /**
      * Deletes an assessment by id
      *
      * @param assessmentId the id of the assessment
      * @return true if successfully deleted
      * @throws ResourceNotFound if the assessment doesn't exist
      * @throws ResourceUnchangable if the assessment has foreign key constraints
      */
     boolean deleteAssessment(int assessmentId) throws ResourceNotFound, ResourceUnchangable;

     /**
      * Gets all assessments a specific trainee (associate) has taken
      *
      * @param traineeId the id of the trainee
      * @return a list of assessments belonging to an associate
      */
     List<Assessment> getAssessmentsByAssociateId(int traineeId);

     /**
      * Gets all assessments in a week for a given batch
      *
      * @param batchId the id of the batch
      * @param weekId the id of the week
      * @return a list of assessments for a batch in a given week
      */
     List<Assessment> getBatchWeek(int batchId, String weekId);

}
