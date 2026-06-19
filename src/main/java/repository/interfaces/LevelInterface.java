package repository.interfaces;

import models.Level;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LevelInterface extends CrudRepository<Level> {
    Optional<Level> findByName(String name) throws SQLException;

    List<Level> findAll() throws SQLException;
}
