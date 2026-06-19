package services.interfaces;

import dto.SignInForm;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public interface SignInService {
    boolean signIn(SignInForm signInForm, HttpSession session) throws SQLException;
}
