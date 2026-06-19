package repository.impl.modelsRepository;

import models.MemberShip;
import models.Pricing;
import repository.interfaces.MemberShipRepository;
import repository.interfaces.PricingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemberShipRepositoryImpl implements MemberShipRepository {

    private final Connection connection;

    private final PricingRepository pricingRepository;

    private static final String SQL_INSERT = "insert into memberships" +
            "(user_id, workout_id, start_date, end_date, visit_total, current_amount_visit, price_id)" +
            "values (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID = "select id, user_id, workout_id, start_date, end_date, visit_total, current_amount_visit, price_id" +
            " from memberships where id = ?";

    private static final String SQL_SELECT_BY_USER_ID = "select id, user_id, workout_id, start_date, end_date, visit_total, current_amount_visit, price_id" +
            " from memberships where user_id = ?";
    private static final String SQL_UPDATE = "update memberships set user_id = ?, workout_id = ?, start_date = ?, end_date = ?, visit_total = ?, current_amount_visit = ?, price_id = ?" +
            " where id = ?";
    private static final String SQL_DELETE = "delete from memberships where id = ?";

    private static final String SQL_CHECK_EXISTS = "select 1 from memberships where user_id = ? and workout_id = ?";


    public MemberShipRepositoryImpl(Connection connection, PricingRepository pricingRepository) {
        this.connection = connection;
        this.pricingRepository = pricingRepository;
    }

    @Override
    public Optional<MemberShip> findById(Integer id) throws SQLException {
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
    public void save(MemberShip entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        statement.setObject(1, entity.getUserId());
        statement.setInt(2, entity.getWorkoutId());
        statement.setDate(3, Date.valueOf(entity.getStart_date()));
        statement.setDate(4, Date.valueOf(entity.getEnd_date()));
        statement.setInt(5, entity.getTotal_visit());
        statement.setInt(6, entity.getCurrent_amount_visit());
        statement.setInt(7, entity.getPricing().getId());
        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void update(MemberShip membership) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setObject(1, membership.getUserId());
        statement.setInt(2, membership.getWorkoutId());
        statement.setDate(3, Date.valueOf(membership.getStart_date()));
        statement.setDate(4, Date.valueOf(membership.getEnd_date()));
        statement.setInt(5, membership.getTotal_visit());
        statement.setInt(6, membership.getCurrent_amount_visit());
        statement.setInt(7, membership.getPricing().getId());
        statement.setInt(8, membership.getId());
        statement.executeUpdate();
    }


    @Override
    public void remove(MemberShip entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public List<MemberShip> findByUserId(UUID userId) throws SQLException {
        List<MemberShip> memberShips = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_USER_ID);
        statement.setObject(1, userId);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                memberShips.add(fillForm(resultSet));
            }
        }
        return memberShips;
    }

    @Override
    public boolean existsByUserAndWorkout(UUID userId, Integer workoutId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_CHECK_EXISTS);
        statement.setObject(1, userId);
        statement.setInt(2, workoutId);
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

    private MemberShip fillForm(ResultSet resultSet) throws SQLException {

        Pricing pricing = pricingRepository.findById(resultSet.getInt("price_id")).orElseThrow(() -> new IllegalArgumentException("нет такой цены"));


        return MemberShip.builder()
                .id(resultSet.getInt("id"))
                .userId((UUID) resultSet.getObject("user_id"))
                .workoutId(resultSet.getInt("workout_id"))
                .start_date(resultSet.getDate("start_date").toLocalDate())
                .end_date(resultSet.getDate("end_date").toLocalDate())
                .total_visit(resultSet.getInt("visit_total"))
                .current_amount_visit(resultSet.getInt("current_amount_visit"))
                .pricing(pricing)
                .build();
    }
}
