package repository.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface WorkoutCoachRepository {

    void assignCoachToWorkout(int workoutId, int coachId) throws SQLException;

    void removeCoachFromWorkout(int workoutId, int coachId) throws SQLException;

    List<Integer> findCoachIdsByWorkoutId(int workoutId) throws SQLException;

    List<Integer> findWorkoutIdsByCoachId(int coachId) throws SQLException;

    boolean isCoachAssignedToWorkout(int coachId, int workoutId) throws SQLException;

    void save(Integer coachId, Integer workoutId) throws SQLException;

    void delete(Integer coachId, Integer workoutId) throws SQLException;

}
