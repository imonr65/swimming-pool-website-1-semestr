package repository.impl.referenceBook;

import models.Level;
import repository.interfaces.LevelInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LevelRepositoryImpl implements LevelInterface {

    private final Connection connection;

    private static final String SQL_INSERT = "insert into levels(id, level_name) values(?,?)";

    private static final String SQL_SELECT = "select id, level_name from levels";

    private static final String SQL_SELECT_BY_ID = "select id, level_name from levels where id = ?";

    public LevelRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Level> findByName(String name) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Level> findAll() throws SQLException {
        List<Level> levels = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                levels.add(Level.builder()
                        .id(resultSet.getLong("id"))
                        .levelName(resultSet.getString("level_name"))
                        .build());
            }
        }
        return levels;
    }

    @Override
    public Optional<Level> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(Level.builder()
                        .id(resultSet.getLong("id"))
                        .levelName(resultSet.getString("level_name"))
                        .build());
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Level entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getLevelName());
        statement.executeUpdate();
    }

    @Override
    public void update(Level entity) throws SQLException {

    }

    @Override
    public void remove(Level entity) throws SQLException {

    }
}
