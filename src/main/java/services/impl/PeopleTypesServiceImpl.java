package services.impl;

import models.PeopleType;
import repository.interfaces.PeopleTypeRepository;
import services.interfaces.PeopleTypesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PeopleTypesServiceImpl implements PeopleTypesService {

    private final PeopleTypeRepository peopleTypeRepository;

    public PeopleTypesServiceImpl(PeopleTypeRepository peopleTypeRepository) {
        this.peopleTypeRepository = peopleTypeRepository;
    }

    @Override
    public List<PeopleType> getAllPeopleTypes() throws SQLException {
        return peopleTypeRepository.findAll();
    }

    @Override
    public void save(PeopleType peopleType) throws SQLException {
        peopleTypeRepository.save(peopleType);
    }

    @Override
    public Optional<PeopleType> getPeopleTypeById(int id) throws SQLException {
        return peopleTypeRepository.findById(id);
    }

}
