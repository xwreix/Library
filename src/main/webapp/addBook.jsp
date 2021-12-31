<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 29.12.2021
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новая книга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="container">
    <form action="/library/addBook" method="post" id="newBook" class="form">
        <h1>Регистрация новой книги</h1>

        <div class="form-field">
            <label for="nameInRus">Наименование книги на русском языке:</label>
            <input type="text" name="nameInRus" id="nameInRus" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="originalName">Наименование книги на языке оригинала:</label>
            <input type="text" name="originalName" id="originalName" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="genres">Выберите жанры:</label>
            <select name="genres" id="genres" multiple size="3">
                <c:forEach var="genre" items="${genres}">
                    <option value="${genre.key}">${genre.value}</option>
                </c:forEach>
            </select>
            <small></small>
        </div>

        <div class="form-field">
            <label for="cost">Стоимость:</label>
            <input type="number" step="0.01" name="cost" id="cost"autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="amount">Количество экземпляров:</label>
            <input type="number" name="amount" id="amount" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field dynamic_fields">
            <label for="author[]">Авторы:</label>
            <div class="author">
                <div class="table">
                    <div class="cell">
                        <input type="text" name="author[]" id="author[]" autocomplete="off"/>
                    </div>
                    <div class="cell">
                        <button class="remove pull-right btn btn-danger" type="button">-</button>
                    </div>
                </div>
            </div>

            <div class="authors"></div>

            <button class="add btn btn-success" type="button">+</button>
        </div>

        //many pictures of author

        //many covers

        <div class="form-field">
            <label for="priceForDay">Цена за день использования:</label>
            <input type="number" step="0.01" name="priceForDay" id="priceForDay" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="publYear">Год издания:</label>
            <input type="number" name="publYear" id="publYear" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="registrDate">Дата регистрации:</label>
            <input type="date" name="registrDate" id="registrDate" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="pageAmount">Количество страниц:</label>
            <input type="number" name="pageAmount" id="pageAmount" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <input type="submit" value="Создать">
        </div>
    </form>
</div>
</form>

<script src="${pageContext.request.contextPath}/js/addBook.js"></script>
</body>
</html>