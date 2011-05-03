<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<h2>Menu</h2>
<ul class="menu">
  <li><a href="<c:url value="/twitter"/>">User Profile</a></li>
  <li><a href="<c:url value="/twitter/timeline"/>">Timeline</a></li>
  <li><a href="<c:url value="/twitter/friends"/>">Friends</a></li>
  <li><a href="<c:url value="/twitter/followers"/>">Followers</a></li>
  <li><a href="<c:url value="/twitter/messages"/>">Messages</a></li>
  <li><a href="<c:url value="/twitter/trends/current"/>">Current Trends</a></li>
</ul>
