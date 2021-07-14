package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import io.javalin.http.Handler;
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

    public Handler deleteGrade = context -> {
        try{
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

    public Handler getGradesForWeek = context -> {
        try {
            int associateId = Integer.parseInt(context.pathParam("associateId"));
            int weekId = Integer.parseInt(context.pathParam("week"));
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


