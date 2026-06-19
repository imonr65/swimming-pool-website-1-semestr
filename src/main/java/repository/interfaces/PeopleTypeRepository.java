package repository.interfaces;

import models.PeopleType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PeopleTypeRepository extends CrudRepository<PeopleType> {
    Optional<PeopleType> findByName(String name) throws SQLException;

    List<PeopleType> findAll() throws SQLException;
}
