package services.interfaces;

import models.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CategoriesService {

    List<Category> getAllCategories() throws SQLException;

    void saveCategory(Category category) throws SQLException;

    Optional<Category> getCategory(int id) throws SQLException;
}
