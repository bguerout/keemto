<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>

<h3>Manage your connections</h3>

<div>
  <c:forEach items="${connections}" var="provider">

    <c:forEach items="${provider.value}" var="connection">
      <div class="connection">
        <form method="post" action="connect/${connection.key.providerId}/${connection.key.providerUserId}">
        <img src="<c:url value="/resources/images/social/${connection.key.providerId}/logo.png" />" />
        <a href="${connection.profileUrl}">${connection.displayName}</a>
        <input type="image" src="<c:url value="/resources/images/delete.gif" />" border="0">
        <input type="hidden" name="_method" value="delete" />
        </form>
      </div>
    </c:forEach>
  </c:forEach>

  <a href="connect/twitter">Add a new Twitter Connection</a>

</div>

