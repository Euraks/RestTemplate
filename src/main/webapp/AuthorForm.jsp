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
    <title> Update Author </title>
</head>
<body>
<h1>Update Author </h1>
<br>
<h3><a href="index.jsp">Home</a></h3>
<br>

<form action="${pageContext.request.contextPath}/AuthorServlet?action=updateAuthor&authorId=${author.uuid}" method="post">

    <input type="hidden" value="${author.uuid}" name="authorId"><br>
    Author Name: <input type="text" value="${author.authorName}" name="authorName"><br>
    <input type="submit" value="Create">
</form>

</body>
</html>
