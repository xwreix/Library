<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Результат</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
</head>
<body>
<jsp:include page="header.jsp"/>
<h1>${result}</h1>
</body>
</html>
