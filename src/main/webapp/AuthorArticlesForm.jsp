<jsp:useBean id="authorEntity" scope="request" type="org.example.model.AuthorEntity"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Create Author and Articles</title>
</head>
<body>

<h1>Create Author and Articles</h1>

<form action="${pageContext.request.contextPath}/AuthorServlet?action=${action}" method="post">
  <input type="hidden" name="action" value="create">
  Author ID: <input type="text" value="${authorEntity.uuid}" name="authorId"><br>
  Author Name: <input type="text" value="${authorEntity.authorName}" name="authorName"><br>

  Articles: <br>
  <div id="articles">
    <div>
      Article ID: <input type="text" name="articleIds"><br>
      Article Text: <input type="text" name="articleTexts"><br>
    </div>
  </div>

  <input type="submit" value="Create">
</form>
<br>
</body>
</html>
