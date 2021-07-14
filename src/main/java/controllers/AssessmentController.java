package controllers;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import io.javalin.http.Handler;
import com.google.gson.Gson;
import models.Assessment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import services.AssessmentService;

import java.sql.SQLException;
import java.util.List;

public class AssessmentController {
    private static final Logger aclogger = LogManager.getLogger(AssessmentController.class);
    private static final String CONTENTTYPE = "application/json";
    private static final String ASSESSMENTID = "assessmentId";

    //BUG - Extract these later
    private AssessmentService as;
    private final Gson gson = new Gson();

    public AssessmentController(AssessmentService as) {
        this.as = as;
    }

    public Handler getAssessments = context -> {
        try {
            aclogger.info("Attempting to get all assessments");
            List<Assessment> assessment = as.getAssessments();
            context.contentType(CONTENTTYPE);
            context.result(gson.toJson(assessment));
            context.status(200);
        } catch (RuntimeException e) {
            aclogger.info(e);
            context.result("No assessments could be found");
            context.status(404);
        }
    };

    public Handler createAssessment = context -> {
        try {
            aclogger.info("Attempting to create an assessment");
            Assessment assessment = gson.fromJson(context.body(), Assessment.class);
            Assessment updatedAssessment = as.createAssessment(assessment);
            context.contentType(CONTENTTYPE);
            aclogger.info("Attempting to return updated assessment");
            context.result(gson.toJson(updatedAssessment));
            context.status(201);
        } catch (InvalidValue | RuntimeException e) {
            aclogger.info(e);
            context.status(400);
            context.result(e.getMessage());
        }
    };

    public Handler getAssessmentsByTraineeId = context -> {
        try {
            int traineeId = Integer.parseInt(context.pathParam("traineeId"));
            aclogger.info("Attempting to get all assessments for trainee with id " + traineeId);
            List<Assessment> assessments = as.getAssessmentsByAssociateId(traineeId);
            if(assessments.size() == 0){
                throw new RuntimeException("The assessment for trainee " + traineeId + " could not be found");
            }
            context.contentType(CONTENTTYPE);
            context.result(gson.toJson(assessments));
            context.status(200);
        } catch (RuntimeException e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(404);
        }
    };

    public Handler getBatchWeek = context -> {
        try {
            int batchId = Integer.parseInt(context.pathParam("batchId"));
            String week = context.queryParam("week");
            aclogger.info("Attempting to get assessments for batch " + batchId + " for week " + week);
            List<Assessment> assessments = as.getBatchWeek(batchId, week);
            if(assessments.size() == 0){
                throw new RuntimeException("There are no assessments for the combination of batch " + batchId + " and week " + week);
            }
            context.contentType(CONTENTTYPE);
            context.result(gson.toJson(assessments));
            context.status(200);
        } catch (RuntimeException e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(404);
        }
    };

    public Handler adjustWeight = context -> {
        try {
            int weight = Integer.parseInt(context.queryParam("weight"));
            int assessmentId = Integer.parseInt(context.pathParam(ASSESSMENTID));
            aclogger.info("Attempting to update the weight on an assessment");
            Assessment a = as.adjustWeight(assessmentId, weight);
            context.contentType(CONTENTTYPE);
            aclogger.info("Attempting to return updatedWeight");
            context.result(gson.toJson(a));
            context.status(205);
        } catch (ResourceNotFound e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(404);
        } catch (InvalidValue | RuntimeException e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(400);
        }
    };

    public Handler assignAssessmentType = context -> {
        int typeId = Integer.parseInt(context.pathParam("typeId"));
        int assessmentId = Integer.parseInt(context.pathParam(ASSESSMENTID));
        try {
            aclogger.info("Attempting to update type for assessment");
            Assessment a = as.assignAssessmentType(assessmentId,typeId);
            context.contentType(CONTENTTYPE);
            aclogger.info("Attempting to return updated type for assessment");
            context.result(gson.toJson(a));
            context.status(205);
        } catch (ResourceNotFound | SQLException e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(404);
        } catch (RuntimeException | InvalidValue e) {
            aclogger.info(e);
            context.result(e.getMessage());
            context.status(400);
        }
    };

    // public Handler getGradeForAssociate = context -> {
    //     int associateId = Integer.parseInt(context.queryParam("associateId"));
    //     int assessmentId = Integer.parseInt(context.pathParam(ASSESSMENTID));
    //     try{
    //         aclogger.info("Checking if associate with id " + associateId + " exists");
    //         List<Assessment> assessments = as.getAssessmentsByAssociateId(associateId);
    //         if(assessments.size() == 0){
    //             throw new RuntimeException("The associate with id " + associateId + " could not be found");
    //         }
    //         Grade grade = gs.getGrade(assessmentId, associateId);
    //         if(grade == null){
    //             throw new RuntimeException("The assessment with id " + assessmentId + " could not be found");
    //         }
    //         context.contentType(CONTENTTYPE);
    //         context.result(gson.toJson(grade));
    //         context.status(200);
    //     }catch (ResourceNotFound | RuntimeException e){
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(404);
    //     }
    // };

    // public Handler getGradesForWeek = context -> {
    //     try {
    //         int traineeId = Integer.parseInt(context.queryParam("traineeId"));
    //         int weekId = Integer.parseInt(context.queryParam("week"));
    //         aclogger.info("Attempting to get grades for trainee " + traineeId  + "for week " + weekId);
    //         aclogger.info("Checking if trainee with id " + traineeId + " exists");
    //         List<Assessment> assessments = as.getAssessmentsByAssociateId(traineeId);
    //         if(assessments.size() == 0){
    //             throw new RuntimeException("The trainee with id " + traineeId + " could not be found");
    //         }
    //         aclogger.info("Attempting to get grades for trainee with id " + traineeId + " for week " + weekId);
    //         List<Grade> grades = gs.getGradesForWeek(traineeId, weekId);
    //         if(grades.size() == 0){
    //             throw new RuntimeException("The week with id " + weekId + " could not be found");
    //         }
    //         context.contentType(CONTENTTYPE);
    //         context.result(gson.toJson(grades));
    //         context.status(200);
    //     } catch (RuntimeException e) {
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(404);
    //     }
    // };

    // public Handler insertGrade = context -> {
    //     try {
    //         aclogger.info("Attempting to update the grade on an assessment");
    //         Grade grade = gson.fromJson(context.body(), Grade.class);
    //         Grade insertedGrade = gs.insertGrade(grade);
    //         context.contentType(CONTENTTYPE);
    //         aclogger.info("Attempting to return inserted  grade");
    //         context.result(gson.toJson(insertedGrade));
    //         context.status(201);
    //     } catch (ResourceNotFound e) {
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(404);
    //     } catch (ResourceUnchangable e) {
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(403);
    //     } catch (DuplicateResource | InvalidValue | RuntimeException e) {
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(400);
    //     }
    // };

//    public Handler getNotesForTrainee = context -> {
//        try {
//            int id = Integer.parseInt(context.pathParam("id"));
//            int weekId = Integer.parseInt(context.pathParam(WEEKID));
//            aclogger.info("Attempting to get notes for trainee " + id + " for week " + weekId);
//            aclogger.info("Checking if trainee with id " + id + " exists");
//            List<Assessment> assessments = as.getAssessmentsByAssociateId(id);
//            if(assessments.size() == 0){
//                throw new RuntimeException("The trainee with id " + id + " could not be found");
//            }
//            List<Note> notes = as.getNotesForTrainee(id, weekId);
//            if(notes.size() == 0){
//                throw new RuntimeException("The week with id " + weekId + " could not be found");
//            }
//            context.contentType(CONTENTTYPE);
//            context.result(gson.toJson(notes));
//            context.status(200);
//        } catch (RuntimeException e) {
//            aclogger.info(e);
//            context.result(e.getMessage());
//            context.status(404);
//        }
//    };

    // public Handler createAssessmentType = context -> {
    //     try {
    //         aclogger.info("Attempting to create a type for assessments");
    //         AssessmentType assessmentType = gson.fromJson(context.body(), AssessmentType.class);
    //         AssessmentType updatedAssessmentType = ats.createAssessmentType(assessmentType);
    //         context.contentType(CONTENTTYPE);
    //         aclogger.info("Attempting to return updated type");
    //         context.result(gson.toJson(updatedAssessmentType));
    //         context.status(201);
    //     } catch (DuplicateResource | RuntimeException e) {
    //         aclogger.info(e);
    //         context.result(e.getMessage());
    //         context.status(400);
    //     }
    // };

    // public Handler getAverageGrade = context -> {
    //     int assessmentId = Integer.parseInt(context.queryParam(ASSESSMENTID));

    //     try {
    //         aclogger.info("Attempting to get average grade per assessment with id " + assessmentId);

    //         double averageGrade = gs.getAverageGrade(assessmentId);

    //         aclogger.info("Attempting to return average grade");
    //         context.result(gson.toJson(averageGrade));
    //         context.status(201);
    //     } catch (ResourceNotFound e) {
    //         context.result(e.getMessage());
    //         context.status(404);
    //     }
    // };




}
