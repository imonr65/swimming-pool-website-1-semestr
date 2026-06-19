package services.impl;

import models.Category;
import repository.interfaces.CategoriesRepository;
import services.interfaces.CategoriesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        return categoriesRepository.findAll();
    }

    @Override
    public void saveCategory(Category category) throws SQLException {
        categoriesRepository.save(category);
    }

    @Override
    public Optional<Category> getCategory(int id) throws SQLException {
        return categoriesRepository.findById(id);
    }
}
