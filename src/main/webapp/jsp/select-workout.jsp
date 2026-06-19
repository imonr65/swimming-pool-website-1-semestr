<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Select Workout</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/select-workout.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Add Workout to Your Profile</h1>
        <p>Select a workout from the list below</p>
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

    <%-- Список всех тренировок --%>
    <div class="workouts-list">
        <c:choose>
            <c:when test="${not empty workouts}">
                <form method="post" action="${pageContext.request.contextPath}/add-workout-to-coach">
                    <div class="workout-selection">
                        <c:forEach var="workout" items="${workouts}">
                            <div class="workout-item">
                                <input type="radio" name="workoutId" value="${workout.id}" id="workout${workout.id}"
                                       class="workout-radio">
                                <label for="workout${workout.id}" class="workout-label">
                                    <div class="workout-info">
                                        <h3>Workout #${workout.id}</h3>
                                        <p class="description">${workout.description}</p>
                                        <div class="workout-meta">
                                            <span>Category: ${workout.category.id}</span>
                                            <span>Level: ${workout.level.id}</span>
                                            <span>Type: ${workout.peopleType.id}</span>
                                        </div>
                                    </div>
                                </label>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Add Selected Workout</button>
                        <a href="${pageContext.request.contextPath}/coach-profile" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="no-workouts">
                    <p>No workouts available.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>