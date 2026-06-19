<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Coach Schedule</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/coach-schedule.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Manage Your Schedule</h1>
        <p>Coach: ${coach.user.firstName} ${coach.user.lastName}</p>
    </header>

    <%-- Сообщения --%>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">
                ${sessionScope.message}
            <c:remove var="message" scope="session"/>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">
                ${sessionScope.error}
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>

    <%-- Форма создания расписания --%>
    <section class="schedule-form">
        <h2>Create New Schedule</h2>
        <form method="post" action="${pageContext.request.contextPath}/coach-schedule">
            <div class="form-group">
                <label for="workoutId">Workout:</label>
                <select id="workoutId" name="workoutId" required>
                    <option value="">Select a workout</option>
                    <c:forEach var="workout" items="${coachWorkouts}">
                        <option value="${workout.id}">${workout.description} (ID: ${workout.id})</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="startTime">Start Time:</label>
                <input type="datetime-local" id="startTime" name="startTime" required>
            </div>

            <div class="form-group">
                <label for="endTime">End Time:</label>
                <input type="datetime-local" id="endTime" name="endTime" required>
            </div>

            <div class="form-group">
                <label for="maxParticipants">Max Participants:</label>
                <input type="number" id="maxParticipants" name="maxParticipants" min="1" required>
            </div>

            <button type="submit" class="btn btn-primary">Create Schedule</button>
        </form>
    </section>

    <%-- Список существующих расписаний --%>
    <section class="schedule-list">
        <h2>Your Schedules</h2>
        <c:choose>
            <c:when test="${not empty schedules}">
                <div class="schedules">
                    <c:forEach var="schedule" items="${schedules}">
                        <div class="schedule-item">
                            <div class="schedule-info">
                                <h3>Workout ID: ${schedule.workoutId}</h3>
                                <p>
                                    <strong>Time:</strong>
                                        ${fn:substring(schedule.startTime, 0, 16)}
                                    -
                                        ${fn:substring(schedule.endTime, 11, 16)}
                                </p>
                                <p><strong>Participants:</strong> ${schedule.currentParticipants}
                                    / ${schedule.maxParticipants}</p>
                            </div>
                            <form method="post" action="${pageContext.request.contextPath}/coach-schedule"
                                  class="delete-form">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="scheduleId" value="${schedule.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure?')">
                                    Delete
                                </button>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="no-schedules">You don't have any schedules yet.</p>
            </c:otherwise>
        </c:choose>
    </section>
</div>
</body>
</html>