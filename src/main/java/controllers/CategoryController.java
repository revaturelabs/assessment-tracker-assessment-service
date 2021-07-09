package controllers;

import java.util.List;

import com.google.gson.Gson;

import io.javalin.http.Handler;
import services.CategoryService;
import models.Category;
import exceptions.*;

public class CategoryController {

    private static final String CONTENT_TYPE = "application/json";
    private CategoryService categoryService;
    private final Gson gson = new Gson();

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    public Handler getCategories = context -> {
        List<Category> categories = categoryService.getCategories();
        context.contentType(CONTENT_TYPE);
        context.result(gson.toJson(categories));
        context.status(200);
    };

    public Handler getCategoryById = context -> {
        try {
            int categoryId = Integer.parseInt(context.pathParam("id"));
            Category category = this.categoryService.getCategory(categoryId);
            context.contentType(CONTENT_TYPE);
            context.result(gson.toJson(category));
            context.status(200);
        } catch (ResourceNotFound e) {
            context.result(e.getMessage());
            context.status(404);
        }
        catch(NumberFormatException e){
            context.result("Category ID couldn't be parsed correctly");
            context.status(400);
        }
    };

}
