package controllers;

import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import io.javalin.http.Handler;
import com.google.gson.Gson;
import io.javalin.plugin.openapi.annotations.*;
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

    @OpenApi(
            path = "/assessments",
            method = HttpMethod.GET,
            summary = "Get all assessments",
            operationId = "getAllAssessments",
            tags = {"Assessment"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Assessment[].class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)})
            }
    )
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

    @OpenApi(
            path = "/assessments",
            method = HttpMethod.POST,
            summary = "Create an assessment",
            operationId = "createAssessment",
            tags = {"Assessment"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Assessment.class)}),
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = Assessment.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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

    @OpenApi(
            path = "/associates/:associateId/assessments",
            method = HttpMethod.GET,
            summary = "Get assessments by trainee/associate id",
            operationId = "getAssessmentsByTraineeId",
            tags = {"Assessment"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Assessment[].class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getAssessmentsByTraineeId = context -> {
        try {
            int traineeId = Integer.parseInt(context.pathParam("associateId"));
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

    @OpenApi(
            path = "/batches/:batchId/weeks/:weekId/assessments",
            method = HttpMethod.GET,
            summary = "Get all assessments in a batch for a certain week",
            operationId = "getBatchByWeek",
            tags = {"Assessment"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Assessment.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getBatchWeek = context -> {
        try {
            int batchId = Integer.parseInt(context.pathParam("batchId"));
            String week = Integer.toString(Integer.parseInt(context.pathParam("weekId")));
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

    @OpenApi(
            path = "/weight/:weight/assessments/:assessmentId",
            method = HttpMethod.PATCH,
            summary = "Update the weight of an assessment",
            operationId = "adjustWeight",
            pathParams = {
                    @OpenApiParam(name = "weight", type = Integer.class, description = "The new weight"),
                    @OpenApiParam(name = "assessmentId", type = Integer.class, description = "Assessment id"),
            },
            tags = {"Assessment"},
            responses = {
                    @OpenApiResponse(status = "205", content = {@OpenApiContent(from = Assessment.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler adjustWeight = context -> {
        try {
            int weight = Integer.parseInt(context.pathParam("weight"));
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

    @OpenApi(
            path = "/types/:typeId/assessments/:assessmentId",
            method = HttpMethod.PATCH,
            summary = "Change the type of an assessment",
            operationId = "assignAssessmentType",
            pathParams = {
                    @OpenApiParam(name = "typeId", type = Integer.class, description = "The new type"),
                    @OpenApiParam(name = "assessmentId", type = Integer.class, description = "Assessment id"),
            },
            tags = {"Assessment"},
            responses = {
                    @OpenApiResponse(status = "205", content = {@OpenApiContent(from = Assessment.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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
}
