package services.impl;

import models.Workout;
import org.springframework.jdbc.core.JdbcTemplate;
import repository.interfaces.WorkoutsRepository;
import services.interfaces.WorkoutsService;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WorkoutsServiceImpl implements WorkoutsService {

    private final WorkoutsRepository repository;
    public WorkoutsServiceImpl(WorkoutsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Workout> getAllWorkouts() throws SQLException {
        return repository.findAll();
    }

    @Override
    public List<Workout> getWorkoutsByFilters(Integer categoryId, Integer peopleId, Integer levelId) throws SQLException {
        try {
            return repository.findByFilters(categoryId, peopleId, levelId);
        } catch (SQLException e) {
            throw new RuntimeException("Error filtering workouts", e);
        }
    }

    @Override
    public Optional<Workout> getWorkoutById(Integer id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding workout", e);
        }
    }

    @Override
    public void saveWorkout(Workout workout) throws SQLException {
        repository.save(workout);
    }

    @Override
    public void deleteWorkout(Integer id) throws SQLException {
        repository.removeById(id);
    }

}
