package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.Workout;
import services.interfaces.WorkoutsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/services/filter")
public class WorkoutFilterServlet extends HttpServlet {
    private WorkoutsService workoutsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Integer categoryId = parseInteger(req.getParameter("categoryId"));
            Integer peopleId = parseInteger(req.getParameter("peopleId"));
            Integer levelId = parseInteger(req.getParameter("levelId"));


            List<Workout> workouts = workoutsService.getWorkoutsByFilters(categoryId, peopleId, levelId);


            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            String json = objectMapper.writeValueAsString(workouts);
            resp.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Ошибка фильтрации: " + e.getMessage() + "\"}");
        }
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        workoutsService = (WorkoutsService) config.getServletContext().getAttribute("workoutsService");
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty() || "null".equals(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
