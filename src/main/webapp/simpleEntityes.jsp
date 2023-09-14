<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Simple Entitys</title>
</head>
<body>
<br>
<p>Table SimpleEntity's</p>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>UUid</th>
        <th>Description</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${simpleEntityes}" var="simpleEntityes">
        <jsp:useBean id="simpleEntityes" type="org.example.model.SimpleEntity"/>
        <tr>
            <td>${simpleEntityes.uuid}</td>
            <td>${simpleEntityes.description}</td>
            <td><a href="simple?action=delete&id=${simpleEntityes.uuid}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<p> Add new SimpleEntity</p>
<br />
<form method="post" action="<c:url value='/simple'/>">
    <label>Description:  <input type="text" name="description" required /></label>
    <br>
    <br>
    <input type="submit" value="Ok" name="Ok"><br>
</form>
</body>
</html>
