<%--
  Created by IntelliJ IDEA.
  User: xwreix
  Date: 31.12.2021
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/library/addBook" method="post" id="Ing" enctype="multipart/form-data">
    <input type="file" name="file" id="file"/>
    <input type="submit" name="submit" value="submit"/>
</form>
</body>
</html>
