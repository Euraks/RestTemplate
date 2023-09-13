<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add  Simple Entity</title>
</head>

<body>

<br />
<form method="post" action="<c:url value='/simple'/>">
    <label>Description:  <input type="text" name="description" required /></label>
    <br>
    <br>
    <input type="submit" value="Ok" name="Ok"><br>
</form>
</body>
</html>