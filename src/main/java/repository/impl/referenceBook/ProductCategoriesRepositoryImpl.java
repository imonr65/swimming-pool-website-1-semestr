package repository.impl.referenceBook;

import models.ProductCategory;
import repository.interfaces.ProductCategoriesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductCategoriesRepositoryImpl implements ProductCategoriesRepository {

    private final Connection connection;

    private final static String SQL_INSERT_INTO_PRODUCT_CATEGORIES = "insert into product_categories(id, category_name) values(?, ?)";

    private final static String SQL_SELECT_BY_ID = "select id, category_name from product_categories where id = ?";

    private final static String SQL_SELECT_BY_CATEGORY_NAME = "select id, category_name from product_categories where category_name = ?";

    private final static String SQL_SELECT_ALL = "select id, category_name from product_categories";

    public ProductCategoriesRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<ProductCategory> findByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CATEGORY_NAME);
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                ProductCategory productCategory = ProductCategory.builder()
                        .id(resultSet.getLong("id"))
                        .categoryName(resultSet.getString("category_name"))
                        .build();
                return Optional.of(productCategory);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ProductCategory> findAll() throws SQLException {
        List<ProductCategory> productCategories = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ProductCategory productCategory = ProductCategory.builder()
                        .id(resultSet.getLong("id"))
                        .categoryName(resultSet.getString("category_name"))
                        .build();
                productCategories.add(productCategory);
            }
        }
        return productCategories;
    }

    @Override
    public Optional<ProductCategory> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                ProductCategory productCategory = ProductCategory.builder()
                        .id(resultSet.getLong("id"))
                        .categoryName(resultSet.getString("category_name"))
                        .build();
                return Optional.of(productCategory);
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(ProductCategory entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_PRODUCT_CATEGORIES);
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getCategoryName());
        statement.executeUpdate();
    }

    @Override
    public void update(ProductCategory entity) throws SQLException {

    }

    @Override
    public void remove(ProductCategory entity) throws SQLException {

    }
}
