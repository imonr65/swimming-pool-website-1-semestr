package services.impl;


import dto.UserDto;
import models.Role;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.interfaces.RoleRepository;
import repository.interfaces.UsersRepository;
import services.interfaces.SignUpService;

import java.sql.SQLException;
import java.util.UUID;

public class SignUpServiceImpl implements SignUpService {

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public SignUpServiceImpl(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void signUp(UserDto form) throws SQLException {
        System.out.println(form.getEmail());
        if (usersRepository.findByEmail(form.getEmail()).isPresent()) {
            System.out.println(usersRepository.findByEmail(form.getEmail()).get().getEmail());
            throw new IllegalArgumentException("User with this email already exists");
        }
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Role not found"));

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
}
