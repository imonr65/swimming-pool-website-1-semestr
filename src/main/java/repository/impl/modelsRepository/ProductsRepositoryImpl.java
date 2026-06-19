package repository.impl.modelsRepository;

import models.Product;
import models.ProductCategory;
import repository.impl.referenceBook.ProductCategoriesRepositoryImpl;
import repository.interfaces.ProductCategoriesRepository;
import repository.interfaces.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryImpl implements ProductRepository {

    private final Connection connection;

    private static final String SQL_INSERT_INTO_PRODUCT = "insert into products(product_name, photo_id, category_id, description, price, is_available) values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "select id,photo_id, product_name, category_id, description, price, is_available from products where id = ?";

    private static final String SQL_SELECT_BY_NAME = "select id, photo_id,product_name, category_id, description, price, is_available from products where product_name = ?";

    private static final String SQL_SELECT_ALL = "select id, photo_id,product_name, category_id, description, price, is_available from products";

    private static final String SQL_SELECT_BY_CATEGORY = "select id, photo_id,product_name, category_id, description, price, is_available from products where category_id = ?";

    private static final String SQL_SELECT_BY_PRICE_RANGE = "select id, photo_id, product_name, category_id, description, price, is_available from products where price between ? and ?";

    private static final String SQL_SELECT_WITH_FILTERS = "select id, photo_id, product_name, category_id, description, price, is_available from products where 1=1";

    private static final String SQL_DELETE_BY_ID = "delete from products where id = ?";

    private final ProductCategoriesRepository categoriesRepository;

    public ProductsRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.categoriesRepository = new ProductCategoriesRepositoryImpl(connection);
    }

    @Override
    public Optional<Product> findByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NAME);
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(fillForm(resultSet));
            }
        }
        return products;
    }

    @Override
    public List<Product> findByCategory(Long categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CATEGORY);
        statement.setLong(1, categoryId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(fillForm(resultSet));
            }
        }
        return products;
    }

    @Override
    public List<Product> findByPriceRange(Integer minPrice, Integer maxPrice) throws SQLException {
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PRICE_RANGE);
        statement.setInt(1, minPrice);
        statement.setInt(2, maxPrice);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(fillForm(resultSet));
            }
        }
        return products;
    }

    @Override
    public List<Product> findByFilters(Long categoryId, Integer minPrice, Integer maxPrice, Boolean isAvailable) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQL_SELECT_WITH_FILTERS);
        List<Object> parameters = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" and category_id = ?");
            parameters.add(categoryId);
        }
        if (minPrice != null) {
            sql.append(" and price >= ?");
            parameters.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" and price <= ?");
            parameters.add(maxPrice);
        }
        if (isAvailable != null) {
            sql.append(" and is_available = ?");
            parameters.add(isAvailable);
        }

        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(fillForm(resultSet));
            }
        }

        return products;
    }

    @Override
    public Optional<Product> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Product entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_PRODUCT, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getPhotoId());
        statement.setLong(3, entity.getProductCategory().getId());
        statement.setString(4, entity.getDescription());
        statement.setInt(5, entity.getPrice());
        statement.setBoolean(6, entity.getIsAvailable());


        int affectedRows = statement.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    entity.setId(generatedId);
                    System.out.println("Generated ID: " + generatedId);
                } else {
                    System.out.println("No ID was generated");
                }
            }
        }

        statement.close();
    }

    @Override
    public void update(Product entity) throws SQLException {

    }

    @Override
    public void remove(Product entity) throws SQLException {

    }

    @Override
    public void removeById(Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    private Product fillForm(ResultSet resultSet) throws SQLException {
        ProductCategory category = categoriesRepository.findById(resultSet.getInt("category_id"))
                .orElseThrow(() -> new IllegalArgumentException("нет такой категории"));

        Product product = Product.builder()
                .id(resultSet.getLong("id"))
                .photoId(resultSet.getLong("photo_id"))
                .name(resultSet.getString("product_name"))
                .productCategory(category)
                .description(resultSet.getString("description"))
                .price(resultSet.getInt("price"))
                .isAvailable(resultSet.getBoolean("is_available"))
                .build();
        return product;
    }
}
