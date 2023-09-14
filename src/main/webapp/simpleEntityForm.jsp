<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SimpleEntity Form</title>
</head>
<body>
<section>
    <h3><a href="index.jsp">Home</a></h3>
    <br>
    <h2>${action == 'create' ? 'Create SimpleEntity' : 'Update SimpleEntity'}</h2>
    <form method="post" action="${pageContext.request.contextPath}/simple?action=${action}">
        <dl>
            <label>UUid: ${simpleEntity.uuid}
                <input type="hidden" name="uuid" value="${simpleEntity.uuid}" required></label>
            <label>Description:
                <input type="text" value="${simpleEntity.description}" name="description" required/></label>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>


<%--<form method="post" action="${pageContext.request.contextPath}/simple">--%>
<%--    <label>Description:  <input type="text" name="description" required /></label>--%>
<%--    <br>--%>
<%--    <br>--%>
<%--    <input type="submit" value="Ok" name="Ok"><br>--%>
<%--</form>--%>
</body>
</html>
