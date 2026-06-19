package servlets;

import dto.SignInForm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import services.interfaces.SignInService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {

    private SignInService signInService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        signInService = (SignInService) config.getServletContext().getAttribute("signInService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/signIn.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        SignInForm signInForm = SignInForm.builder()
                .userEmail(email)
                .password(password)
                .build();

        HttpSession httpSession = req.getSession();

        try {
            signInService.signIn(signInForm, httpSession);
            resp.sendRedirect("/");
        } catch (UsernameNotFoundException | SQLException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/jsp/signIn.jsp").forward(req, resp);
        }

    }
}
