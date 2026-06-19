package repository.interfaces;

import models.Schedule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends CrudRepository<Schedule> {
    Optional<Schedule> findById(Integer id) throws SQLException;

    List<Schedule> findByCoachId(Integer coachId) throws SQLException;

    List<Schedule> findByWorkoutId(Integer workoutId) throws SQLException;

    List<Schedule> findAvailableSchedules(Integer workoutId, LocalDateTime start, LocalDateTime end) throws SQLException;

    boolean existsConflict(Integer coachId, LocalDateTime start, LocalDateTime end) throws SQLException;

    void removeById(Integer id) throws SQLException;
}
