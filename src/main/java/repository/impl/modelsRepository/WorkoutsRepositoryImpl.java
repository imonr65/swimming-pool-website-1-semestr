package repository.impl.modelsRepository;

import models.Category;
import models.Level;
import models.PeopleType;
import models.Workout;
import repository.interfaces.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class WorkoutsRepositoryImpl implements WorkoutsRepository {

    private final Connection connection;

    private static final String SQL_INSERT = "insert into " +
            "workouts(description, type_id, category_id, level_id, has_coach) " +
            "values (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_WORKOUTS = """
            select w.*, pt.type_name, c.category_name, l.level_name
            from workouts w
            left join peolple_types pt ON w.type_id = pt.id
            left join categories c ON w.category_id = c.id
            left join levels l ON w.level_id = l.id
            """;

    private static String SQL_SELECT_BY_ID = "select id, description, type_id, category_id, level_id, has_coach from workouts where id = ?";

    private static final String SQL_DELETE = "delete from workouts where id = ?";

    private final PeopleTypeRepository peopleTypeRepository;

    private final CategoriesRepository categoriesRepository;

    private final WorkoutCoachRepository workoutCoachRepository;

    private final LevelInterface levelRepository;

    public WorkoutsRepositoryImpl(
            Connection connection, PeopleTypeRepository peopleTypeRepository, CategoriesRepository categoriesRepository,
            WorkoutCoachRepository workoutCoachRepository, LevelInterface levelRepository
    ) {
        this.connection = connection;
        this.peopleTypeRepository = peopleTypeRepository;
        this.categoriesRepository = categoriesRepository;
        this.workoutCoachRepository = workoutCoachRepository;
        this.levelRepository = levelRepository;
    }

    @Override
    public List<Workout> findAll() throws SQLException {
        List<Workout> workouts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_WORKOUTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                workouts.add(fillForm(resultSet));
            }
        }
        return workouts;
    }


    @Override
    public List<Workout> findByFilters(Integer categoryId, Integer peopleId, Integer levelId) throws SQLException {
        List<Workout> workouts = new ArrayList<>();
        StringBuilder query = new StringBuilder(SQL_SELECT_WORKOUTS);
        List<Object> parameters = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (categoryId != null) {
            conditions.add("w.category_id = ?");
            parameters.add(categoryId);
        }
        if (peopleId != null) {
            conditions.add("w.type_id = ?");
            parameters.add(peopleId);
        }
        if (levelId != null) {
            conditions.add("w.level_id = ?");
            parameters.add(levelId);
        }

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    workouts.add(fillForm(resultSet));
                }
            }
        }
        return workouts;
    }

    @Override
    public Optional<Workout> findById(Integer id) throws SQLException {
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
    public void save(Workout entity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getDescription());
            statement.setLong(2, entity.getPeopleType().getId());
            statement.setLong(3, entity.getCategory().getId());
            statement.setLong(4, entity.getLevel().getId());
            statement.setBoolean(5, entity.isHasCoach());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Workout entity) throws SQLException {

    }

    @Override
    public void remove(Workout entity) throws SQLException {

    }

    public void removeById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public List<Workout> findByCoachId(int coachId) throws SQLException {
        List<Workout> workouts = new ArrayList<>();
        List<Integer> workoutIds = workoutCoachRepository.findWorkoutIdsByCoachId(coachId);

        for (Integer workoutId : workoutIds) {
            Optional<Workout> workout = findById(workoutId);
            workout.ifPresent(workouts::add);
        }
        return workouts;
    }

    private Workout fillForm(ResultSet resultSet) throws SQLException {
        PeopleType peopleType = peopleTypeRepository.findById(resultSet.getInt("type_id")).orElse(null);

        Category category = categoriesRepository.findById(resultSet.getInt("category_id")).orElse(null);

        Level level = levelRepository.findById(resultSet.getInt("level_id")).orElse(null);
        Workout workout = Workout.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .hasCoach(resultSet.getBoolean("has_coach"))
                .peopleType(peopleType)
                .category(category)
                .level(level)
                .build();
        return workout;
    }
}
