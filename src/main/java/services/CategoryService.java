package services;

import java.util.List;

import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public interface CategoryService {

    Category createCategory(Category category) throws DuplicateResource, InvalidValue;
    
    List<Category> getCategories();

    Category getCategory(int categoryId) throws ResourceNotFound;

    Category updateCategory(Category category) throws ResourceNotFound, DuplicateResource, InvalidValue;
    
    void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable;

    Category addCategory(int assessmentId, int categoryId) throws ResourceNotFound, InvalidValue;

    List<Category> getCategories(int assessmentId);

    void removeCategory(int assessmentId, int categoryId) throws ResourceNotFound;
}
