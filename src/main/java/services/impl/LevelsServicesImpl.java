package services.impl;

import models.Level;
import repository.interfaces.LevelInterface;
import services.interfaces.LevelService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LevelsServicesImpl implements LevelService {

    private final LevelInterface levelRepository;

    public LevelsServicesImpl(LevelInterface levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public List<Level> getAllLevels() throws SQLException {
        return levelRepository.findAll();
    }

    @Override
    public void save(Level level) throws SQLException {
        levelRepository.save(level);
    }

    @Override
    public Optional<Level> getLevelById(int id) throws SQLException {
        return levelRepository.findById(id);
    }


}
