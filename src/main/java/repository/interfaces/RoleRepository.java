package repository.interfaces;

import models.Role;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role> {

    Optional<Role> findByName(String name) throws SQLException;

    List<Role> findAll() throws SQLException;
}
