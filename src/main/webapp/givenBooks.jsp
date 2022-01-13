<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдача книги</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/table.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <table class="table sorted searchable" id="list">
        <thead>
        <tr>
            <th>Наименование</th>
            <th>Имеющиеся нарушения экземпляра</th>
            <th>Скидка</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.givenBooks}" var="element">
            <tr>
                <td>${element.name}</td>

                <td>
                    <c:choose>
                        <c:when test="${element.damage == null}">
                            -
                        </c:when>
                        <c:otherwise>
                            ${element.damage}
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>${element.discount} %</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
