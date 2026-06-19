package repository.impl.referenceBook;

import models.PeopleType;
import repository.interfaces.PeopleTypeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PeopleTypeRepositoryImpl implements PeopleTypeRepository {

    private final Connection connection;

    private static final String SQL_INSERT_INTO_PEOPLE_TYPES = "insert into peolple_types(type_name) values(?)";

    private static final String SQL_SELECT_FROM_PEOPLE_TYPES_BY_NAME = "select * from peolple_types where type_name=?";

    private static final String SQL_SELECT_FROM_TABLE_BY_ID = "select * from peolple_types where id=?";

    private static final String SQL_SELECT_ALL_FROM_TABLE = "select * from peolple_types order by id";

    private static final String SQL_UPDATE_PEOPLE_TYPE = "update peolple_types set type_name=? where id=?";

    private static final String SQL_DELETE_PEOPLE_TYPE = "delete from peolple_types where id=?";

    public PeopleTypeRepositoryImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void save(PeopleType type) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_PEOPLE_TYPES);
        statement.setString(1, type.getTypeName());
        statement.executeUpdate();
    }

    @Override
    public Optional<PeopleType> findByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_PEOPLE_TYPES_BY_NAME);
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery();) {
            if (resultSet.next()) {
                PeopleType type = PeopleType.builder()
                        .id(resultSet.getInt("id"))
                        .typeName(resultSet.getString("type_name"))
                        .build();
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<PeopleType> findAll() throws SQLException {
        List<PeopleType> types = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_FROM_TABLE);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                PeopleType type = PeopleType.builder()
                        .id(resultSet.getInt("id"))
                        .typeName(resultSet.getString("type_name"))
                        .build();
                types.add(type);
            }
        }
        return types;
    }

    @Override
    public Optional<PeopleType> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_TABLE_BY_ID);
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                PeopleType type = PeopleType.builder()
                        .id(resultSet.getInt("id"))
                        .typeName(resultSet.getString("type_name"))
                        .build();
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }


    @Override
    public void update(PeopleType entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PEOPLE_TYPE);
        statement.setString(1, entity.getTypeName());
        statement.setInt(2, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public void remove(PeopleType entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PEOPLE_TYPE);
        statement.setInt(1, entity.getId());
        statement.executeUpdate();
    }
}
