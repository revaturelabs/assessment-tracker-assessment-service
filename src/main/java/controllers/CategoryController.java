package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Handler;
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

    public Handler getCategories = context -> {
        List<Category> categories = categoryService.getCategories();
        context.contentType(CONTENT_TYPE_JSON);
        context.result(gson.toJson(categories));
        context.status(200);
    };

    public Handler getCategoryById = context -> {
        try {
            int categoryId = Integer.parseInt(context.pathParam("id"));
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

    public Handler deleteCategory = context -> {
        try {
            int categoryId = Integer.parseInt(context.pathParam("id"));
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
}
