package servlets.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CoachDto;
import models.User;
import services.interfaces.CoachesService;
import services.interfaces.UsersService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/trainers")
public class AdminTrainersServlet extends HttpServlet {
    private CoachesService coachesService;
    private UsersService usersService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        coachesService = (CoachesService) config.getServletContext().getAttribute("coachesService");
        usersService = (UsersService) config.getServletContext().getAttribute("usersService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("activeTab", "trainers");
            req.getRequestDispatcher("/jsp/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading trainers page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("delete".equals(req.getParameter("action"))) {
            try {
                String idParam = req.getParameter("id");
                if (idParam == null || idParam.trim().isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"status\": \"error\", \"message\": \"Trainer ID is required\"}");
                    return;
                }

                int trainerId = Integer.parseInt(idParam);
                coachesService.remove(trainerId);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"status\": \"success\", \"message\": \"Trainer deleted successfully\"}");

            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid trainer ID format\"}");
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to delete trainer: " + e.getMessage() + "\"}");
            }
        } else {
            try {
                CoachDto coachDto = objectMapper.readValue(req.getReader(), CoachDto.class);

                if (coachDto.getName() == null || coachDto.getEmail() == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Name and email are required\"}");
                    return;
                }

                User user = usersService.getUserByEmail(coachDto.getEmail());
                if (user == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"User with this email not found\"}");
                    return;
                }

                coachDto.setUser(user);
                coachesService.save(coachDto);

                resp.getWriter().write("{\"message\": \"Trainer added successfully\"}");

            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Unexpected error: " + e.getMessage());
            }
        }
    }
}
