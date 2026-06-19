package services.impl;

import models.Schedule;
import repository.interfaces.ScheduleRepository;
import services.interfaces.ScheduleService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleServiceImpl implements ScheduleService {

    private final  ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public void save(Schedule schedule) throws SQLException {
        validateSchedule(schedule);

        if (hasScheduleConflict(schedule.getCoachId(), schedule.getStartTime(), schedule.getEndTime())) {
            throw new IllegalArgumentException("Тренировка уже занята в течение этого времени. Пожалуйста, выберите другое время.");
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public void update(Schedule schedule) throws SQLException {
        validateSchedule(schedule);
        if (hasScheduleConflict(schedule.getCoachId(), schedule.getStartTime(), schedule.getEndTime())) {
            throw new IllegalArgumentException("Тренировка уже занята в течение этого времени. Пожалуйста, выберите другое время.");
        }
        scheduleRepository.update(schedule);
    }

    @Override
    public Schedule findById(int id) throws SQLException {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
    }

    @Override
    public List<Schedule> findByCoachID(Integer coachId) throws SQLException {
        return scheduleRepository.findByCoachId(coachId);
    }

    @Override
    public List<Schedule> findByWorkoutId(Integer workoutId) throws SQLException {
        return scheduleRepository.findByWorkoutId(workoutId);
    }

    @Override
    public List<Schedule> getAvailableSchedules(Integer workoutId, LocalDateTime start, LocalDateTime end) throws SQLException {
        return scheduleRepository.findAvailableSchedules(workoutId, start, end);
    }

    @Override
    public void deleteSchedule(Integer id) throws SQLException {
        scheduleRepository.removeById(id);
    }

    @Override
    public boolean hasScheduleConflict(Integer coachId, LocalDateTime start, LocalDateTime end) throws SQLException {
        return scheduleRepository.existsConflict(coachId, start, end);
    }

    @Override
    public void incrementParticipants(Integer scheduleId) throws SQLException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        if (schedule.getCurrentParticipants() < schedule.getMaxParticipants()) {
            schedule.setCurrentParticipants(schedule.getCurrentParticipants() + 1);
            scheduleRepository.update(schedule);
        } else {
            throw new IllegalStateException("Достигнут лимит количества участников");
        }

    }

    @Override
    public void decrementParticipants(Integer scheduleId) throws SQLException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        if (schedule.getMaxParticipants() > 0) {
            schedule.setCurrentParticipants(schedule.getCurrentParticipants() - 1);
            scheduleRepository.update(schedule);
        } else {
            throw new IllegalStateException("Нельзя уменьшить количество участников: значение уже 0");
        }
    }

    private void validateSchedule(Schedule schedule) {
        if (schedule.getStartTime() == null || schedule.getEndTime() == null) {
            throw new IllegalArgumentException("Время начала и окончания тренировки обязательны");
        }

        if (schedule.getEndTime().isBefore(schedule.getStartTime()) ||
                schedule.getEndTime().isEqual(schedule.getStartTime())) {
            throw new IllegalArgumentException("Время окончания тренировки должно быть позже времени начала");
        }

        if (schedule.getStartTime().plusMinutes(30).isAfter(schedule.getEndTime())) {
            throw new IllegalArgumentException("Минимальная продолжительность тренировки - 30 минут");
        }

        if (schedule.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Нельзя создавать тренировки в прошлом");
        }

        if (schedule.getMaxParticipants() <= 0) {
            throw new IllegalArgumentException("Максимальное количество участников должно быть положительным числом");
        }

        if (schedule.getCurrentParticipants() < 0) {
            throw new IllegalArgumentException("Текущее количество участников не может быть отрицательным");
        }

        if (schedule.getCurrentParticipants() > schedule.getMaxParticipants()) {
            throw new IllegalArgumentException("Текущее количество участников не может превышать максимальное");
        }
    }
}
