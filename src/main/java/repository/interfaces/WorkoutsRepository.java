package repository.interfaces;

import models.Workout;

import java.sql.SQLException;
import java.util.List;

public interface WorkoutsRepository extends CrudRepository<Workout> {

    List<Workout> findAll() throws SQLException;


    List<Workout> findByFilters(Integer categoryId, Integer peopleId, Integer levelId) throws SQLException;

    void removeById(int id) throws SQLException;

    List<Workout> findByCoachId(int coachId) throws SQLException;
}
