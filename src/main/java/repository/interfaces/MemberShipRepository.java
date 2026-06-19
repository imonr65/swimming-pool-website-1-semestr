package repository.interfaces;

import models.MemberShip;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberShipRepository extends CrudRepository<MemberShip> {

    Optional<MemberShip> findById(Integer id) throws SQLException;

    List<MemberShip> findByUserId(UUID userId) throws SQLException;

    boolean existsByUserAndWorkout(UUID userId, Integer workoutId) throws SQLException;

    void removeById(Integer id) throws SQLException;
}
