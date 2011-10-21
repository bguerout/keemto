<%@ page contentType="application/json" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:out escapeXml="false" value='{
"type":403,
"message":"authentication is needed."
}'/>
