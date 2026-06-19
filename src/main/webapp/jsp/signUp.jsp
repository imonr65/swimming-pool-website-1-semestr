<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <style>
        <%@ include file="/css/signUp.css" %>
    </style>
    <title>Регистрация</title>
</head>
<body>
<div class="form-container">
    <div class="form-header">
        <h1>Регистрация</h1>
        <p>Создайте новый аккаунт</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
                ${errorMessage}
        </div>
    </c:if>

    <form action="/signUp" method="post">
        <div class="form-group">
            <input name="email" type="email" placeholder="Email" required>
        </div>
        <div class="form-group">
            <input name="password" type="password" placeholder="Пароль" required>
        </div>
        <div class="form-group">
            <input name="firstName" placeholder="Имя" required>
        </div>
        <div class="form-group">
            <input name="lastName" placeholder="Фамилия" required>
        </div>
        <div class="form-group">
            <input name="phone" type="tel" placeholder="Номер телефона" required>
        </div>
        <button type="submit" class="submit-btn">Зарегистрироваться</button>
    </form>

    <div class="form-footer">
        <p>Уже есть аккаунт? <a href="/signIn">Войдите</a></p>
    </div>
</div>

</body>
</html>