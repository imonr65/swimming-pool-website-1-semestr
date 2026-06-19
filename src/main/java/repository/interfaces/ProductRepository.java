package repository.interfaces;

import models.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product> {
    Optional<Product> findByName(String name) throws SQLException;

    List<Product> findAll() throws SQLException;

    List<Product> findByCategory(Long categoryId) throws SQLException;

    List<Product> findByPriceRange(Integer minPrice, Integer maxPrice) throws SQLException;

    List<Product> findByFilters(Long categoryId, Integer minPrice, Integer maxPrice, Boolean isAvailable) throws SQLException;

    void removeById(Long id) throws SQLException;
}
