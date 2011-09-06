<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/javascript" %>
$(document).ready(function() {
    Keemto.init();
    <sec:authorize access="isFullyAuthenticated()">
        Keemto.activeSession.set({login: '<sec:authentication property="principal.username" />'});
    </sec:authorize>

});
