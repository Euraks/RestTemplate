<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Authors and Articles</title>
</head>
<body>

<h1>Authors and Articles</h1>

<table border="1">
    <tr>
        <th>Author ID</th>
        <th>Author Name</th>
        <th>Articles</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>

    <c:forEach var="author" items="${authors}">
        <tr>
            <td>${author.uuid}</td>
            <td>${author.authorName}</td>
            <td>
                <ul>
                    <c:forEach var="article" items="${author.articleList}">
                        <li>${article.text}</li>
                    </c:forEach>
                </ul>
            </td>
            <td><a href="/AuthorServlet?action=update?authorId=${author.uuid}">Update</a></td>
            <td><a href="/AuthorServlet?action=delete?authorId=${author.uuid}">Delete</a></td>
        </tr>
    </c:forEach>
</table>

<h2>Update or Delete Articles</h2>

<c:forEach var="author" items="${authors}">
    <h3>Articles by ${author.authorName}</h3>
    <table border="1">
        <tr>
            <th>Article ID</th>
            <th>Text</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="article" items="${author.articleList}">
            <tr>
                <td>${article.uuid}</td>
                <td>${article.text}</td>
                <td><a href="editArticle?action=update?articleId=${article.uuid}">Update</a></td>
                <td><a href="deleteArticle?action=delete?articleId=${article.uuid}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</c:forEach>

<p><a href="AuthorArticlesForm.jsp"> Author and Servlet Page</a> </p>

</body>
</html>

