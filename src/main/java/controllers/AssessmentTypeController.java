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

    @OpenApi(
            path = "/types",            // only necessary to include when using static method references
            method = HttpMethod.POST,    // only necessary to include when using static method references
            summary = "Create an Assessment Type",
            operationId = "createAssessmentType",
            tags = {"assessmentType"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AssessmentType.class)}),
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = AssessmentType.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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

    @OpenApi(
            path = "/types",            // only necessary to include when using static method references
            method = HttpMethod.GET,    // only necessary to include when using static method references
            summary = "Get all Assessment Types",
            operationId = "getAssessmentTypes",
            tags = {"assessmentType"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AssessmentType[].class)})
            }
    )
    public Handler getAssessmentTypes = context -> {
        List<AssessmentType> assessmentTypes = this.assessmentTypeService.getAssessmentTypes();
        context.contentType(CONTENT_TYPE_JSON);
        context.result(gson.toJson(assessmentTypes));
        context.status(200);
    };

    @OpenApi(
            path = "/types/:typeId",            // only necessary to include when using static method references
            method = HttpMethod.GET,    // only necessary to include when using static method references
            summary = "Get Assessment Type by type id",
            operationId = "getAssessmentTypeById",
            tags = {"assessmentType"},
            pathParams = {@OpenApiParam(name = "typeId", type = Integer.class, description = "The type ID")},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AssessmentType.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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

    @OpenApi(
            path = "/types",            // only necessary to include when using static method references
            method = HttpMethod.PUT,    // only necessary to include when using static method references
            summary = "Update an Assessment Type",
            operationId = "updateAssessmentType",
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AssessmentType.class)}),
            tags = {"assessmentType"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AssessmentType.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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

    @OpenApi(
            path = "/types/:typeId",            // only necessary to include when using static method references
            method = HttpMethod.DELETE,    // only necessary to include when using static method references
            summary = "Delete an Assessment Type",
            operationId = "deleteAssessmentType",
            tags = {"assessmentType"},
            pathParams = {@OpenApiParam(name = "typeId", type = Integer.class, description = "The type ID")},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
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
