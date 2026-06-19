package repository.impl.modelsRepository;

import models.Category;
import models.PeopleType;
import models.Pricing;
import repository.interfaces.PricingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PricingRepositoryImpl implements PricingRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "insert into pricing(name, category_id, price_for_one, period, type_id) values(?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID = "SELECT p.*, c.category_name, pt.type_name " +
            "FROM pricing p " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN peolple_types pt ON p.type_id = pt.id " +
            "WHERE p.id = ?";
    private static final String SQL_DELETE = "delete from pricing where id = ?";

    private static final String SQL_SELECT_BY_CATEGORY_AND_TYPE_ID = "SELECT p.*, c.category_name, pt.type_name " +
            "FROM pricing p " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN peolple_types pt ON p.type_id = pt.id " +
            "WHERE p.category_id = ? AND p.type_id = ?";
    private static final String SQL_SELECT = "select id, name, category_id, price_for_one, period, type_id from pricing";

    private static final String SQL_SELECT_BY_CATEGORY_AND_TYPES_ID = "select id, name, category_id, price_for_one, period, type_id from pricing where category_id = ? and type_id = ?";

    public PricingRepositoryImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Pricing> findAll() throws SQLException {
        List<Pricing> pricings = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                pricings.add(fillForm(resultSet));
            }
        }

        return pricings;
    }

    @Override
    public List<Pricing> findByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException {
        List<Pricing> pricings = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CATEGORY_AND_TYPE_ID);
        statement.setLong(1, categoryId);
        statement.setLong(2, peopleTypeId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                pricings.add(fillForm(resultSet));
            }
        }

        return pricings;
    }

    @Override
    public List<Pricing> findAllWithDetails() throws SQLException {
        return List.of();
    }

    @Override
    public void removeById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    @Override
    public Optional<Pricing> findByCategoryIdAndTypeId(Integer categoryId, Integer typeId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CATEGORY_AND_TYPES_ID);
        statement.setInt(1, categoryId);
        statement.setInt(2, typeId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Pricing> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(Pricing entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getName());
        statement.setInt(2, entity.getCategory().getId());
        statement.setLong(3, entity.getPriceForOne());
        statement.setString(4, entity.getPeriod());
        statement.setInt(5, entity.getPeopleType().getId());
        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void update(Pricing entity) throws SQLException {

    }

    @Override
    public void remove(Pricing entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setLong(1, entity.getId());
        statement.executeUpdate();
    }

    private Pricing fillForm(ResultSet resultSet) throws SQLException {
        Category category = Category.builder()
                .id(resultSet.getInt("category_id"))
                .build();

        PeopleType peopleType = PeopleType.builder()
                .id(resultSet.getInt("type_id"))
                .build();

        return Pricing.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .category(category)
                .priceForOne(resultSet.getInt("price_for_one"))
                .period(resultSet.getString("period"))
                .peopleType(peopleType)
                .build();
    }
}
