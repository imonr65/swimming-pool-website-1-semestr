package services.interfaces;

import models.Workout;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface WorkoutsService {

    List<Workout> getAllWorkouts() throws SQLException;

    List<Workout> getWorkoutsByFilters(Integer categoryId, Integer peopleId, Integer levelId) throws SQLException;

    void saveWorkout(Workout workout) throws SQLException;

    void deleteWorkout(Integer id) throws SQLException;

    Optional<Workout> getWorkoutById(Integer id) throws SQLException;

}
