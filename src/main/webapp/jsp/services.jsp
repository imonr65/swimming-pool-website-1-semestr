<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Услуги бассейна</title>
    <style>
        <%@ include file="/css/services.css" %>
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <h1>Тренировки и услуги бассейна</h1>

    <div id="bookingModal" class="modal" style="display: none;">
        <div class="modal-content">
            <span class="close" onclick="closeBookingModal()">&times;</span>
            <h3>Бронирование тренировки</h3>
            <div class="form-group">
                <label>Выберите абонемент:</label>
                <select id="bookingMembership">
                    <option value="">Выберите абонемент</option>
                </select>
            </div>
            <input type="hidden" id="bookingWorkoutId">
            <input type="hidden" id="bookingScheduleId">
            <button onclick="bookWorkout()" class="btn btn-primary">Забронировать</button>
            <button onclick="closeBookingModal()" class="btn btn-secondary">Отмена</button>
        </div>
    </div>
    <div class="filters">
        <select id="categoryFilter">
            <option value="">Все категории</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.categoryName}</option>
            </c:forEach>
        </select>

        <select id="peopleFilter">
            <option value="">Все типы</option>
            <c:forEach items="${peopleTypes}" var="peopleType">
                <option value="${peopleType.id}">${peopleType.typeName}</option>
            </c:forEach>
        </select>

        <select id="levelFilter">
            <option value="">Все уровни</option>
            <c:forEach items="${levels}" var="level">
                <option value="${level.id}">${level.levelName}</option>
            </c:forEach>
        </select>

        <button onclick="applyFilters()" class="filter-btn">Применить фильтры</button>
        <button onclick="clearFilters()" class="clear-btn">Очистить</button>
    </div>

    <div id="loadingIndicator" class="loading" style="display: none;">
        <div class="spinner"></div>
        <span>Загрузка...</span>
    </div>

    <div id="message" class="message" style="display: none;"></div>

    <div id="workoutsList" class="workouts-grid">
        <c:forEach items="${workouts}" var="workout">
            <div class="workout-card" data-workout-id="${workout.id}">
                <div class="workout-header">
                    <h3>${workout.description}</h3>
                </div>
                <div class="workout-info">
                    <div class="info-item">
                        <span class="label">Категория:</span>
                        <span class="value">${workout.category.categoryName}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">Тип:</span>
                        <span class="value">${workout.peopleType.typeName}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">Уровень:</span>
                        <span class="value">${workout.level.levelName}</span>
                    </div>
                </div>
                <div class="actions">
                    <button onclick="addToMembership(${workout.id})" class="btn btn-primary">
                        Добавить в абонемент
                    </button>
                    <button onclick="showBookingModal(${workout.id})" class="btn btn-success">
                        Забронировать
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>

</div>
<jsp:include page="footer.jsp"/>

<script>
    function applyFilters() {
        const categoryId = document.getElementById('categoryFilter').value;
        const peopleId = document.getElementById('peopleFilter').value;
        const levelId = document.getElementById('levelFilter').value;

        showLoading(true);

        const params = new URLSearchParams();
        if (categoryId) params.append('categoryId', categoryId);
        if (peopleId) params.append('peopleId', peopleId);
        if (levelId) params.append('levelId', levelId);

        const url = '${pageContext.request.contextPath}/services/filter?' + params;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}`);
                }
                return response.json();
            })
            .then(workouts => {
                renderWorkouts(workouts);
                showMessage('Фильтры применены успешно', 'success');
            })
            .finally(() => {
                showLoading(false);
            });
    }

    function clearFilters() {
        document.getElementById('categoryFilter').value = '';
        document.getElementById('peopleFilter').value = '';
        document.getElementById('levelFilter').value = '';
        applyFilters();
    }

    function renderWorkouts(workouts) {
        const container = document.getElementById('workoutsList');

        if (!workouts || workouts.length === 0) {
            container.innerHTML = '<div class="no-results">Тренировки не найдены</div>';
            return;
        }

        let html = '';
        workouts.forEach(workout => {
            const description = workout.description || 'Нет описания';
            const categoryName = workout.category ? workout.category.categoryName : 'Не указано';
            const peopleTypeName = workout.peopleType ? workout.peopleType.typeName : 'Не указано';
            const levelName = workout.level ? workout.level.levelName : 'Не указано';

            html += '<div class="workout-card" data-workout-id="' + workout.id + '">' +
                '<div class="workout-header">' +
                '<h3>' + description + '</h3>' +
                '</div>' +
                '<div class="workout-info">' +
                '<div class="info-item">' +
                '<span class="label">Категория:</span>' +
                '<span class="value">' + categoryName + '</span>' +
                '</div>' +
                '<div class="info-item">' +
                '<span class="label">Тип:</span>' +
                '<span class="value">' + peopleTypeName + '</span>' +
                '</div>' +
                '<div class="info-item">' +
                '<span class="label">Уровень:</span>' +
                '<span class="value">' + levelName + '</span>' +
                '</div>' +
                '</div>' +
                '<div class="actions">' +
                '<button onclick="addToMembership(' + workout.id + ')" class="btn btn-primary">Добавить в абонемент</button>' +
                '</div>' +
                '</div>';
        });
        container.innerHTML = html;
        debugLog('Отрисовано тренировок: ' + workouts.length);
    }

    function addToMembership(workoutId) {
        console.log('addToMembership вызвана для workoutId:', workoutId);

        if (!workoutId) {
            console.error('workoutId is undefined or null');
            showMessage('Ошибка: ID тренировки не определен', 'error');
            return;
        }

        if (confirm('Добавить эту тренировку в абонемент?')) {
            const params = new URLSearchParams();
            params.append('action', 'addToMembership');
            params.append('workoutId', workoutId.toString());

            console.log('Отправка запроса с параметрами:', params.toString());

            fetch('${pageContext.request.contextPath}/services', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
                .then(response => {
                    console.log('Статус ответа:', response.status);
                    console.log('OK:', response.ok);

                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }

                    const contentType = response.headers.get('content-type');
                    console.log('Content-Type:', contentType);

                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(text => {
                            console.log('Не-JSON ответ:', text);
                            throw new Error('Сервер вернул не-JSON ответ: ' + text);
                        });
                    }
                })
                .then(result => {
                    console.log('Успешный ответ от сервера:', result);
                    if (result && result.status) {
                        showMessage(result.message, result.status === 'success' ? 'success' : 'error');
                    } else {
                        showMessage('Неизвестный формат ответа от сервера', 'error');
                    }
                })
                .catch(error => {
                    console.error('Ошибка в addToMembership:', error);
                    showMessage('Ошибка при создании абонемента: ' + error.message, 'error');
                });
        }
    }


    function showLoading(show) {
        document.getElementById('loadingIndicator').style.display = show ? 'flex' : 'none';
    }

    function showMessage(text, type) {
        const messageEl = document.getElementById('message');
        messageEl.textContent = text;
        messageEl.className = 'message ' + type;
        messageEl.style.display = 'block';

        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 5000);
    }

    document.addEventListener('DOMContentLoaded', function () {
        debugLog('DOM загружен');
    });

    function loadSchedule(workoutId) {
        const today = new Date().toISOString().split('T')[0];
        const nextWeek = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

        fetch('${pageContext.request.contextPath}/schedule?action=getAvailable&workoutId=' + workoutId + '&start=' + today + '&end=' + nextWeek)
            .then(response => response.json())
            .then(schedules => {
                renderSchedule(workoutId, schedules);
            })
            .catch(error => {
                console.error('Error loading schedule:', error);
            });
    }

    function renderSchedule(workoutId, schedules) {
        const scheduleContainer = document.getElementById('schedule-' + workoutId);
        if (!scheduleContainer) return;

        if (!schedules || schedules.length === 0) {
            scheduleContainer.innerHTML = '<p>Нет доступных расписаний</p>';
            return;
        }

        let html = '<div class="schedule-list">';
        schedules.forEach(schedule => {
            const startTime = new Date(schedule.startWorkout).toLocaleString();
            const availableSpots = schedule.availableSpots;
            const isAvailable = availableSpots > 0;

            html += '<div class="schedule-item ' + (isAvailable ? 'available' : 'full') + '">' +
                '<div class="schedule-time">' + startTime + '</div>' +
                '<div class="schedule-spots">Свободно: ' + availableSpots + '/' + schedule.maxParticipants + '</div>' +
                (isAvailable ?
                    '<button onclick="showBookingModal(' + workoutId + ', ' + schedule.id + ')" class="btn btn-book">Забронировать</button>' :
                    '<button disabled class="btn btn-disabled">Мест нет</button>') +
                '</div>';
        });
        html += '</div>';

        scheduleContainer.innerHTML = html;
    }

    function showBookingModal(workoutId, scheduleId) {
        fetch('${pageContext.request.contextPath}/services?action=getMemberships&workoutId=' + workoutId)
            .then(response => response.json())
            .then(memberships => {
                const modal = document.getElementById('bookingModal');
                const membershipSelect = document.getElementById('bookingMembership');

                // Заполняем выбор абонементов
                membershipSelect.innerHTML = '';
                memberships.forEach(membership => {
                    const option = document.createElement('option');
                    option.value = membership.id;
                    option.textContent = 'Абонемент до ' + membership.end_date + ' (осталось: ' + (membership.total_visit - membership.current_amount_visit) + ' посещений)';
                    membershipSelect.appendChild(option);
                });

                document.getElementById('bookingScheduleId').value = scheduleId;
                modal.style.display = 'block';
            })
            .catch(error => {
                showMessage('Ошибка при загрузке абонементов', 'error');
            });
    }

    function bookWorkout() {
        const scheduleId = document.getElementById('bookingScheduleId').value;
        const membershipId = document.getElementById('bookingMembership').value;

        if (!scheduleId || !membershipId) {
            showMessage('Пожалуйста, выберите абонемент', 'error');
            return;
        }

        const formData = new FormData();
        formData.append('action', 'bookWorkout');
        formData.append('scheduleId', scheduleId);
        formData.append('membershipId', membershipId);

        fetch('${pageContext.request.contextPath}/services', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(result => {
                showMessage(result.message, result.status === 'success' ? 'success' : 'error');
                if (result.status === 'success') {
                    closeBookingModal();
                    // Обновляем расписание
                    const workoutId = document.getElementById('bookingWorkoutId').value;
                    loadSchedule(workoutId);
                }
            })
            .catch(error => {
                showMessage('Ошибка при бронировании', 'error');
            });
    }

    function closeBookingModal() {
        document.getElementById('bookingModal').style.display = 'none';
    }
</script>
</body>
</html>