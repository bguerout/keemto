<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Scheduled Tasks</title></head>
<body>
<div id="global-actions">
    <c:url value="admin/tasks/refresh" var="refreshURL"/>
    <a href="${refreshURL}">Refresh Fetching Tasks</a>
</div>
<div id="tasks">
    <c:forEach var="task" items="${requestScope['tasks']}">
        <div class="task">
            <c:out value="${task}"/>
            <a href="${task.taskId}">Run</a>
        </div>
    </c:forEach>
</div>
</body>
</html>
