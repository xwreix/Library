<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 06.01.2022
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список книг</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css" type="text/css">
</head>
<body>
<div class="container">
    <div class="search">
        <input type="text" placeholder="Введите наименование" id="search" onkeyup="tableSearch()"
        onkeydown="">
    </div>
    <table class="table sorted" id="list">
        <thead>
        <tr>
            <th>Название книги</th>
            <th>Жанры</th>
            <th datatype="number">Год выпуска</th>
            <th datatype="number">Общее количество экземпляров</th>
            <th datatype="number">Количество доступных к выдаче экзмепляров</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.books}" var="element">
            <tr>
                <td>${element.name}</td>

                <td>
                    <c:forEach items="${element.genres}" var="genre">
                        <ol>${genre}</ol>
                    </c:forEach>
                </td>

                <td>
                    <c:choose>
                        <c:when test="${element.year == 0}">
                            -
                        </c:when>
                        <c:otherwise>
                            ${element.year}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${element.totalAmount}</td>
                <td>${element.availableAmount}</td>
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
