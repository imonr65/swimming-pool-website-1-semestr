package repository.impl.referenceBook;

import repository.interfaces.WorkoutCoachRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkoutCoachRepositoryImpl implements WorkoutCoachRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "insert into workouts_coaches(workout_id, coach_id) values(?, ?)";

    private static final String SQL_DELETE = "delete from workouts_coaches where workout_id = ? and coach_id = ?";

    private static final String SQL_SELECT_COACHES_BY_WORKOUT_ID = "select coach_id from workouts_coaches where workout_id = ?";

    private static final String SQL_SELECT_WORKOUTS_BY_COACH_ID = "select workout_id from workouts_coaches where coach_id = ?";

    private static final String SQL_CHECK_ASSIGNMENT = "select 1 from workouts_coaches where workout_id = ? and coach_id = ?";

    public WorkoutCoachRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void assignCoachToWorkout(int workoutId, int coachId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        statement.setInt(1, workoutId);
        statement.setInt(2, coachId);
        statement.executeUpdate();
    }

    @Override
    public void removeCoachFromWorkout(int workoutId, int coachId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, workoutId);
        statement.setInt(2, coachId);
        statement.executeUpdate();
    }

    @Override
    public List<Integer> findCoachIdsByWorkoutId(int workoutId) throws SQLException {
        List<Integer> coachIds = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COACHES_BY_WORKOUT_ID);
        statement.setInt(1, workoutId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                coachIds.add(resultSet.getInt("coach_id"));
            }
        }
        return coachIds;
    }

    @Override
    public List<Integer> findWorkoutIdsByCoachId(int coachId) throws SQLException {
        List<Integer> workoutIds = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_WORKOUTS_BY_COACH_ID);
        statement.setInt(1, coachId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                workoutIds.add(resultSet.getInt("workout_id"));
            }
        }
        return workoutIds;
    }

    @Override
    public boolean isCoachAssignedToWorkout(int coachId, int workoutId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_CHECK_ASSIGNMENT);
        statement.setInt(1, coachId);
        statement.setInt(2, workoutId);
        try (ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
        }
    }

    @Override
    public void save(Integer coachId, Integer workoutId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
        statement.setInt(1, coachId);
        statement.setInt(2, workoutId);
        statement.executeUpdate();
    }

    @Override
    public void delete(Integer coachId, Integer workoutId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, coachId);
        statement.setInt(2, workoutId);
        statement.executeUpdate();
    }
}
