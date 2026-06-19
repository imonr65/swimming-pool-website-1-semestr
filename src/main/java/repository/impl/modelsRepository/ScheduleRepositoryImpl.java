package repository.impl.modelsRepository;

import models.Schedule;
import repository.interfaces.ScheduleRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO schedules " +
            "(coach_id, workout_id, start_workout, end_workout, max_participants, current_participants) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, coach_id, workout_id, start_workout, end_workout, max_participants, current_participants " +
                    "FROM schedules WHERE id = ?";

    private static final String SQL_SELECT_BY_COACH_ID =
            "SELECT id, coach_id, workout_id, start_workout, end_workout, max_participants, current_participants " +
                    "FROM schedules WHERE coach_id = ?";

    private static final String SQL_SELECT_BY_WORKOUT_ID =
            "SELECT id, coach_id, workout_id, start_workout, end_workout, max_participants, current_participants " +
                    "FROM schedules WHERE workout_id = ?";

    private static final String SQL_SELECT_AVAILABLE =
            "SELECT id, coach_id, workout_id, start_workout, end_workout, max_participants, current_participants " +
                    "FROM schedules WHERE workout_id = ? AND start_workout >= ? AND end_workout <= ? AND current_participants < max_participants";

    private static final String SQL_UPDATE =
            "UPDATE schedules " +
                    "SET coach_id = ?, workout_id = ?, start_workout = ?, end_workout = ?, max_participants = ?, current_participants = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM schedules WHERE id = ?";

    private static final String SQL_CHECK_CONFLICT =
            "SELECT 1 FROM schedules WHERE coach_id = ? AND (id != COALESCE(?, -1)) AND " +
                    "( " +
                    "   (start_workout <= ? AND end_workout > ?) OR " +
                    "   (start_workout < ? AND end_workout >= ?) OR " +
                    "   (start_workout >= ? AND end_workout <= ?) OR " +
                    "   (start_workout <= ? AND end_workout >= ?) " +
                    ")";

    public ScheduleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Schedule> findById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
        preparedStatement.setInt(1, id);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Schedule entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, entity.getCoachId());
        statement.setInt(2, entity.getWorkoutId());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getStartTime()));
        statement.setTimestamp(4, Timestamp.valueOf(entity.getEndTime()));
        statement.setInt(5, entity.getMaxParticipants());
        statement.setInt(6, entity.getCurrentParticipants());
        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void update(Schedule entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setInt(1, entity.getCoachId());
        statement.setInt(2, entity.getWorkoutId());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getStartTime()));
        statement.setTimestamp(4, Timestamp.valueOf(entity.getEndTime()));
        statement.setInt(5, entity.getMaxParticipants());
        statement.setInt(6, entity.getCurrentParticipants());
        statement.setInt(7, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public void remove(Schedule entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public List<Schedule> findByCoachId(Integer coachId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_COACH_ID);
        statement.setInt(1, coachId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                schedules.add(fillForm(resultSet));
            }
        }
        return schedules;
    }

    @Override
    public List<Schedule> findByWorkoutId(Integer workoutId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_WORKOUT_ID);
        statement.setInt(1, workoutId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                schedules.add(fillForm(resultSet));
            }
        }
        return schedules;
    }

    @Override
    public List<Schedule> findAvailableSchedules(Integer workoutId, LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_AVAILABLE);
        statement.setInt(1, workoutId);
        statement.setTimestamp(2, Timestamp.valueOf(start));
        statement.setTimestamp(3, Timestamp.valueOf(end));
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                schedules.add(fillForm(resultSet));
            }
        }
        return schedules;
    }

    @Override
    public boolean existsConflict(Integer coachId, LocalDateTime start, LocalDateTime end) throws SQLException {
        return existsConflict(coachId, start, end, null);
    }


    private boolean existsConflict(Integer coachId, LocalDateTime start, LocalDateTime end, Integer excludeScheduleId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_CHECK_CONFLICT);
        statement.setInt(1, coachId);
        statement.setObject(2, excludeScheduleId);

        statement.setTimestamp(3, Timestamp.valueOf(start));
        statement.setTimestamp(4, Timestamp.valueOf(start));

        statement.setTimestamp(5, Timestamp.valueOf(end));
        statement.setTimestamp(6, Timestamp.valueOf(end));

        statement.setTimestamp(7, Timestamp.valueOf(start));
        statement.setTimestamp(8, Timestamp.valueOf(end));

        statement.setTimestamp(9, Timestamp.valueOf(start));
        statement.setTimestamp(10, Timestamp.valueOf(end));

        try (ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
        }
    }

    @Override
    public void removeById(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    private Schedule fillForm(ResultSet resultSet) throws SQLException {
        return Schedule.builder()
                .id(resultSet.getInt("id"))
                .coachId(resultSet.getInt("coach_id"))
                .workoutId(resultSet.getInt("workout_id"))
                .startTime(resultSet.getTimestamp("start_workout").toLocalDateTime())
                .endTime(resultSet.getTimestamp("end_workout").toLocalDateTime())
                .maxParticipants(resultSet.getInt("max_participants"))
                .currentParticipants(resultSet.getInt("current_participants"))
                .build();
    }
}