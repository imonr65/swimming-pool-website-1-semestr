package servlets;

import dto.UserDto;
import services.interfaces.SignUpService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    private SignUpService signUpService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto signUpForm = UserDto.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .phone(req.getParameter("phone"))
                .build();

        try {
            signUpService.signUp(signUpForm);
            resp.sendRedirect("/");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Произошла ошибка при регистрации. Попробуйте позже.");
            req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        signUpService = (SignUpService) config.getServletContext().getAttribute("signUpService");
    }
}
