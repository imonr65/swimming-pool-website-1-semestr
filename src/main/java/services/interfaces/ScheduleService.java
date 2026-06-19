package services.interfaces;

import models.Schedule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    void save(Schedule schedule) throws SQLException;

    void update(Schedule schedule) throws SQLException;

    Schedule findById(int id) throws SQLException;

    List<Schedule> findByCoachID(Integer coachId) throws SQLException;

    List<Schedule> findByWorkoutId(Integer workoutId) throws SQLException;

    List<Schedule> getAvailableSchedules(Integer workoutId, LocalDateTime start, LocalDateTime end) throws SQLException;

    void deleteSchedule(Integer id) throws SQLException;

    boolean hasScheduleConflict(Integer coachId, LocalDateTime start, LocalDateTime end) throws SQLException;

    void incrementParticipants(Integer scheduleId) throws SQLException;

    void decrementParticipants(Integer scheduleId) throws SQLException;
}
