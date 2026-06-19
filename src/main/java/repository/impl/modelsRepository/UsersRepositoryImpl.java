package repository.impl.modelsRepository;


import models.Role;
import models.User;
import repository.impl.referenceBook.RoleRepositoryImpl;
import repository.interfaces.RoleRepository;
import repository.interfaces.UsersRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersRepositoryImpl implements UsersRepository {

    private final Connection connection;


    private static final String SQL_INSERT_USER = "INSERT INTO users(id, first_name, last_name, email, password, phone, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_FROM_USERS_BY_NAME = "select id, photo_id, first_name, last_name, email, password, phone, role_id from users where email = ?";

    private static final String SQL_SELECT_ALL_FROM_USERS = "select id, photo_id, first_name, last_name, email, password, phone, role_id from users";

    private static final String SQL_UPDATE_USER = "update users set first_name = ?, last_name = ?, phone = ? where email = ?";
    private static final String SQL_UPDATE_USER_PHOTO = "update users set photo_id = ? where email = ?";

    private static final String SQL_SELECT_BY_UUID = "select id, photo_id, first_name, last_name, email, password, phone, role_id from users where id = ?";

    private static final String SQL_UPDATE_USER_ROLE = "update users set role_id = ? where email = ?";

    private static final String SQL_DELETE = "delete from users where id = ?";

    private final RoleRepository roleRepository;

    public UsersRepositoryImpl(Connection connection) {
        this.connection = connection;
        roleRepository = new RoleRepositoryImpl(connection);

    }

    @Override
    public void save(User entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER);
        statement.setObject(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        statement.setString(4, entity.getEmail());
        statement.setString(5, entity.getPassword());
        statement.setString(6, entity.getPhone());

        if (entity.getRole() != null && entity.getRole().getId() != null) {
            statement.setInt(7, entity.getRole().getId());
        }
        statement.executeUpdate();
    }

    @Override
    public void update(User entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER);

        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getPhone());
        statement.setString(4, entity.getEmail());
        statement.executeUpdate();
    }

    public void updatePhoto(String email, Long photoId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER_PHOTO);
        if (photoId != null) {
            statement.setLong(1, photoId);
        }
        statement.setString(2, email);
        statement.executeUpdate();
    }

    @Override
    public User findByUUID(UUID uuid) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_UUID);
        statement.setObject(1, uuid);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return fillForm(resultSet);
            }
        }
        throw new SQLException("User not found");
    }

    @Override
    public void updateRole(String email, int roleId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER_ROLE);
        statement.setInt(1, roleId);
        statement.setString(2, email);
        statement.executeUpdate();
    }

    @Override
    public void remove(User entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setObject(1, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String userEmail) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_USERS_BY_NAME);
        statement.setString(1, userEmail);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_FROM_USERS);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(fillForm(resultSet));
            }
        }

        return users;
    }


    private User fillForm(ResultSet resultSet) throws SQLException {
        Role role = roleRepository.findById(resultSet.getInt("role_id")).orElseThrow(() -> new IllegalArgumentException("нет такой роли"));
        User user = User.builder()
                .id((UUID) resultSet.getObject("id"))
                .password(resultSet.getString("password"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .phone(resultSet.getString("phone"))
                .photoId(resultSet.getLong(("photo_id")))
                .role(role)
                .build();
        return user;
    }
}
