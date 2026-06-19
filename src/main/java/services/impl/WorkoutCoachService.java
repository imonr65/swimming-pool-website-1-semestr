package services.impl;

import dto.CoachAssignmentDto;
import models.Coach;
import models.Workout;
import repository.interfaces.CoachesRepository;
import repository.interfaces.WorkoutCoachRepository;
import repository.interfaces.WorkoutsRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkoutCoachService {

    private final WorkoutCoachRepository workoutCoachRepository;
    private final WorkoutsRepository workoutsRepository;
    private final CoachesRepository coachesRepository;


    public WorkoutCoachService(
            WorkoutCoachRepository workoutCoachRepository,
            WorkoutsRepository workoutsRepository,
            CoachesRepository coachesRepository
    ) {
        this.workoutCoachRepository = workoutCoachRepository;
        this.workoutsRepository = workoutsRepository;
        this.coachesRepository = coachesRepository;
    }

    public void assignCoachToWorkout(int workoutId, int coachId) throws SQLException {
        Optional<Workout> workout = workoutsRepository.findById(workoutId);
        Optional<Coach> coach = coachesRepository.findById(coachId);

        if (workout.isPresent() && coach.isPresent()) {
            workoutCoachRepository.assignCoachToWorkout(workoutId, coachId);
        } else {
            throw new IllegalArgumentException("Workout or coach not found");
        }
    }

    public void removeCoachFromWorkout(int workoutId, int coachId) throws SQLException {
        workoutCoachRepository.removeCoachFromWorkout(workoutId, coachId);
    }

    public List<Workout> getWorkoutsForCoach(int coachId) throws SQLException {
        return workoutsRepository.findByCoachId(coachId);
    }

    public List<Coach> getCoachesForWorkout(int workoutId) throws SQLException {
        return coachesRepository.findWorkoutId(workoutId);
    }

    public boolean isCoachAssignedToWorkout(int coachId, int workoutId) throws SQLException {
        return workoutCoachRepository.isCoachAssignedToWorkout(coachId, workoutId);
    }

    public List<Workout> getAvailableWorkoutsForCoach(int coachId) throws SQLException {
        List<Workout> allWorkouts = workoutsRepository.findAll();
        return allWorkouts.stream()
                .filter(workout -> {
                    try {
                        return !isCoachAssignedToWorkout(workout.getId(), coachId);
                    } catch (SQLException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public CoachAssignmentDto getCoachAssignmentDto(int coachId) throws SQLException {
        Optional<Coach> coach = coachesRepository.findById(coachId);
        if (!coach.isPresent()) {
            throw new IllegalArgumentException("Coach not found");
        }
        List<Workout> assignedWorkouts = getWorkoutsForCoach(coachId);
        List<Workout> availableWorkouts = getAvailableWorkoutsForCoach(coachId);
        return new CoachAssignmentDto(coach.get(), assignedWorkouts, availableWorkouts);

    }
}
