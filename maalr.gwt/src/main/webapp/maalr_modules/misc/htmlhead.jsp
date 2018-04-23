<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	if(session.getAttribute("locale") == null) {
		session.setAttribute("locale", request.getLocale().getLanguage());
	}
	if(request.getParameter("locale") != null) {
		session.setAttribute("locale", request.getParameter("locale"));
	}
%>

<!DOCTYPE html>

<html lang='<%=session.getAttribute("locale") %>'>

<fmt:setLocale value='<%=session.getAttribute("locale") %>'/>
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<head>

<title>${pageTitle}</title>

<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="Robots" content="INDEX,FOLLOW">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">

<link rel="shortcut icon" type="image/x-icon" href="${dictContext}/assets/img/favicon.ico">
<link rel="search" type="application/opensearchdescription+xml" title="<%=Configuration.getInstance().getLongName()%>" href="${dictContext}/static/searchplugin.xml">

<script type="text/javascript">
	var site_name= "<%=Configuration.getInstance().getLongName()%>"
</script>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="https://login.persona.org/include.js"></script>

<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/de.uni_koeln.spinfo.maalr.user.nocache.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/maalr-persona.js"></script>

<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/bootstrap.min.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/gwt-bootstrap.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/font-awesome.css" rel="stylesheet">
<link href="${dictContext}/assets/style/user.css" rel="stylesheet">
<link href="${dictContext}/assets/style/about.css" rel="stylesheet">

</head>
