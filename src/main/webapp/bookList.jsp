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
    <table class="table" id="books">
        <thead>
        <th>Название книги</th>
        <th>Жанры</th>
        <th>Год выпуска</th>
        <th>Общее количество экземпляров</th>
        <th>Количество доступных к выдаче экзмепляров</th>
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

<%--    <div class="pagination-container">--%>
<%--        <nav>--%>
<%--            <ul class="pagination">--%>
<%--                <li data-page="prev">--%>
<%--                    <span> < <span class="sr-only">(current)</span></span>--%>
<%--                </li>--%>

<%--                <li data-page="next" id="prev">--%>
<%--                    <span> > <span class="sr-only">(current)</span></span>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </nav>--%>
<%--    </div>--%>
</div>

<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/bookList.js"t></script>
</body>
</html>
