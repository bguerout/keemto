<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<h2>Menu</h2>
<ul class="menu">
  <sec:authorize access="isAuthenticated()">
    <li><a href="<c:url value="/connections"/>">Manage your Connections</a></li>
     <li><a href="<c:url value="/signout"/>">Sign Out</a></li>
  </sec:authorize>
  <sec:authorize access="isAnonymous()">
    <li><a href="<c:url value="/signin"/>">Sign In</a></li>
  </sec:authorize>
</ul>
