<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/javascript" %>
 <%
response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
response.addHeader("Cache-Control", "no-store");
response.addHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 1L); //prevent caching at the proxy server
%>
$(document).ready(function() {
    <sec:authorize access="isAnonymous()">
        Keemto.init();
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        Keemto.init({user:new Keemto.Common.User({login: '<sec:authentication property="principal.username" />'})});
    </sec:authorize>

    <c:if test="${not empty sessionScope.oauthToken}">
      new Keemto.Common.OAuthConfirmPopup().render();
    </c:if>
});
