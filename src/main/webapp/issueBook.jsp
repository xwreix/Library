<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 09.01.2022
  Time: 18:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдать книгу</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
</head>
<body>
<div class="container">
    <form action="/library/issueBook" method="post" id="newIssue" class="form">
        <h1>Выдача книг</h1>

        <div class="form-field">
            <label for="email">Email читателя:</label>
            <input type="text" name="email" id="email" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <button type="button" name="checkReader" id="checkReader">Проверить</button>
        </div>

        <div class="form-field books">
            <h3>Наименования книг:</h3>
                <input type="text" class="bookNames" name="book1" id="book">
                <small></small>
        </div>

        <div class="form-field add">
            <button type="button" name="addBook" id="addBook">Добавить ещё одну книгу</button>
        </div>

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

        <div class="form-field infoBook">
            <button type="button" name="info" id="info">Info</button>
        </div>


        <div id="tt"></div>

    </form>
</div>


<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/issueBook.js"></script>
</body>
</html>
