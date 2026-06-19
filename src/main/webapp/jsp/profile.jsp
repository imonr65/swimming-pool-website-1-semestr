<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Профиль пользователя</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="profile-container">
    <c:if test="${not empty param.success}">
        <div class="success-message">
            <c:choose>
                <c:when test="${param.success == 'profile'}">Профиль успешно обновлен!</c:when>
                <c:when test="${param.success == 'photo'}">Фото профиля успешно обновлено!</c:when>
            </c:choose>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="error-message">
            <c:choose>
                <c:when test="${param.error == 'no_file'}">Пожалуйста, выберите файл для загрузки</c:when>
            </c:choose>
        </div>
    </c:if>

    <div class="profile-header">
        <div>
            <c:choose>
                <c:when test="${not empty userProfile.photoId}">
                    <img src="${pageContext.request.contextPath}/uploaded/files?id=${userProfile.photoId}&t=${System.currentTimeMillis()}"
                         alt="Profile Photo" class="profile-photo" id="profileImage"
                         onclick="document.getElementById('photoInput').click()">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/default-avatar.png"
                         alt="Default Avatar" class="profile-photo" id="profileImage"
                         onclick="document.getElementById('photoInput').click()">
                </c:otherwise>
            </c:choose>
            <!-- Скрытый input для загрузки фото -->
            <input type="file" id="photoInput" class="hidden-file-input"
                   accept="image/*" onchange="uploadPhoto(this.files[0])">
        </div>

        <div class="profile-info">
            <h1>${userProfile.firstName} ${userProfile.lastName}</h1>
            <p>${userProfile.email}</p>

            <!-- Бейдж активного абонемента -->
            <c:if test="${not empty memberships}">
                <div class="subscription-badge active">Активный абонемент</div>
            </c:if>
            <c:if test="${empty memberships}">
                <div class="subscription-badge inactive">Нет активных абонементов</div>
            </c:if>
        </div>
    </div>

    <!-- Секция абонементов -->
    <div class="profile-section">
        <h2>Мои абонементы</h2>
        <c:choose>
            <c:when test="${not empty memberships}">
                <div class="memberships-container">
                    <c:forEach var="membership" items="${memberships}">
                        <div class="membership-card">
                            <div class="membership-header">
                                <h3>Абонемент #${membership.id}</h3>
                                <span class="membership-status ${membership.current_amount_visit < membership.total_visit ? 'active' : 'used'}">
                                    <c:choose>
                                        <c:when test="${membership.current_amount_visit < membership.total_visit}">
                                            Активен
                                        </c:when>
                                        <c:otherwise>
                                            Использован
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <div class="membership-details">
                                <div class="detail-row">
                                    <span class="label">Тренировка:</span>
                                    <span class="value">ID ${membership.workoutId}</span>
                                </div>
                                <div class="detail-row">
                                    <span class="label">Период действия:</span>
                                    <span class="value">
                                        ${membership.formattedStartDate} - ${membership.formattedEndDate}
                                    </span>
                                </div>
                                <div class="detail-row">
                                    <span class="label">Посещения:</span>
                                    <span class="value">
                                        ${membership.current_amount_visit} / ${membership.total_visit}
                                    </span>
                                </div>
                                <div class="detail-row">
                                    <span class="label">Осталось посещений:</span>
                                    <span class="value remaining">
                                            ${membership.total_visit - membership.current_amount_visit}
                                    </span>
                                </div>
                                <c:if test="${not empty membership.pricing}">
                                    <div class="detail-row">
                                        <span class="label">Тариф:</span>
                                        <span class="value">${membership.pricing.name}</span>
                                    </div>
                                </c:if>
                            </div>

                            <!-- Прогресс-бар посещений -->
                            <div class="progress-container">
                                <div class="progress-bar">
                                    <div class="progress-fill"
                                         style="width: ${(membership.current_amount_visit / membership.total_visit) * 100}%">
                                    </div>
                                </div>
                                <div class="progress-text">
                                    Использовано ${membership.current_amount_visit} из ${membership.total_visit}
                                    посещений
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-memberships">
                    <p>У вас пока нет активных абонементов.</p>
                    <a href="${pageContext.request.contextPath}/workouts" class="btn btn-primary">
                        Посмотреть доступные тренировки
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Форма редактирования профиля -->
    <div class="profile-section">
        <h2>Редактировать профиль</h2>

        <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateProfile">

            <div class="form-group">
                <label for="firstName">Имя:</label>
                <input type="text" id="firstName" name="firstName" value="${userProfile.firstName}" required>
            </div>

            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <input type="text" id="lastName" name="lastName" value="${userProfile.lastName}" required>
            </div>

            <div class="form-group">
                <label for="phone">Телефон:</label>
                <input type="tel" id="phone" name="phone" value="${userProfile.phone}">
            </div>
            <button type="submit" class="btn">Сохранить изменения</button>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script>
    function uploadPhoto(file) {
        if (!file) return;

        const formData = new FormData();
        formData.append('action', 'uploadPhoto');
        formData.append('profilePhoto', file);

        fetch('${pageContext.request.contextPath}/profile', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    const newSrc = '${pageContext.request.contextPath}/uploaded/files?id=' + data.photoId + '&t=' + new Date().getTime();
                    document.getElementById('profileImage').src = newSrc;
                    showNotification('Фото профиля успешно обновлено!', 'success');
                } else {
                    showNotification('Ошибка: ' + data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('Ошибка при загрузке фото', 'error');
            });
    }

    function showNotification(message, type = 'success') {
        const existingNotifications = document.querySelectorAll('.notification');
        existingNotifications.forEach(notif => notif.remove());

        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        setTimeout(() => {
            notification.remove();
        }, 3000);
    }
</script>
</body>
</html>