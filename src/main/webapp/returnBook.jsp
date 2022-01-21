<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Возврат книг</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/multistep.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="/library/returnBook" method="post" id="returnBook" class="form" enctype="multipart/form-data">
        <h1>Возврат книг</h1>

        <div class="tab" id="1">
            <div class="form-field">
                <label for="email">Email читателя:</label>
                <input type="text" name="email" id="email" autocomplete="off">
                <small></small>
            </div>
        </div>

        <div class="tab" id="2">
            <div class="form-field books">
                <div class="copy">
                    <label for="book">Наименование книги:</label>
                    <input type="text" class="bookName" name="book1" id="book" autocapitalize="on">
                    <small></small>

                    <label for="damage">Нарушения:</label>
                    <input type="text" class="damage" name="damage1" id="damage">
                    <input type="file" name="damagePhotos1[]" id="damagePhotos" multiple accept="image/*>">

                    <label for="rating">Рейтинг:</label>
                    <input type="number" class="rating" name="rating1" id="rating" max="10">
                </div>
            </div>

            <div class="form-field add">
                <button type="button" name="addBook" id="addBook">Добавить ещё одну книгу</button>
            </div>
        </div>

        <div class="tab" id="3">
            <div class="form-field">
                <label for="returnDate">Дата возврата:</label>
                <input type="date" name="returnDate" id="returnDate" autocomplete="off">
                <small></small>
            </div>

            <div class="form-field">
                <label for="cost">Стоимость: </label>
                <input type="number" step="0.01" name="cost" id="cost" autocomplete="off">
                <small></small>
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
<script src="${pageContext.request.contextPath}/js/returnBook.js"></script>
<script src="${pageContext.request.contextPath}/js/forms/finishIssue.js"></script>
<script src="${pageContext.request.contextPath}/js/service/multistep.js"></script>
</body>
</html>
