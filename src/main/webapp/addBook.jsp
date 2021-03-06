<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новая книга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="/library/addBook" method="post" id="newBook" class="form" enctype="multipart/form-data">
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
            <select class="error" name="genres" id="genres" multiple size="5">
                <c:forEach var="genre" items="${genres}">
                    <option value="${genre.key}">${genre.value}</option>
                </c:forEach>
            </select>
            <small></small>
        </div>

        <div class="form-field">
            <label for="cost">Стоимость:</label>
            <input type="number" step="0.01" name="cost" id="cost" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <label for="amount">Количество экземпляров:</label>
            <input type="number" name="amount" id="amount" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field authors">
            <h3>Авторы:</h3>
            <div class="author">
                <label> Имя автора:
                    <input type="text" class="authorName" name="author1" id="author">
                    <small></small>
                </label>
                <label> Фото автора:
                    <input type="file" name="authorPhotos1[]" id="authorPhotos" multiple accept="image/*>">
                </label>
            </div>
        </div>

        <div class="form-field add">
            <button type="button" name="addAuthor" id="addAuthor">Добавить ещё одного автора</button>
        </div>

        <div class="form-field">
            <label for="covers">Обложки книги:</label>
            <input type="file" name="covers[]" id="covers" multiple accept="image/*">
            <small></small>
        </div>

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

<script src="${pageContext.request.contextPath}/js/service/validation.js"></script>
<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/forms/book.js"></script>
<script src="${pageContext.request.contextPath}/js/addBook.js"></script>
</body>
</html>
