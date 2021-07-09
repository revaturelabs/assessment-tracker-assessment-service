package services;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public interface CategoryService {
    
    List<Category> getCategories();

    Category getCategory(int categoryId) throws ResourceNotFound;

    Category updateCategory(Category category) throws ResourceNotFound;
    
    void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable;

    Category createCategory(Category category) throws DuplicateResource;
}
