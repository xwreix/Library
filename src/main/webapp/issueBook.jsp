<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдать книгу</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/multistep.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="/library/issueBook" method="post" id="newIssue" class="form">
        <h1>Выдача книг</h1>

        <div class="tab" id="1">
            <div class="form-field">
                <label for="email">Email читателя:</label>
                <input type="text" name="email" id="email" autocomplete="off">
                <small></small>
            </div>
        </div>

        <div class="tab" id="2">
            <div class="form-field books">
                <h3>Наименования книг:</h3>
                <div>
                    <input type="text" class="bookName" name="book1" id="book" autocapitalize="on">
                    <small></small>
                </div>
            </div>

            <div class="form-field add">
                <button type="button" name="addBook" id="addBook">Добавить ещё одну книгу</button>
            </div>
        </div>

        <div class="tab" id="3">
            <div class="form-field">
                <label for="preliminaryDate">Дата возврата книг:</label>
                <input type="date" name="preliminaryDate" id="preliminaryDate" autocomplete="off">
                <small></small>
            </div>

            <div class="form-field">
                <label for="cost">Предварительная стоимость: </label>
                <input type="number" step="0.01" name="cost" id="cost" autocomplete="off">
                <small></small>
            </div>

            <div class="form-field" hidden>
                <input type="number" name="discount" id="discount" value="0">
            </div>

        </div>

        <div class="navButtons">
            <button type="button" id="nextBtn">Вперед</button>
            <button type="button" id="prevBtn">Назад</button>
        </div>

        <div class="navCircles">
            <span class="step"></span>
            <span class="step"></span>
            <span class="step"></span>
        </div>
    </form>
</div>


<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/service/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/issueBook.js"></script>
<script src="${pageContext.request.contextPath}/js/forms/newIssue.js"></script>
<script src="${pageContext.request.contextPath}/js/service/multistep.js"></script>
</body>
</html>
