<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Библиотека</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="btn-group" id="forms">
    <a href="${pageContext.request.contextPath}/library/addReader" class="button">Добавить читателя</a>
    <a href="${pageContext.request.contextPath}/library/addBook" class="button">Добавить книгу</a>
    <a href="${pageContext.request.contextPath}/library/issueBook" class="button">Выдать книги</a>
    <a href="${pageContext.request.contextPath}/library/returnBook" class="button">Возврат книг</a>
    <a href="${pageContext.request.contextPath}/library/writeOff" class="button">Списание книги</a>
</div>
<div class="btn-group">
    <a href="${pageContext.request.contextPath}/library/readerList" class="button">Список читателей</a>
    <a href="${pageContext.request.contextPath}/library/profitability" class="button">Доходность</a>
    <a href="${pageContext.request.contextPath}/library/send" class="button">Отправить уведомления</a>
</div>
<p>${requestScope.message}</p>
<jsp:include page="popularBooks.jsp"/>
<jsp:include page="bookList.jsp"/>
</body>
</html>
