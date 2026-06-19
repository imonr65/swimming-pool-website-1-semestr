package servlets.admins;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.WorkoutDto;
import models.Category;
import models.Level;
import models.PeopleType;
import models.Workout;
import services.interfaces.CategoriesService;
import services.interfaces.LevelService;
import services.interfaces.PeopleTypesService;
import services.interfaces.WorkoutsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/workouts")
public class AdminWorkoutsServlet extends HttpServlet {

    private WorkoutsService workoutsService;
    private CategoriesService categoriesService;
    private PeopleTypesService peopleTypesService;
    private LevelService levelService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Category> categories = categoriesService.getAllCategories();
            List<PeopleType> peopleTypes = peopleTypesService.getAllPeopleTypes();
            List<Level> levels = levelService.getAllLevels();

            req.setAttribute("categories", categories);
            req.setAttribute("peopleTypes", peopleTypes);
            req.setAttribute("levels", levels);
            req.setAttribute("activeTab", "workouts");
            req.getRequestDispatcher("/jsp/admin.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error loading workouts\"}");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            try {
                String idParameter = req.getParameter("id");
                if (idParameter == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Workout ID is required\"}");
                    return;
                }

                int workoutId = Integer.parseInt(idParameter);
                workoutsService.deleteWorkout(workoutId);

                resp.getWriter().write("{\"message\": \"Workout deleted successfully\"}");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid workout ID\"}");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                WorkoutDto workoutDto = objectMapper.readValue(req.getReader(), WorkoutDto.class);
                PeopleType peopleType = peopleTypesService.getPeopleTypeById(workoutDto.getPeopleTypeId()).orElseThrow(() -> new RuntimeException("Level not found"));
                Category category = categoriesService.getCategory(workoutDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Level not found"));
                Level level = levelService.getLevelById(workoutDto.getLevelId()).orElseThrow(() -> new RuntimeException("Level not found"));

                Workout workout = new Workout();
                workout.setDescription(workoutDto.getDescription());
                workout.setPeopleType(peopleType);
                workout.setCategory(category);
                workout.setLevel(level);
                workout.setHasCoach(workoutDto.isHasCoach());

                workoutsService.saveWorkout(workout);

                resp.getWriter().write("{\"message\": \"Workout added successfully\"}");
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Error saving workout\"}");
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        workoutsService = (WorkoutsService) config.getServletContext().getAttribute("workoutsService");
        categoriesService = (CategoriesService) config.getServletContext().getAttribute("categoriesService");
        peopleTypesService = (PeopleTypesService) config.getServletContext().getAttribute("peopleTypesService");
        levelService = (LevelService) config.getServletContext().getAttribute("levelService");

    }
}
