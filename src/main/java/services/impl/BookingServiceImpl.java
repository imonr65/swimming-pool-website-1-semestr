package services.impl;

import models.Booking;
import repository.interfaces.BookingRepository;
import services.interfaces.BookingService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void saveBooking(Booking booking) throws SQLException {
        if (hasUserBookedSchedule(booking.getUserId(), booking.getScheduleId())) {
            throw new IllegalArgumentException("Пользователь уже забронировал это расписание");
        }
        bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(int id) throws SQLException {
        return bookingRepository.findByBookingId(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));

    }

    @Override
    public List<Booking> getBookingsByUser(UUID userId) throws SQLException {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingsBySchedule(Integer scheduleId) throws SQLException {
        return bookingRepository.findByScheduleId(scheduleId);
    }

    @Override
    public boolean hasUserBookedSchedule(UUID userId, Integer scheduleId) throws SQLException {
        return bookingRepository.existsByUserAndSchedule(userId, scheduleId);
    }

    @Override
    public void cancelBooking(Integer bookingId) throws SQLException {
        bookingRepository.delete(bookingId);
    }
}
