package services.impl;

import dto.SignInForm;
import models.Role;
import models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.interfaces.UsersRepository;
import services.interfaces.SignInService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Optional;


public class SignInServiceImpl implements SignInService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;


    public SignInServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean signIn(SignInForm signInForm, HttpSession session) throws SQLException {
        String userEmail = signInForm.getUserEmail();
        Optional<User> user = usersRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            if (passwordEncoder.matches(signInForm.getPassword(), user.get().getPassword())) {
                session.setAttribute("authenticated", true);
                Role role = user.get().getRole();
                session.setAttribute("userRole", role);
                session.setAttribute("userEmail", userEmail);
                session.setAttribute("currentUser", user.get());
                return true;
            } else {
                return false;
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
