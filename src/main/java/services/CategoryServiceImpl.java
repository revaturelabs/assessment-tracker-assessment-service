package services;

import java.util.List;

import dao.CategoryDAO;
import exceptions.DuplicateResource;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public class CategoryServiceImpl implements CategoryService{

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO){
        this.categoryDAO = categoryDAO;
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
    public Category updateCategory(Category category) throws ResourceNotFound {
        return this.categoryDAO.updateCategory(category);
    }

    @Override
    public void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable {
        this.categoryDAO.deleteCategory(categoryId);
    }

    @Override
    public Category createCategory(Category category) throws DuplicateResource {
        return this.categoryDAO.createCategory(category);
    }
    
}
