<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription.Language" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />


<div id="navi_head">
	<div id="brand_title">
		<a class="brand active" href="/"><%=Configuration.getInstance().getLongName()%></a>
	</div>
	<%-- MAIN MENU --%>
    <ul class="left">
     <li> <%@include file="/maalr_modules/misc/login_widget.jsp"%></li>
     <li><a id="navTemplate" title="about" href="${dictContext}/about.html"><fmt:message key="maalr.navi.about" /></a></li>
       
    </ul>
	<%-- LANGUAGE SELECTION --%>	
	<ul class="right">
		<li><a href="?locale=fa" class="<%=(session.getAttribute("locale").equals("fa"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.persian" /></a></li>
		<li><a href="?locale=en" class="<%=(session.getAttribute("locale").equals("en"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.english" /></a></li>
		<li><a href="?locale=de" class="<%=(session.getAttribute("locale").equals("de"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.german" /></a></li>
	</ul>
	
</div>



