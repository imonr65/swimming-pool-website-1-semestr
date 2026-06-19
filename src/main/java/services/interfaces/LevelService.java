package services.interfaces;

import models.Level;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LevelService {
    List<Level> getAllLevels() throws SQLException;

    void save(Level level) throws SQLException;

    Optional<Level> getLevelById(int id) throws SQLException;
}
