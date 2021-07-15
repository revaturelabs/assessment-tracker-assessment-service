package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.javalin.http.Handler;
import io.javalin.plugin.openapi.annotations.*;
import services.CategoryService;
import models.Category;
import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import exceptions.InvalidValue;

public class CategoryController {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private CategoryService categoryService;
    private final Gson gson = new Gson();

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @OpenApi(
            path = "/categories",
            method = HttpMethod.POST,
            summary = "Create an Assessment Category",
            operationId = "createCategory",
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Category.class)}),
            tags = {"Category"},
            responses = {
                    @OpenApiResponse(status = "201", content = {@OpenApiContent(from = Category.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler createCategory = context -> {
        try {
            Category category = gson.fromJson(context.body(), Category.class);
            if (category == null)
                throw new JsonSyntaxException("");
            category = this.categoryService.createCategory(category);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(category));
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
            path = "/categories",
            method = HttpMethod.GET,
            summary = "Get all Assessment Categories",
            operationId = "getCategories",
            tags = {"Category"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Category[].class)}),

            }
    )
    public Handler getCategories = context -> {
        List<Category> categories = categoryService.getCategories();
        context.contentType(CONTENT_TYPE_JSON);
        context.result(gson.toJson(categories));
        context.status(200);
    };

    @OpenApi(
            path = "/categories/:categoryId",
            method = HttpMethod.GET,
            summary = "Get an Assessment Category by ID",
            operationId = "getCategoryById",
            pathParams = {@OpenApiParam(name = "categoryId", type = Integer.class, description = "The category ID")},
            tags = {"Category"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Category.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler getCategoryById = context -> {
        try {
            int categoryId = Integer.parseInt(context.pathParam("categoryId"));
            Category category = this.categoryService.getCategory(categoryId);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(category));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Category ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
            path = "/categories",
            method = HttpMethod.PUT,
            summary = "Update an Assessment Category",
            operationId = "updateCategory",
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Category.class)}),
            tags = {"Category"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Category.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler updateCategory = context -> {
        try {
            Category category = gson.fromJson(context.body(), Category.class);
            if (category == null)
                throw new JsonSyntaxException("");
            category = this.categoryService.updateCategory(category);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(category));
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
            path = "/categories/:categoryId",
            method = HttpMethod.DELETE,
            summary = "Delete an Assessment Category by ID",
            operationId = "deleteCategory",
            pathParams = {@OpenApiParam(name = "categoryId", type = Integer.class, description = "The category ID")},
            tags = {"Category"},
            responses = {
                    @OpenApiResponse(status = "204"),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "409", content = {@OpenApiContent(from = String.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
            }
    )
    public Handler deleteCategory = context -> {
        try {
            int categoryId = Integer.parseInt(context.pathParam("categoryId"));
            this.categoryService.deleteCategory(categoryId);
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
            context.result("Category ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
        path = "/assessments/:assessmentId/categories/:categoryId",
        method = HttpMethod.POST,
        summary = "Adds a category to an assessment",
        operationId = "addCategoryToAssessment",
        pathParams = {
            @OpenApiParam(name = "assessmentId", type = Integer.class, description = "The assessment ID"),
            @OpenApiParam(name = "categoryId", type = Integer.class, description = "The category ID")},
        tags = {"Category"},
        responses = {
                @OpenApiResponse(status = "201", content = {@OpenApiContent(from = Category.class)}),
                @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                @OpenApiResponse(status = "422", content = {@OpenApiContent(from = String.class)}),
                @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
        }
    )
    public Handler addCategoryToAssessment = context -> {
        try {
            int assessmentId = Integer.parseInt(context.pathParam("assessmentId"));
            int categoryId = Integer.parseInt(context.pathParam("categoryId"));
            Category category = this.categoryService.addCategory(assessmentId, categoryId);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(category));
            context.status(201);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (InvalidValue e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(422);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Category or assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
        path = "/assessments/:assessmentId/categories",
        method = HttpMethod.GET,
        summary = "Gets all categories of an assessment",
        operationId = "getCategoriesForAssessment",
        pathParams = {
            @OpenApiParam(name = "assessmentId", type = Integer.class, description = "The assessment ID")},
        tags = {"Category"},
        responses = {
                @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Category.class)}),
                @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
        }
    )
    public Handler getCategoriesForAssessment = context -> {
        try{
            int assessmentId = Integer.parseInt(context.pathParam("assessmentId"));
            List<Category> categories = this.categoryService.getCategories(assessmentId);
            context.contentType(CONTENT_TYPE_JSON);
            context.result(gson.toJson(categories));
            context.status(200);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };

    @OpenApi(
        path = "/assessments/:assessmentId/categories/:categoryId",
        method = HttpMethod.DELETE,
        summary = "Deletes a category from an assessment",
        operationId = "deleteCategoryForAssessment",
        pathParams = {
            @OpenApiParam(name = "assessmentId", type = Integer.class, description = "The assessment ID"),
            @OpenApiParam(name = "categoryId", type = Integer.class, description = "The category ID")},
        tags = {"Category"},
        responses = {
                @OpenApiResponse(status = "204"),
                @OpenApiResponse(status = "404", content = {@OpenApiContent(from = String.class)}),
                @OpenApiResponse(status = "400", content = {@OpenApiContent(from = String.class)})
        }
    )
    public Handler deleteCategoryForAssessment = context -> {
        try {
            int assessmentId = Integer.parseInt(context.pathParam("assessmentId"));
            int categoryId = Integer.parseInt(context.pathParam("categoryId"));
            this.categoryService.removeCategory(assessmentId, categoryId);
            context.status(204);
        } catch (ResourceNotFound e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result(e.getMessage());
            context.status(404);
        } catch (NumberFormatException e) {
            context.contentType(CONTENT_TYPE_TEXT);
            context.result("Category or assessment ID couldn't be parsed correctly");
            context.status(400);
        }
    };
}
