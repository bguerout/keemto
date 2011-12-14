<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Scheduled Tasks</title></head>
<body>
<div id="global-actions">
    <c:url value="/admin/tasks/reset" var="refreshURL"/>
    <form method="POST" action="${refreshURL}">
        <input type="submit" value="Reset"/>
    </form>
</div>
<div id="tasks">
    <c:forEach var="task" items="${requestScope['tasks']}">
        <div class="task">
            <c:url value="/admin/tasks/${task.taskId}" var="deleteTaskURL"/>
            <form method="POST" action="${deleteTaskURL}">
                <label>${task}</label>
                <input type="hidden" name="_method" value="delete"/>
                <input type="submit" value="Cancel"/>
            </form>
        </div>
    </c:forEach>
</div>
</body>
</html>
