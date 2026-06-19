package services.impl;

import dto.UserDto;
import dto.UserProfileDto;
import models.Role;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.interfaces.RoleRepository;
import repository.interfaces.UsersRepository;
import services.interfaces.UsersService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return usersRepository.findAll();
    }

    @Override
    public void addUser(UserDto form) throws SQLException {
        Role role = roleRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .phone(form.getPhone())
                .role(role)
                .build();
        usersRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        return usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    @Override
    public UserProfileDto getUserProfileByEmail(String email) throws SQLException {
        User user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserProfileDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .photoId(user.getPhotoId())
                .build();
    }

    @Override
    public void updateUserProfile(UserProfileDto form) throws SQLException {
        User user = usersRepository.findByEmail(form.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (form.getFirstName() != null || !form.getFirstName().isEmpty()) {
            user.setFirstName(form.getFirstName());
        } else {
            user.setFirstName(user.getFirstName());
        }
        if (form.getLastName() != null || !form.getLastName().isEmpty()) {
            user.setLastName(form.getLastName());
        } else {
            user.setLastName(user.getLastName());
        }
        if (form.getPhone() != null || !form.getPhone().isEmpty()) {
            user.setPhone(form.getPhone());
        } else {
            user.setPhone(user.getPhone());
        }
        usersRepository.update(user);
    }

    @Override
    public void updateUserPhoto(String email, Long photoId) throws SQLException {
        usersRepository.updatePhoto(email, photoId);
    }

    @Override
    public void updateRole(String email, int roleId) throws SQLException {
        usersRepository.updateRole(email, roleId);
    }
}
