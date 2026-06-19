package servlets;

import models.Coach;
import models.User;
import services.impl.WorkoutCoachService;
import services.impl.WorkoutsServiceImpl;
import services.interfaces.CoachesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/add-workout-to-coach")
public class AddWorkoutToCoachServlet extends HttpServlet {

    private WorkoutsServiceImpl workoutsService;
    private WorkoutCoachService workoutCoachService;
    private CoachesService coachesService;

    @Override
    public void init() throws ServletException {
        workoutsService = (WorkoutsServiceImpl) getServletContext().getAttribute("workoutsService");
        workoutCoachService = (WorkoutCoachService) getServletContext().getAttribute("workoutCoachService");
        coachesService = (CoachesService) getServletContext().getAttribute("coachesService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        Coach coach;
        try {
            coach = coachesService.findCoachByUserId(user.getId()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Integer coachId = coach.getId();

        if (coachId == null) {
            response.sendRedirect("signIn");
            return;
        }

        try {
            var allWorkouts = workoutsService.getAllWorkouts();
            request.setAttribute("workouts", allWorkouts);

            request.getRequestDispatcher("/jsp/select-workout.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        Coach coach;
        try {
            coach = coachesService.findCoachByUserId(user.getId()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Integer coachId = coach.getId();


        String workoutIdParam = request.getParameter("workoutId");

        try {
            int workoutId = Integer.parseInt(workoutIdParam);

            workoutCoachService.assignCoachToWorkout(workoutId, coachId);

            session.setAttribute("message", "Workout successfully added to your profile!");
            response.sendRedirect("/add-workout-to-coach");

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid workout ID");
            response.sendRedirect("/add-workout-to-coach");
        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
            response.sendRedirect("/add-workout-to-coach");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("/add-workout-to-coach");
        }
    }
}