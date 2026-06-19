package repository.impl.referenceBook;

import models.Category;
import repository.interfaces.CategoriesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriesRepositoryImpl implements CategoriesRepository {

    private final Connection connection;

    private static final String SQL_INSERT_INTO_CATEGORIES = "insert into categories (id, category_name) values (?, ?)";

    private static final String SQL_SELECT_ALL_CATEGORIES = "select id, category_name from categories";

    private static final String SQL_SELECT_CATEGORY_BY_ID = "select id, category_name from categories where id = ?";

    private static final String SQL_SELECT_CATEGORY_BY_NAME = "select id, category_name from categories where category_name = ?";

    private static final String SQL_DELETE_FROM_CATEGORIES = "delete from categories where id = ?";

    public CategoriesRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Category> findByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_CATEGORY_BY_NAME);
        statement.setString(1, name);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Category category = Category.builder()
                        .id(resultSet.getInt("id"))
                        .categoryName(resultSet.getString("category_name"))
                        .build();
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_CATEGORIES);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(Category.builder()
                        .categoryName(resultSet.getString("category_name"))
                        .id(resultSet.getInt("id"))
                        .build());
            }
        }
        return categories;
    }

    @Override
    public Optional<Category> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_CATEGORY_BY_ID);
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Category category = Category.builder()
                        .id(resultSet.getInt("id"))
                        .categoryName(resultSet.getString("category_name"))
                        .build();
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Category entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_CATEGORIES);
        statement.setInt(1, entity.getId());
        statement.setString(2, entity.getCategoryName());
        statement.executeUpdate();
    }

    @Override
    public void update(Category entity) throws SQLException {

    }

    @Override
    public void remove(Category entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_FROM_CATEGORIES);
        statement.setInt(1, entity.getId());
        statement.executeUpdate();
    }
}
