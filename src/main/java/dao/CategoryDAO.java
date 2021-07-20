package dao;

import java.util.List;

import dtos.CatAvg;
import exceptions.DuplicateResource;
import exceptions.InvalidValue;
import exceptions.ResourceNotFound;
import exceptions.ResourceUnchangable;
import models.Category;

public interface CategoryDAO {
    
    /**
     * Creates a new category in the database
     * 
     * @param category - category object being added to database
     * @return Category created in the database
     * @throws DuplicateResource - If database requires a unique category name that is already in the database
     * @throws InvalidValue - If category name was left blank
     */
    Category createCategory(Category category) throws DuplicateResource, InvalidValue;

    /**
     * Gets all categories in the database
     * 
     * @return A list of all categories in the database
     */
    List<Category> getCategories();

    /**
     * Gets a category from database based on its ID
     * 
     * @param categoryId - ID of the category being returned
     * @return The category matching the ID found
     * @throws ResourceNotFound If no category was found with the given ID
     */
    Category getCategory(int categoryId) throws ResourceNotFound;

    /**
     * Updates a category with an ID matching the category provided
     * 
     * @param category - Category being updated
     * @return The updated category
     * @throws ResourceNotFound If no category had a matching ID in the database
     * @throws DuplicateResource If database requires a unique cateogry name that is already in the database
     * @throws InvalidValue If category name is empty
     */
    Category updateCategory(Category category) throws ResourceNotFound, DuplicateResource, InvalidValue;

    /**
     * Deletes the cateogry from the database with a matching ID
     * 
     * @param categoryId - Cateogry ID being removed from database
     * @throws ResourceNotFound If the ID provided doesn't exist in the database
     * @throws ResourceUnchangable If there exists parent objects in the database that reference this category
     */
    void deleteCategory(int categoryId) throws ResourceNotFound, ResourceUnchangable;

    /**
     * Adds a category to an assessment
     * 
     * @param assessmentId - Assessment ID recieveing an existing category
     * @param categoryId - Existing category being added to assessment
     * @return The category added to an assessment
     * @throws ResourceNotFound - If category ID doesn't exist in the database
     * @throws InvalidValue - If assessment ID doesn't exist in the database
     */
    Category addCategory(int assessmentId, int categoryId) throws ResourceNotFound, InvalidValue;

    /**
     * Gets all categories added to an assessment
     * 
     * @param assessmentId - The assessment ID categories are retrieved from in the database
     * @return A list of cateogires attached to the assessment matching the given ID
     */
    List<Category> getCategories(int assessmentId);

    /**
     * Removes a category from an assessment
     * 
     * @param assessmentId - Assessment ID the category is being removed from
     * @param categoryId - Existing category ID attached to an assessment
     * @throws ResourceNotFound - If assessment ID and category ID were not found together
     */
    void removeCategory(int assessmentId, int categoryId) throws ResourceNotFound;

    /**
     * Returns the average grade per assessment for assessments of a given category
     * @param categoryName The name of the category to get the assessments from
     * @return A list of pairs of assessment ids and their averages
     */
    List<CatAvg> getAvgCategory(String categoryName);
}
