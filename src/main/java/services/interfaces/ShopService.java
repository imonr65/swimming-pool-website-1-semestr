package services.interfaces;

import models.Product;
import models.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ShopService {
    List<Product> getAllProducts();
    List<Product> getFilteredProducts(Long categoryId, Integer minPrice, Integer maxPrice, Boolean isAvailable);
    List<ProductCategory> getAllProductCategories();
    Optional<Product> getProductById(Long id);
}
