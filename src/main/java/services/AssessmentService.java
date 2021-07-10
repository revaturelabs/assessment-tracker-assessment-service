package services;

import dao.AssessmentDAO;
import dao.AssessmentDAOImpl;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import models.Assessment;
import models.Grade;
import models.Note;
import models.AssessmentType;

import java.sql.SQLException;
import java.util.List;

public class AssessmentService {
    AssessmentDAO assessmentDAO = new AssessmentDAOImpl();

    public List<Assessment> getAssessments(){
        return assessmentDAO.getAssessments();
    }

    public List<Assessment> getAssessmentsByTraineeId(int traineeId){
        return assessmentDAO.getAssessmentsByTraineeId(traineeId);
    }

    public List<Grade> getGradesForWeek(int traineeId, String weekId){
        return assessmentDAO.getGradesForWeek(traineeId, weekId);

    }

    public Assessment createAssessment(Assessment assessment) throws InvalidValue {
        return assessmentDAO.createAssessment(assessment);
    }

    public boolean adjustWeight(int assessmentId, int weight) throws InvalidValue, ResourceNotFound {
        return assessmentDAO.adjustWeight(assessmentId, weight);
    }

    public AssessmentType createAssessmentType(AssessmentType assessmentType) {
        return assessmentDAO.createAssessmentType(assessmentType.getName(), assessmentType.getDefaultWeight());
    }

    public List<Note> getNotesForTrainee(int id, int weekId) {
        return assessmentDAO.getNotesForTrainee(id, weekId);
    }

    public boolean updateTypeForAssessment(int assessmentId, int typeId) throws SQLException, ResourceNotFound, InvalidValue {
        return assessmentDAO.assignAssessmentType(assessmentId, typeId);
    }

    public Grade insertGrade(Grade grade) {
        if(assessmentDAO.getGradeForAssociate(grade.getAssociateId(), grade.getAssessmentId()) == null){
            return assessmentDAO.insertGrade(grade);
        }
        else{return assessmentDAO.updateGrade(grade);}
    }


    public List<Assessment> getBatchWeek(int batchId, String weekId){
        return assessmentDAO.getBatchWeek(batchId, weekId);
    }
    public Grade getGradeForAssociate(int associateId, int assessmentId){
        return assessmentDAO.getGradeForAssociate(associateId, assessmentId);
    }
}
