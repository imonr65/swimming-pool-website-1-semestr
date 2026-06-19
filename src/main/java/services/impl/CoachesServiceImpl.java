package services.impl;

import dto.CoachDto;
import models.Coach;
import models.User;
import repository.interfaces.CoachesRepository;
import repository.interfaces.RoleRepository;
import services.interfaces.CoachesService;
import services.interfaces.UsersService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CoachesServiceImpl implements CoachesService {

    private final CoachesRepository coachesRepository;
    private final RoleRepository roleRepository;
    private final UsersService usersService;

    public CoachesServiceImpl(CoachesRepository coachesRepository, RoleRepository roleRepository, UsersService usersService) {
        this.coachesRepository = coachesRepository;
        this.roleRepository = roleRepository;
        this.usersService = usersService;
    }

    @Override
    public void save(CoachDto coachDto) throws SQLException {
        Coach coach = Coach.builder()
                .user(coachDto.getUser())
                .biography(coachDto.getBiography())
                .description(coachDto.getDescription())
                .experience(coachDto.getExperience())
                .rating(0.0)
                .specialty(coachDto.getSpecialization())
                .build();
        User user = coach.getUser();
        usersService.updateRole(user.getEmail(), 3);
        coachesRepository.save(coach);
    }

    @Override
    public Optional<Coach> findByCoachName(String firstName, String lastName) throws SQLException {
        return coachesRepository.findByCoachName(firstName, lastName);
    }

    @Override
    public Optional<Coach> findById(Integer id) throws SQLException {
        return coachesRepository.findById(id);
    }

    @Override
    public List<Coach> getAllCoaches() throws SQLException {
        return coachesRepository.getAllCoaches();
    }

    @Override
    public void remove(Integer id) throws SQLException {
        coachesRepository.removeById(id);
    }

    @Override
    public void update(Coach coach) throws SQLException {

    }

    @Override
    public Optional<Coach> findCoachByUserId(UUID userId) throws SQLException {
        return coachesRepository.findByUserId(userId);
    }
}
