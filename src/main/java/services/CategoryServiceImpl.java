package services;

import java.util.List;

import dao.CategoryDAO;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public class CategoryServiceImpl implements CategoryService{

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO){
        this.categoryDAO = categoryDAO;
    }
    
    @Override
    public Category createCategory(Category category) throws DuplicateResource, InvalidValue {
        return this.categoryDAO.createCategory(category);
    }

    @Override
    public List<Category> getCategories() {
        return this.categoryDAO.getCategories();
    }

    @Override
    public Category getCategory(int categoryId) throws ResourceNotFound {
        return this.categoryDAO.getCategory(categoryId);
    }

    @Override
    public Category updateCategory(Category category) throws ResourceNotFound, DuplicateResource, InvalidValue {
        return this.categoryDAO.updateCategory(category);
    }

    @Override
    public void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable {
        this.categoryDAO.deleteCategory(categoryId);
    }

    @Override
    public Category addCategory(int assessmentId, int categoryId) throws ResourceNotFound, InvalidValue {
        return this.categoryDAO.addCategory(assessmentId, categoryId);
    }

    @Override
    public List<Category> getCategories(int assessmentId) {
        return this.categoryDAO.getCategories(assessmentId);
    }

    @Override
    public void removeCategory(int assessmentId, int categoryId) throws ResourceNotFound {
        // TODO Auto-generated method stub
        
    }
    
}
