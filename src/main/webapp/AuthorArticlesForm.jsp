<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Author and Articles</title>
</head>
<body>
<h1>Article</h1>
<br>
<h3><a href="index.jsp">Home</a></h3>
<br>
<form action="${pageContext.request.contextPath}/AuthorServlet?action=${action}" method="post">
    <input type="hidden" value="${author!=null?author.uuid:0}" name="authorId">
    <input type="hidden" value="${author!=null?author.authorName:"Input Name"}" name="authorName">

    <div id="articles">
        <div>
            <input type="hidden" value="${article!=null?article.uuid:0}" name="articleIds"><br>
            Article Text: <input type="text" value="${article!=null?article.text:"Input text"}" name="articleTexts"><br>
        </div>
    </div>

    <input type="submit" value="Create">
</form>
<br>
</body>
</html>
