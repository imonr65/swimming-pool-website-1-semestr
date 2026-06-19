package servlets;

import models.*;
import services.impl.WorkoutCoachService;
import services.interfaces.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/services")
public class ServicesServlet extends HttpServlet {

    private WorkoutsService workoutsService;
    private CategoriesService categoriesService;
    private LevelService levelService;
    private PeopleTypesService peopleTypesService;
    private WorkoutCoachService workoutCoachService;
    private PricingInterface pricingService;
    private MemberShipService memberShipService;
    private BookingService bookingService;
    private ScheduleService scheduleService;
    private CoachesService coachesService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Category> categories = categoriesService.getAllCategories();
            List<Level> levels = levelService.getAllLevels();
            List<PeopleType> types = peopleTypesService.getAllPeopleTypes();

            req.setAttribute("categories", categories);
            req.setAttribute("peopleTypes", types);
            req.setAttribute("levels", levels);

            String categoryId = req.getParameter("categoryId");
            String peopleId = req.getParameter("peopleId");
            String levelId = req.getParameter("levelId");

            List<Workout> workouts;
            if (categoryId != null || peopleId != null || levelId != null) {
                workouts = workoutsService.getWorkoutsByFilters(
                        parseInteger(categoryId),
                        parseInteger(peopleId),
                        parseInteger(levelId)
                );
            } else {
                workouts = workoutsService.getAllWorkouts();
            }

            for (Workout workout : workouts) {
                List<Schedule> schedules = scheduleService.findByWorkoutId(workout.getId());
                req.setAttribute("schedules_" + workout.getId(), schedules);
            }

            req.setAttribute("workouts", workouts);
            req.getRequestDispatcher("/jsp/services.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Ошибка загрузки услуг");
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("currentUser");

        switch (action) {
            case "addToMembership":
                String workoutIdStr = req.getParameter("workoutId");
                System.out.println("Received addToMembership request - workoutId: " + workoutIdStr);

                if (user == null) {
                    System.out.println("User not authenticated");
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Пользователь не авторизован\"}");
                    return;
                }

                UUID userId = user.getId();
                try {
                    if (workoutIdStr == null || workoutIdStr.trim().isEmpty()) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID тренировки не найден\"}");
                        return;
                    }

                    int workoutId = Integer.parseInt(workoutIdStr);
                    System.out.println("Processing workout ID: " + workoutId + " for user: " + userId);

                    Optional<Workout> workoutOpt = workoutsService.getWorkoutById(workoutId);
                    if (!workoutOpt.isPresent()) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Тренировка не найдена\"}");
                        return;
                    }

                    // Проверяем, есть ли уже абонемент
                    if (memberShipService.hasUserMembershipForWorkout(userId, workoutId)) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"У вас уже есть абонемент на эту тренировку\"}");
                        return;
                    }

                    Workout workout = workoutOpt.get();
                    System.out.println("Workout found: " + workout.getDescription());

                    // Получаем pricing
                    Pricing pricing = pricingService.findPricingByCategoryAndPeopleType(
                            workout.getCategory().getId(),
                            workout.getPeopleType().getId()
                    ).orElse(null);

                    if (pricing == null) {
                        System.out.println("Pricing not found for category: " + workout.getCategory().getId() +
                                " and people type: " + workout.getPeopleType().getId());
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Не найдена цена для данной тренировки\"}");
                        return;
                    }

                    System.out.println("Pricing found: " + pricing.getId());

                    // Создаем абонемент
                    MemberShip memberShip = MemberShip.builder()
                            .userId(userId)
                            .workoutId(workoutId)
                            .start_date(LocalDate.now())
                            .end_date(LocalDate.now().plusMonths(1))
                            .total_visit(8)
                            .current_amount_visit(0)
                            .pricing(pricing)
                            .build();

                    memberShipService.save(memberShip);
                    System.out.println("Membership saved successfully");

                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"status\":\"success\", \"message\":\"Абонемент успешно добавлен\"}");

                } catch (NumberFormatException e) {
                    System.out.println("Invalid workout ID format: " + workoutIdStr);
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Неверный формат ID тренировки\"}");
                } catch (SQLException e) {
                    System.out.println("SQL error: " + e.getMessage());
                    e.printStackTrace();
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Ошибка базы данных: " + e.getMessage() + "\"}");
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                    e.printStackTrace();
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Неожиданная ошибка: " + e.getMessage() + "\"}");
                }
                break;
            case "assignCoach":
                try {
                    String workoutIdS = req.getParameter("workoutId");
                    String coachIdS = req.getParameter("coachId");

                    if (workoutIdS == null || coachIdS == null) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID не найден\"}");
                        return;
                    }

                    int workoutId = Integer.parseInt(workoutIdS);
                    int coachId = Integer.parseInt(coachIdS);

                    workoutCoachService.assignCoachToWorkout(workoutId, coachId);

                    resp.getWriter().write("{\"status\":\"success\", \"message\":\"Тренер успешно назначен\"}");
                } catch (Exception e) {
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID не найден\"}");
                }
                break;
            case "bookWorkout":
                String scheduleIdStr = req.getParameter("scheduleId");
                String memberIdStr = req.getParameter("memberId");
                if (user == null) {
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Пользователь не авторизован\"}");
                    return;
                }

                if (scheduleIdStr == null || memberIdStr == null) {
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"ID расписания и абонемента обязательны\"}");
                    return;
                }

                try {
                    Integer scheduleId = parseInteger(scheduleIdStr);
                    Integer memberId = parseInteger(memberIdStr);
                    UUID uId = user.getId();

                    if (bookingService.hasUserBookedSchedule(uId, scheduleId)) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Вы уже забронировали это расписание\"}");
                        return;
                    }

                    Schedule schedule = scheduleService.findById(scheduleId);
                    if (schedule == null) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Расписание не найдено\"}");
                        return;
                    }

                    MemberShip memberShip = memberShipService.findById(memberId);
                    if (memberShip == null || !memberShip.getUserId().equals(uId)) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Абонемент не найден или не принадлежит вам\"}");
                        return;
                    }

                    if (memberShip.getCurrent_amount_visit() >= memberShip.getTotal_visit()) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Лимит посещений по абонементу исчерпан\"}");
                        return;
                    }

                    if (schedule.getCurrentParticipants() >= schedule.getMaxParticipants()) {
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Нет свободных мест на это время\"}");
                        return;
                    }

                    Booking booking = Booking.builder()
                            .userId(uId)
                            .scheduleId(scheduleId)
                            .membershipId(memberId)
                            .priceId(memberShip.getPricing().getId())
                            .build();
                    bookingService.saveBooking(booking);
                    scheduleService.incrementParticipants(scheduleId);
                    memberShipService.incrementVisitCount(memberId);
                    resp.getWriter().write("{\"status\":\"success\", \"message\":\"Тренировка успешно забронирована\"}");

                } catch (SQLException e) {
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"Ошибка при бронировании: " + e.getMessage() + "\"}");
                } catch (IllegalArgumentException e) {
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        workoutsService = (WorkoutsService) config.getServletContext().getAttribute("workoutsService");
        categoriesService = (CategoriesService) config.getServletContext().getAttribute("categoriesService");
        levelService = (LevelService) config.getServletContext().getAttribute("levelService");
        peopleTypesService = (PeopleTypesService) config.getServletContext().getAttribute("peopleTypesService");
        workoutCoachService = (WorkoutCoachService) config.getServletContext().getAttribute("workoutCoachService");
        pricingService = (PricingInterface) config.getServletContext().getAttribute("pricingService");
        memberShipService = (MemberShipService) config.getServletContext().getAttribute("memberShipService");
        bookingService = (BookingService) config.getServletContext().getAttribute("bookingService");
        scheduleService = (ScheduleService) config.getServletContext().getAttribute("scheduleService");
        coachesService = (CoachesService) config.getServletContext().getAttribute("coachesService");
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}