<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page session="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<title>XEvents</title>

<script src="http://code.jquery.com/jquery-1.4.4.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js/jquery.timeago-0.9.2.js"/>" type="text/javascript"></script>

<link href="<c:url value="/resources/images/france_favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" type="text/css" media="screen" />

</head>
<body>
  <div id="wrapper">
    <div id="header-wrapper">
      <div id="header">
        <div id="logo">
          <h1>
            <a href="#"><span class="xebia-logo">X</span>events</a>
          </h1>

          <p>Xebian's Events Aggregator</p>
        </div>
        <div id="menu">
          <ul>
            <li class="current_page_item"><a id="stackoverflow" href="#">Stackoverflow</a></li>
            <li><a id="superuser" href="#">Super User</a></li>
            <li><a id="serverfault" href="#">Server Fault</a></li>
          </ul>
        </div>
      </div>
    </div>
    <!-- end #header -->
    <div id="page">
      <div id="page-bgtop">
        <div id="page-bgbtm">
          <div id="questions">
            <div id="questions-gen">

              <tiles:insertAttribute name="content" />
            </div>
            <div style="clear: both;">&nbsp;</div>
          </div>
          <!-- end #content -->
          <div id="sidebar">
            <ul>
              <li><tiles:insertTemplate template="menu.jsp" />
                <div style="clear: both;">&nbsp;</div>
              </li>
              <li>
                <h2>Project Pages</h2>
                <ul>
                  <li><a href="https://github.com/bguerout/xevents">GitHub</a></li>
                  <li><a href="https://github.com/bguerout/xevents/wiki">Wiki</a> (Backlog & Improvements
                    list)</li>
                  <li><a href="http://xebia.shapado.com">Xebia Shapado</a></li>
                </ul>
              </li>
              <li>
                <h2>Blogroll</h2>
                <ul>
                  <li><a href="blog.xebia.fr">Blog Xebia France</a></li>
                  <li><a href="blog.xebia.com">Blog Xebia</a></li>
                  <li><a href="www.xebia.fr">Xebia</a></li>
                </ul>
              </li>
            </ul>
          </div>
          <!-- end #sidebar -->
          <div style="clear: both;">&nbsp;</div>
        </div>
      </div>
    </div>
    <!-- end #page -->
  </div>
  <div id="footer">
    <p>
      Copyright (c) 2008 Sitename.com. All rights reserved. Design by <a href="http://www.freecsstemplates.org/">
        CSS Templates</a>.
    </p>
  </div>
  <!-- end #footer -->
</body>
</html>

