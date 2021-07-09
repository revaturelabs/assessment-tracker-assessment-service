package dao;

import java.util.List;

import models.Category;

public interface CategoryDAO {

    List<Category> getCategories();

    Category getCategory(int categoryId);

    boolean deleteCategory(int categoryId);

    Category createCategory(Category category);

}
