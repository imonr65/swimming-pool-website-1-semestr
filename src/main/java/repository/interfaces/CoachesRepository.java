package repository.interfaces;

import models.Coach;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachesRepository extends CrudRepository<Coach> {
    Optional<Coach> findByCoachName(String firstName, String lastName) throws SQLException;

    List<Coach> getAllCoaches() throws SQLException;

    void removeById(int id) throws SQLException;

    List<Coach> findWorkoutId(int workoutId) throws SQLException;

    Optional<Coach> findByUserId(UUID userId) throws SQLException;
}
