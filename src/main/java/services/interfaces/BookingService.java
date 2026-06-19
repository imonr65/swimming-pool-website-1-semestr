package services.interfaces;

import models.Booking;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface BookingService {
    void saveBooking(Booking booking) throws SQLException;

    Booking getBooking(int id) throws SQLException;

    List<Booking> getBookingsByUser(UUID userId) throws SQLException;

    List<Booking> getBookingsBySchedule(Integer scheduleId) throws SQLException;

    boolean hasUserBookedSchedule(UUID userId, Integer scheduleId) throws SQLException;

    void cancelBooking(Integer bookingId) throws SQLException;

}
