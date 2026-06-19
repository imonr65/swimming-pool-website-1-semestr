package repository.impl.referenceBook;

import models.Role;
import repository.interfaces.RoleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryImpl implements RoleRepository {

    private final Connection connection;

    private static final String SQL_INSERT_ROLES = "insert into roles(role_name) values(?)";

    private static final String SQL_SELECT_ROLES = "select * from roles order by id";

    private static final String SQL_SELECT_FROM_ROLES_BY_ID = "select * from roles where id = ?";

    private static final String SQL_SELECT_FROM_ROLES_BY_NAME = "select * from roles where role_name = ?";

    private static final String SQL_UPDATE_ROLE = "update roles set role_name = ? where id = ?";

    private static final String SQL_DELETE_ROLE = "delete from roles where id = ?";

    public RoleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ROLES, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, role.getName());
        statement.executeUpdate();
    }


    @Override
    public Optional<Role> findByName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_ROLES_BY_NAME);
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Role role = Role.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("role_name"))
                        .build();
                return Optional.of(role);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ROLES);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Role role = Role.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("role_name"))
                    .build();
            roles.add(role);
        }
        return roles;
    }

    @Override
    public Optional<Role> findById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_ROLES_BY_ID);
        statement.setInt(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Role role = Role.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("role_name"))
                        .build();
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ROLE);
        statement.setString(1, role.getName());
        statement.setInt(2, role.getId());
        statement.executeUpdate();
    }

    @Override
    public void remove(Role role) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ROLE);
        statement.setInt(1, role.getId());
        statement.executeUpdate();
    }
}
