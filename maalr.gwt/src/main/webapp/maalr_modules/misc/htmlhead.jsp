<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<%
	if(session.getAttribute("pl") == null) {
		session.setAttribute("pl", request.getLocale().getLanguage());
	}
	if(request.getParameter("pl") != null) {
		session.setAttribute("pl", request.getParameter("pl"));
	}
%>

<!DOCTYPE html>

<html lang='<%=session.getAttribute("pl") %>'>

<fmt:setLocale value='<%=session.getAttribute("pl") %>'/>
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<head>

<title>${pageTitle}</title>

<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta http-equiv="content-type" content="text/html; charset=UTF8">
<meta name='robots' content='index, follow'>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name='description' content='DEVOCHDELIA | Diccionario Etimológico de las Voces Chilenas Derivadas de las Lenguas Indígenas Americanas (1910), compilado por Rodolfo Lenz'>
<meta name='keywords' content='mapuche,mapudungun,mapudungún,mapudungu,quechua,indigena,indígena,lengua,lenguaje,espanol,español,chile,chileno,chilenismo,chilenismos,americanismos,diccionario,diccionario etimolojico,diccionario etimolójico,etimolojía,lenz,rodolfo lenz,rudolf lenz,devochdelia'>
<meta property="title" content="DEVOCHDELIA | Diccionario Etimológico de las Voces Chilenas Derivadas de las Lenguas Indígenas Americanas">
<meta property="og:title" content="DEVOCHDELIA | Diccionario Etimológico de las Voces Chilenas Derivadas de las Lenguas Indígenas Americanas">
<meta property='og:site_name' content='devochdelia.cl'>



<link rel="shortcut icon" type="image/x-icon" href="${dictContext}/assets/img/favicon.ico">
<link rel="search" type="application/opensearchdescription+xml" title="<%=Configuration.getInstance().getLongName()%>" href="${dictContext}/static/searchplugin.xml">


<script type="text/javascript">
	var site_name= "<%=Configuration.getInstance().getLongName()%>"
</script>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/de.uni_koeln.spinfo.maalr.user.nocache.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/bootstrap.min.js"></script>

<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/bootstrap.min.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/gwt-bootstrap.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/font-awesome.css" rel="stylesheet">
<link href="${dictContext}/assets/style/user.css" rel="stylesheet">
<link href="${dictContext}/assets/style/about.css" rel="stylesheet">


<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-92028738-1', 'auto');
  ga('send', 'pageview');

</script>


</head>
