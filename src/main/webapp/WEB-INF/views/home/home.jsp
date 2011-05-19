<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>


<ul>
  <c:forEach items="${events}" var="event">

    <div class="question">
      <p class="meta">
        <span class="date"><abbr class="timeago" title="${event.timestamp}">${event.timestamp}</abbr>
        </span> <span class="posted"><img width="30" height="30" src="http://www.gravatar.com/avatar/${event.user}" />
        </span> <span class="posted user">Posted by ${event.user}</span>
      </p>
      <div class="entry">
        <p>${event.message}</p>
      </div>
      <p class="links"></p>
    </div>

  </c:forEach>
</ul>

