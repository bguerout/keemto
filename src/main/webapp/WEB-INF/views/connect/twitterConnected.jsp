<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<h3>Twitter Connections</h3>

<p>
  You have registered Twitter account(s) :
  <c:forEach items="${connections}" var="connection">
    <a href="${connection.profileUrl}">${connection.displayName}</a>
  </c:forEach>
</p>

<form action="<c:url value="/connect/twitter" />" method="POST">
  <div class="formInfo">
    <p>Click the button to add another one.</p>
  </div>
  <p>
    <button type="submit">
      <img src="<c:url value="/resources/images/social/twitter/connect-with-twitter.png" />" />
    </button>
  </p>
</form>
