<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 26.12.2021
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Библиотека</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>
<div class="btn-group">
    <a href="${pageContext.request.contextPath}/library/addReader" class="button">Добавить читателя</a>
    <a href="${pageContext.request.contextPath}/library/addBook" class="button">Добавить книгу</a>
</div>
<jsp:include page="bookList.jsp"/>
</body>
</html>
