package services.interfaces;

import dto.CoachDto;
import models.Coach;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoachesService {
    void save(CoachDto coach) throws SQLException;

    Optional<Coach> findByCoachName(String firstName, String lastName) throws SQLException;

    Optional<Coach> findById(Integer id) throws SQLException;

    List<Coach> getAllCoaches() throws SQLException;

    void remove(Integer id) throws SQLException;

    void update(Coach coach) throws SQLException;

    Optional<Coach> findCoachByUserId(UUID userId) throws SQLException;
}
