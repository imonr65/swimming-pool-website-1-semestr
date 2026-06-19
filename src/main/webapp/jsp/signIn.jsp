<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <style>
        <%@ include file="/css/signIn.css" %>
    </style>
    <title>Авторизация</title>
</head>
<body>


<div class="login-container">
    <div class="login-header">
        <h1>Добро пожаловать</h1>
        <p>Войдите в свой аккаунт</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>

    <form class="login-form" action="/signIn" method="post">
        <div class="form-group">
            <input name="email" type="email" placeholder="Login" required>
        </div>

        <div class="form-group">
            <input name="password" type="password" placeholder="Password" required>
        </div>

        <button type="submit" class="submit-btn">SignIn</button>
    </form>

    <div class="register-link">
        <p>Нет аккаунта?</p>
        <a href="/signUp" class="register-btn">Зарегистрироваться</a>
    </div>
</div>

</body>
</html>
