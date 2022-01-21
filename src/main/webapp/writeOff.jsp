<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Списание книги</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css" type="text/css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="text/x-icon">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form  action="/library/writeOff" method="post" id="writingOff" class="form">
        <h1>Списание книги</h1>

        <div class="form-field" id="copy">
            <label for="copyId">Идентификационный номер книги:</label>
            <input type="number" name="copyId" id="copyId" autocomplete="off">
            <small></small>
        </div>

        <div class="form-field">
            <button type="button" name="writeOff" id="writeOff">Списать</button>
        </div>

        <p id="info"></p>

        <div class="form-field">
            <input type="submit" id="submitWritingOff" value="Подтвердить списание">
        </div>

    </form>
</div>

<script src="http://code.jquery.com/jquery-1.11.0.js"></script>
<script src="${pageContext.request.contextPath}/js/service/validation.js"></script>
<script src="${pageContext.request.contextPath}/js/writeOff.js"></script>
<script src="${pageContext.request.contextPath}/js/forms/copy.js"></script>
</body>
</html>
