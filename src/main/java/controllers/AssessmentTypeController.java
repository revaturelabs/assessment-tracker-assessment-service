package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import io.javalin.http.Handler;
import models.AssessmentType;
import services.AssessmentTypeService;

public class AssessmentTypeController {
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private AssessmentTypeService assessmentTypeService;
    private static final Gson gson = new Gson();

    public AssessmentTypeController(AssessmentTypeService assessmentTypeService) {
        this.assessmentTypeService = assessmentTypeService;
    }

    public Handler createAssessmentType = context -> {
        try {
            AssessmentType assessmentType = gson.fromJson(context.body(), AssessmentType.class);
            if (assessmentType == null)
                throw new JsonSyntaxException("");
            assessmentType = this.assessmentTypeService.createAssessmentType(assessmentType);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(assessmentType));
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

    public Handler getAssessmentTypes = context -> {
        List<AssessmentType> assessmentTypes = this.assessmentTypeService.getAssessmentTypes();
        context.contentType(CONTENT_TYPE_JSON);
        context.result(gson.toJson(assessmentTypes));
        context.status(200);
    };

    public Handler getAssessmentTypeById = context -> {
        try {
            int id = Integer.parseInt(context.pathParam("typeId"));
            AssessmentType assessmentType = this.assessmentTypeService.getAssessmentType(id);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(assessmentType));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    public Handler updateAssessmentType = context -> {
        try {
            AssessmentType assessmentType = gson.fromJson(context.body(), AssessmentType.class);
            if (assessmentType == null)
                throw new JsonSyntaxException("");
            assessmentType = this.assessmentTypeService.updateAssessmentType(assessmentType);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(assessmentType));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (DuplicateResource e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(409);
        } catch (InvalidValue e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(422);
        } catch (JsonSyntaxException e) {
            context.result("JSON was not able to be parsed");
            context.status(400);
        }
    };

    public Handler deleteAssessmentType = context -> {
        try {
            int id = Integer.parseInt(context.pathParam("typeId"));
            this.assessmentTypeService.deleteAssessmentType(id);
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
}
