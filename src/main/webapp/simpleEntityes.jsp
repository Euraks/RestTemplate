<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Simple Entitys</title>
</head>
<body>

<table>
    <c:forEach items="${simpleEntityes}" var="simpleEntityes">
        <tr>
            <td><c:out value="${simpleEntityes.description}" /></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
