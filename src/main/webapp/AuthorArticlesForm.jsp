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

<form action="AuthorServlet" method="post">
  <input type="hidden" name="action" value="create">
  Author ID: <input type="text" name="authorId"><br>
  Author Name: <input type="text" name="authorName"><br>

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
