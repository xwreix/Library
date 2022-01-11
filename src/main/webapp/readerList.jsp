<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 09.01.2022
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список читателей</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
</head>
<body>

<div class="container">
    <div class="search">
        <input type="text" placeholder="Введите фамилию" id="search" onkeyup="tableSearch()"
               onkeydown="">
    </div>
    <table class="table sorted searchable" id="list">
        <thead>
        <tr>
            <th>Фамилия</th>
            <th>Имя</th>
            <th>Дата рождения</th>
            <th>Адрес</th>
            <th>Электронная почта</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.readers}" var="element">
            <tr>
                <td>${element.surname}</td>
                <td>${element.name}</td>
                <td>${element.dateOfBirth}</td>


                <td>
                    <c:choose>
                        <c:when test="${element.address == null}">
                            -
                        </c:when>
                        <c:otherwise>
                            ${element.address}
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>${element.email}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="btn-group nav">
        <a onclick="prevPage()" href='#' id="btn_prev" class="button">Предыдущая</a>
        <a onclick="nextPage()" href='#' id="btn_next" class="button">Следующая</a>
    </div>
    <div class="pageNum"><span id="page"></span></div>

</div>

<script src="${pageContext.request.contextPath}/js/tableService.js"></script>
</body>
</html>
