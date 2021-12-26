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
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <title>Новый читатель</title>
</head>
<body>
<div class="container">
    <form action="/library" method="post" id="addReader" class="form">
        <h1>Регистрация нового читателя</h1>
        <div class="form-field success">
            <label for="surname">Фамилия:</label>
            <input type="text" name="surname" id="surname" autocapitalize="on" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field success">
            <label for="patronymic">Отчетство:</label>
            <input type="text" name="patronymic" id="patronymic" autocapitalize="on" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field success">
            <label for="email">Электронная почта:</label>
            <input type="text" name="email" id="email" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <input type="submit" value="Создать">
        </div>
    </form>
</div>

<script src="js/addReader.js"></script>
<%--<h1>Регистрация нового читателя</h1>--%>
<%--<form action="/library/addReader" method="post" name="addReader">--%>
<%--    <p>Фамилия: </p>--%>
<%--    <input type="text" name="surname">--%>
<%--    <p>Имя: </p>--%>
<%--    <input type="text" name="name">--%>
<%--    <p>Отчество: </p>--%>
<%--    <input type="text" name="patronymic">--%>
<%--    <p>Номер паспорта: </p>--%>
<%--    <input type="text" name="passport">--%>
<%--    <p>Дата рождения: </p>--%>
<%--    <input type="date"name="dateOfBirth">--%>
<%--    <p>Адрес (город, улица, дом, квартира): </p>--%>
<%--    <input type="text "name="city" class="address">--%>
<%--    <input type="text" name="street" class="address">--%>
<%--    <input type="text" name="building" class="address short">--%>
<%--    <input type="number" name="flat" class="address short">--%>
<%--    <p>Электронная почта: </p>--%>
<%--    <input type="email"name="email">--%>
<%--    <input type="submit" name="Send">--%>
<%--</form>--%>
</body>
</html>
