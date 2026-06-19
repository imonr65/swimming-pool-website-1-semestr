package repository.impl.modelsRepository;

import models.Coach;
import models.User;
import repository.interfaces.CoachesRepository;
import repository.interfaces.UsersRepository;
import repository.interfaces.WorkoutCoachRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CoachesRepositoryImpl implements CoachesRepository {

    private final Connection connection;

    private static final String SQL_INSERT_INTO_COACHES = "insert into coaches(user_id, experience, description, specialization, biography, rating)" +
            " values(?,?,?,?,?,?)";
    private static final String SQL_SELECT_FROM_COACHES_BY_NAME = "select " +
            "c.id as coach_id, c.user_id, c.experience, c.description, c.specialization, c.biography, c.rating, " +
            "u.first_name, u.last_name, u.email, u.phone, u.photo_id " +
            "from coaches c inner join users u on c.user_id = u.id " +
            "where u.first_name = ? and u.last_name = ?";

    private static final String SQL_SELECT_BY_ID = "select " +
            "c.id as coach_id, c.user_id, c.experience, c.description, c.specialization, c.biography, c.rating, " +  // ← ДОБАВЛЕНА ЗАПЯТАЯ И ПРОБЕЛ
            "u.first_name, u.last_name, u.email, u.phone, u.photo_id " +
            "from coaches c inner join users u on c.user_id = u.id " +
            "where c.id = ?";
    private static final String SQL_SELECT_BY_USER_ID =
            "SELECT c.id as coach_id, c.user_id, c.experience, c.description, " +
                    "c.specialization, c.biography, c.rating, " +
                    "u.first_name, u.last_name, u.email, u.phone, u.photo_id " +
                    "FROM coaches c INNER JOIN users u ON c.user_id = u.id " +
                    "WHERE c.user_id = ?";

    private static final String SQL_SELECT_ALL = "select * from coaches";

    private static final String SQL_DELETE_FROM_COACHES = "delete from coaches where id = ?";

    private final WorkoutCoachRepository workoutCoachRepository;

    private final UsersRepository usersRepository;

    public CoachesRepositoryImpl(Connection connection, UsersRepository usersRepository, WorkoutCoachRepository workoutCoachRepository) {
        this.connection = connection;
        this.usersRepository = usersRepository;
        this.workoutCoachRepository = workoutCoachRepository;
    }

    @Override
    public Optional<Coach> findByCoachName(String firstName, String lastName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_COACHES_BY_NAME);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                User user = User.builder()
                        .id(UUID.fromString(resultSet.getString("user_id")))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .phone(resultSet.getString("phone"))
                        .photoId(resultSet.getLong(("photo_id")))
                        .build();

                Coach coach = Coach.builder()
                        .id(resultSet.getInt("coach_id"))
                        .user(user)
                        .biography(resultSet.getString("biography"))
                        .description(resultSet.getString("description"))
                        .experience(resultSet.getInt("experience"))
                        .rating(resultSet.getDouble("rating"))
                        .specialty(resultSet.getString("specialization"))
                        .build();
                return Optional.of(coach);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Coach> getAllCoaches() throws SQLException {
        List<Coach> coaches = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                coaches.add(fillForm(resultSet));
            }
        }
        return coaches;
    }

    @Override
    public void removeById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_FROM_COACHES);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public Optional<Coach> findById(Integer id) throws SQLException {
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
    public void save(Coach coach) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_COACHES, Statement.RETURN_GENERATED_KEYS);
        statement.setObject(1, coach.getUser().getId());
        statement.setInt(2, coach.getExperience());
        statement.setString(3, coach.getDescription());
        statement.setString(4, coach.getSpecialty());
        statement.setString(5, coach.getBiography());
        statement.setDouble(6, coach.getRating());
        statement.executeUpdate();
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                coach.setId(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void remove(Coach entity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_FROM_COACHES);
        statement.setInt(1, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public List<Coach> findWorkoutId(int workoutId) throws SQLException {
        List<Coach> coaches = new ArrayList<>();
        List<Integer> coachIds = workoutCoachRepository.findCoachIdsByWorkoutId(workoutId);

        for (Integer coachId : coachIds) {
            Optional<Coach> coach = findById(coachId);
            coach.ifPresent(coaches::add);
        }
        return coaches;
    }

    @Override
    public Optional<Coach> findByUserId(UUID userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_USER_ID);
        statement.setObject(1, userId);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                User user = User.builder()
                        .id(UUID.fromString(resultSet.getString("user_id")))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .phone(resultSet.getString("phone"))
                        .photoId(resultSet.getLong("photo_id"))
                        .build();

                Coach coach = Coach.builder()
                        .id(resultSet.getInt("coach_id"))
                        .user(user)
                        .biography(resultSet.getString("biography"))
                        .description(resultSet.getString("description"))
                        .experience(resultSet.getInt("experience"))
                        .rating(resultSet.getDouble("rating"))
                        .specialty(resultSet.getString("specialization"))
                        .build();
                return Optional.of(coach);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Coach entity) throws SQLException {

    }

    private Coach fillForm(ResultSet resultSet) throws SQLException {
        User user = usersRepository.findByUUID(UUID.fromString(resultSet.getString("user_id")));
        Coach coach = Coach.builder()
                .id(resultSet.getInt("coach_id"))
                .user(user)
                .biography(resultSet.getString("biography"))
                .description(resultSet.getString("description"))
                .experience(resultSet.getInt("experience"))
                .rating(resultSet.getDouble("rating"))
                .specialty(resultSet.getString("specialization"))
                .build();
        return coach;
    }
}
