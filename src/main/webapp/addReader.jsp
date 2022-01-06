<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 25.12.2021
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <title>Новый читатель</title>
</head>
<body>
<div class="container">
    <form action="/library/addReader" method="post" id="newReader" class="form" enctype="multipart/form-data">
        <h1>Регистрация нового читателя</h1>

        <div class="form-field">
            <label for="surname">Фамилия:</label>
            <input type="text" name="surname" id="surname" autocapitalize="words" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="name">Имя:</label>
            <input type="text" name="name" id="name" autocapitalize="words" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="patronymic">Отчество:</label>
            <input type="text" name="patronymic" id="patronymic" autocapitalize="words" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="passportNumber">Номер паспорта:</label>
            <input type="text" name="passportNumber" id="passportNumber" autocomplete="off" autocapitalize="characters">
            <small></small>
        </div>

        <div class="form-field">
            <label for="dateOfBirth">Дата рождения:</label>
            <input type="date" name="dateOfBirth" id="dateOfBirth" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="address">Адрес:</label>
            <input type="text" name="address" id="address" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="email">Электронная почта:</label>
            <input type="text" name="email" id="email" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <input type="submit" value="Создать">
        </div>
    </form>
</div>


<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/addReader.js"></script>
</body>
</html>