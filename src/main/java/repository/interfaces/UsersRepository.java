package repository.interfaces;

import models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByEmail(String userEmail) throws SQLException;

    List<User> findAll() throws SQLException;

    void updatePhoto(String email, Long photoId) throws SQLException;

    User findByUUID(UUID uuid) throws SQLException;

    void updateRole(String email, int roleId) throws SQLException;
}
