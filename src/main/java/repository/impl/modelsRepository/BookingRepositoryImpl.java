package repository.impl.modelsRepository;

import models.Booking;
import repository.interfaces.BookingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingRepositoryImpl implements BookingRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO booking (user_id, schedules_id, membership_id, price_id) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID = "SELECT id, user_id, schedules_id, membership_id, price_id " +
            "FROM booking WHERE id = ?";

    private static final String SQL_SELECT_BY_USER_ID = "SELECT id, user_id, schedules_id, membership_id, price_id " +
            "FROM booking WHERE user_id = ?";

    private static final String SQL_SELECT_BY_SCHEDULE_ID = "SELECT id, user_id, schedules_id, membership_id, price_id " +
            "FROM booking WHERE schedules_id = ?";

    private static final String SQL_CHECK_EXISTS = "SELECT 1 FROM booking " +
            "WHERE user_id = ? AND schedules_id = ?";

    private static final String SQL_DELETE = "DELETE FROM booking WHERE id = ?";

    public BookingRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Booking> findByBookingId(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setInt(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(fillForm(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Booking> findByUserId(UUID userId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_USER_ID);
        statement.setObject(1, userId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookings.add(fillForm(resultSet));
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> findByScheduleId(Integer scheduleId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_SCHEDULE_ID);
        statement.setInt(1, scheduleId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookings.add(fillForm(resultSet));
            }
        }
        return bookings;
    }

    @Override
    public boolean existsByUserAndSchedule(UUID userId, Integer scheduleId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_CHECK_EXISTS);
        statement.setObject(1, userId);
        statement.setInt(2, scheduleId);
        try (ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public Optional<Booking> findById(Integer id) throws SQLException {
        return findByBookingId(id);
    }

    @Override
    public void save(Booking entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        ps.setObject(1, entity.getUserId());
        ps.setInt(2, entity.getScheduleId());
        ps.setInt(3, entity.getMembershipId());
        ps.setInt(4, entity.getPriceId());
        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void update(Booking entity) throws SQLException {
    }

    @Override
    public void remove(Booking entity) throws SQLException {
        delete(entity.getId());
    }

    private Booking fillForm(ResultSet resultSet) throws SQLException {
        return Booking.builder()
                .id(resultSet.getInt("id"))
                .userId((UUID) resultSet.getObject("user_id"))
                .membershipId(resultSet.getInt("membership_id"))
                .priceId(resultSet.getInt("price_id"))
                .scheduleId(resultSet.getInt("schedules_id"))
                .build();
    }
}