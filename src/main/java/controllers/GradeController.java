package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.annotations.*;
import models.Grade;
import services.GradeService;

public class GradeController {
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private GradeService gradeService;
    private static final Gson gson = new Gson();

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @OpenApi(
            path = "/grades",
            method = HttpMethod.POST,
            summary = "Create new grade",
            operationId = "createGrade",
            tags = {"Grade"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Grade.class)}),
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = Grade.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler createGrade = context -> {
        try {
            Grade grade = gson.fromJson(context.body(), Grade.class);
            if (grade == null)
                throw new JsonSyntaxException("");
            grade = this.gradeService.createGrade(grade);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(grade));
            context.status(201);
        } catch (DuplicateResource e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(409);
        } catch (InvalidValue e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(422);
        } catch (JsonSyntaxException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("JSON was not able to be parsed");
            context.status(400);
        }
    };

    @OpenApi(
            path = "/assessments/:assessmentId/associates/:associateId/grades",
            method = HttpMethod.GET,
            summary = "Get an given associate's grade for a given assessment",
            operationId = "getGradeForAssociateAssessment",
            pathParams = {
                    @OpenApiParam(name = "assessmentId", type = Integer.class, description = "The assessment ID"),
                    @OpenApiParam(name = "associateId", type = Integer.class, description = "The associate ID")
            },
            tags = {"Grade"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Grade.class)}),
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Grade.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getGradeForAssociateAssessment = context -> {
        try {
            int associateId = Integer.parseInt(context.pathParam("associateId"));
            int assessmentId = Integer.parseInt(context.pathParam("assessmentId"));
            Grade grade = this.gradeService.getGrade(assessmentId, associateId);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(grade));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Assessment ID or Associate ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
            path = "/grades",
            method = HttpMethod.PUT,
            summary = "Update grade",
            operationId = "updateGrade",
            tags = {"Grade"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Grade.class)}),
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = Grade.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler updateGrade = context -> {
        try {
            Grade grade = gson.fromJson(context.body(), Grade.class);
            if (grade == null)
                throw new JsonSyntaxException("");
            grade = this.gradeService.updateGrade(grade);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(grade));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (InvalidValue e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(422);
        } catch (DuplicateResource e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(409);
        } catch (JsonSyntaxException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("JSON was not able to be parsed");
            context.status(400);
        }
    };

    @OpenApi(
            summary = "Delete grade by gradeId",
            operationId = "deleteGrade",
            path = "/grades/:gradeId",
            method = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "gradeId", type = Integer.class, description = "The grade ID")},
            tags = {"Grade"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler deleteGrade = context -> {
        try {
            int id = Integer.parseInt(context.pathParam("gradeId"));
            this.gradeService.deleteGrade(id);
            context.status(204);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (ResourceUnchangable e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(409);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
            path = "/associates/:associateId/week/:weekId/grades",
            method = HttpMethod.GET,
            summary = "Get a given associate's grades for a given week",
            operationId = "getGradesForWeek",
            pathParams = {
                    @OpenApiParam(name = "associateId", type = Integer.class, description = "The associate ID"),
                    @OpenApiParam(name = "weekId", type = Integer.class, description = "The week ID")
            },
            tags = {"Grade"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Grade.class)}),
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Grade[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getGradesForWeek = context -> {
        try {
            int associateId = Integer.parseInt(context.pathParam("associateId"));
            int weekId = Integer.parseInt(context.pathParam("weekId"));
            List<Grade> grades = this.gradeService.getGradesForWeek(associateId, weekId);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(grades));
            context.status(200);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Associate ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
            path = "/assessments/:assessmentId/grades/average",
            method = HttpMethod.GET,
            summary = "Get the average of all grades for a given assessment",
            operationId = "getAverageGrade",
            pathParams = {@OpenApiParam(name = "assessmentId", type = Integer.class, description = "The assessment ID")},
            tags = {"Grade"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Grade.class)}),
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Double.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getAverageGrade = context -> {
        try {
            int assessmentId = Integer.parseInt(context.queryParam("assessmentId"));
            double averageGrade = this.gradeService.getAverageGrade(assessmentId);
            context.result(gson.toJson(averageGrade));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.result(e.getMessage());
            context.status(404);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };
}


