<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="<%=session.getAttribute("pl")%>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />


<%-- FALLBACK LOCALE: accept "es", otherwise force to "en" --%>
	<%
		if(!session.getAttribute("pl").equals("es")) { 
			session.setAttribute("pl","en");
		} 
	%>

<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?pl=es" class="<%=(session.getAttribute("pl").equals("es"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.spanish" /></a></li>
		<li><a href="?pl=en" class="<%=(session.getAttribute("pl").equals("en"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.english" /></a></li>
	</ul>
</div>