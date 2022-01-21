<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Доходность</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="#" id="profitability" class="form">
        <h1>Доходность библиотеки</h1>

        <div class="form-field">
            <label for="startDate">Начальная дата:</label>
            <input type="date" name="startDate" id="startDate" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="finishDate">Конечная дата:</label>
            <input type="date" name="finishDate" id="finishDate" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <button type="button" name="calculate" id="calculate">Рассчитать</button>
        </div>

    </form>

    <p id="info"></p>
</div>

<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/service/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/calculateProfitability.js"></script>
<script src="${pageContext.request.contextPath}/js/forms/profitability.js"></script>
</body>
</html>
