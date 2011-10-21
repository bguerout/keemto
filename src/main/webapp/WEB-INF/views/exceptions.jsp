<%@ page contentType="application/json" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:out escapeXml="false" value='{
"type":500,
"exception":"${exception.class.name}",
"message":"${exception.message}"
}'/>
