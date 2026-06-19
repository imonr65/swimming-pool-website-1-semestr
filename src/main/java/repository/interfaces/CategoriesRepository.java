package repository.interfaces;

import models.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends CrudRepository<Category> {
    Optional<Category> findByName(String name) throws SQLException;

    List<Category> findAll() throws SQLException;
}
