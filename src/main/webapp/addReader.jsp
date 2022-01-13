<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новый читатель</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
</head>
<body>
<jsp:include page="header.jsp"/>
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