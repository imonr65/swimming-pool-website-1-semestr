package repository.interfaces;

import models.Booking;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends CrudRepository<Booking> {
    Optional<Booking> findByBookingId(Integer id) throws SQLException;

    List<Booking> findByUserId(UUID userId) throws SQLException;

    List<Booking> findByScheduleId(Integer scheduleId) throws SQLException;

    boolean existsByUserAndSchedule(UUID userId, Integer scheduleId) throws SQLException;

    void delete(Integer id) throws SQLException;
}
