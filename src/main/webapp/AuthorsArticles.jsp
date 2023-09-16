<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Article" %>
<%@ page import="org.example.model.AuthorEntity" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>
    <title>Authors and Articles</title>
</head>
<body>

<h1>Authors and Articles</h1>
<br>
<h3><a href="index.jsp">Home</a></h3>
<br>
<p><a href="AuthorArticlesForm.jsp?action=update"> Add new author and servlet page</a></p>

<table border="1">
    <thead>
    <tr>
        <th>Author UUID</th>
        <th>Author Name</th>
        <th>Articles</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="author" items="${authors}">
        <tr>
            <td>${author.uuid}</td>
            <td>${author.authorName}</td>
            <td>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Article UUID</th>
                        <th>Article Text</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="article" items="${author.articleList}">
                        <tr>
                            <td>${article.uuid}</td>
                            <td>${article.text}</td>
                            <td>
                                <a href="/AuthorServlet?action=addArticle&authorId=${author.uuid}">Add</a>
                                <a href="/AuthorServlet?action=updateArticle&articleId=${article.uuid}&authorId=${author.uuid}">Update</a>
                                <a href="/AuthorServlet?action=deleteArticle&articleId=${article.uuid}">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
            <td>
                <a href="/AuthorServlet?action=update&authorId=${author.uuid}">Update AUTHOR</a>
                <a href="/AuthorServlet?action=delete&authorId=${author.uuid}">Delete AUTHOR</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
