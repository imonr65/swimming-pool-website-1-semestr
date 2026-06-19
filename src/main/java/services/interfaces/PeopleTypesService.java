package services.interfaces;

import models.PeopleType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PeopleTypesService {

    List<PeopleType> getAllPeopleTypes() throws SQLException;

    void save(PeopleType peopleType) throws SQLException;

    Optional<PeopleType> getPeopleTypeById(int id) throws SQLException;
}
