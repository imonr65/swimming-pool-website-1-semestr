package services.impl;

import models.Product;
import models.ProductCategory;
import repository.interfaces.ProductCategoriesRepository;
import repository.interfaces.ProductRepository;
import services.interfaces.ShopService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShopServiceImpl implements ShopService {

    private final ProductRepository productRepository;

    private final ProductCategoriesRepository productCategoriesRepository;

    public ShopServiceImpl(ProductRepository productRepository, ProductCategoriesRepository productCategoriesRepository) {
        this.productRepository = productRepository;
        this.productCategoriesRepository = productCategoriesRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении продуктов", e);
        }
    }
    @Override
    public List<Product> getFilteredProducts(Long categoryId, Integer minPrice, Integer maxPrice, Boolean isAvailable) {
        try {
            return productRepository.findByFilters(categoryId, minPrice, maxPrice, isAvailable);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при фильтрации продуктов", e);
        }
    }
    @Override
    public List<ProductCategory> getAllProductCategories() {
        try {
            return productCategoriesRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении категорий", e);
        }
    }
    @Override
    public Optional<Product> getProductById(Long id) {
        try {
            return productRepository.findById(id.intValue());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении продукта", e);
        }
    }
}
