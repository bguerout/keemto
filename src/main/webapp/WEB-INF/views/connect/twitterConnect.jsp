<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<h3>Connect to Twitter</h3>

<form action="<c:url value="/connect/twitter" />" method="POST">
  <div class="formInfo">
    <p>You haven't created any connections with Twitter yet. Click the button to connect Xevents with your Twitter
      account. (You'll be redirected to Twitter where you'll be asked to authorize the connection.)</p>
  </div>
  <p>
    <button type="submit">
      <img src="<c:url value="/resources/images/social/twitter/connect-with-twitter.png" />" />
    </button>
  </p>
</form>
