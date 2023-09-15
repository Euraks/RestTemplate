<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Simple Entitys</title>
</head>
<body>

<h1>Simple Entitys</h1>
<br>
<h3><a href="index.jsp">Home</a></h3>
<br>
<p> <a href="simple?action=create">Add Simple Entity's Page</a> </p>
<br />
<br>

<p>Table SimpleEntity's</p>
<table border="1">
    <thead>
    <tr>
        <th>UUid</th>
        <th>Description</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${simpleEntityes.simpleEntityList}" var="simpleEntityes">
        <jsp:useBean id="simpleEntityes" type="org.example.model.SimpleEntity"/>
        <tr>
            <td>${simpleEntityes.uuid}</td>
            <td>${simpleEntityes.description}</td>
            <td><a href="simple?action=update&uuid=${simpleEntityes.uuid}">Update</a></td>
            <td><a href="simple?action=delete&uuid=${simpleEntityes.uuid}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
