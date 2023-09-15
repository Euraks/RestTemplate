<%--
  Created by IntelliJ IDEA.
  User: @Euraks
  Date: 15.09.2023
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title> Create Author </title>
</head>
<body>
<h1>Create Author </h1>
<br>
<h3><a href="index.jsp">Home</a></h3>
<br>

<form action="${pageContext.request.contextPath}/AuthorServlet?action=createAuthor" method="post">
    <input type="hidden" name="action" value="create">
    Author ID: <input type="text" value="${authorEntity.uuid}" name="authorId"><br>
    Author Name: <input type="text" value="${authorEntity.authorName}" name="authorName"><br>
</form>

</body>
</html>
