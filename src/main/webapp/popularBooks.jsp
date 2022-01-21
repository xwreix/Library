<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Популярное</title>
</head>
<body>
<div class="popular">
<c:forEach items="${requestScope.booksImg}" var="element">
    <div class="popular_item">
        <img src="data:image/jpg;base64,${element.base64Image}" width="240" height="300"/>
        <p>Количество прочитавших: ${element.amount}</p>
        <p>Рейтинг: ${element.rating}</p>
    </div>
</c:forEach>
</div>
</body>
</html>
