package servlets;

import models.Coach;
import models.Schedule;
import models.User;
import models.Workout;
import services.impl.ScheduleServiceImpl;
import services.impl.WorkoutCoachService;
import services.interfaces.CoachesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/coach-schedule")
public class CoachSchedule extends HttpServlet {

    private ScheduleServiceImpl scheduleService;
    private WorkoutCoachService workoutCoachService;
    private CoachesService coachesService;

    @Override
    public void init() throws ServletException {
        scheduleService = (ScheduleServiceImpl) getServletContext().getAttribute("scheduleService");
        workoutCoachService = (WorkoutCoachService) getServletContext().getAttribute("workoutCoachService");
        coachesService = (CoachesService) getServletContext().getAttribute("coachesService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("/signIn");
            return;
        }

        Coach coach;
        try {
            coach = coachesService.findCoachByUserId(user.getId())
                    .orElseThrow(() -> new ServletException("Coach not found"));
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

        Integer coachId = coach.getId();

        try {
            List<Workout> coachWorkouts = workoutCoachService.getWorkoutsForCoach(coachId);
            List<Schedule> schedules = scheduleService.findByCoachID(coachId);

            request.setAttribute("coachWorkouts", coachWorkouts);
            request.setAttribute("schedules", schedules);
            request.setAttribute("coach", coach);

            request.getRequestDispatcher("/jsp/coach-schedule.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("signIn");
            return;
        }

        Coach coach;
        try {
            coach = coachesService.findCoachByUserId(user.getId())
                    .orElseThrow(() -> new ServletException("Coach not found"));
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

        Integer coachId = coach.getId();

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String scheduleIdParam = request.getParameter("scheduleId");
            try {
                int scheduleId = Integer.parseInt(scheduleIdParam);
                scheduleService.deleteSchedule(scheduleId);
                session.setAttribute("message", "Schedule deleted successfully!");
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid schedule ID");
            } catch (SQLException e) {
                session.setAttribute("error", "Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                session.setAttribute("error", e.getMessage());
            }
            response.sendRedirect("/coach-schedule");
            return;
        }

        String workoutIdParam = request.getParameter("workoutId");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String maxParticipantsStr = request.getParameter("maxParticipants");

        try {
            int workoutId = Integer.parseInt(workoutIdParam);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
            int maxParticipants = Integer.parseInt(maxParticipantsStr);

            Schedule schedule = Schedule.builder()
                    .coachId(coachId)
                    .workoutId(workoutId)
                    .startTime(startTime)
                    .endTime(endTime)
                    .maxParticipants(maxParticipants)
                    .currentParticipants(0)
                    .build();

            scheduleService.save(schedule);

            session.setAttribute("message", "Schedule created successfully!");
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid input format");
        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect("/coach-schedule");
    }
}