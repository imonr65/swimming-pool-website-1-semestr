package repository.interfaces;

import models.ProductCategory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductCategoriesRepository extends CrudRepository<ProductCategory> {
    Optional<ProductCategory> findByName(String name) throws SQLException;

    List<ProductCategory> findAll() throws SQLException;
}
