<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription.Language" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>'/>
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?pl=fa" class="<%=(session.getAttribute("pl").equals("fa"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.persian" /></a></li>
		<li><a href="?pl=en" class="<%=(session.getAttribute("pl").equals("en"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.english" /></a></li>
		<li><a href="?pl=de" class="<%=(session.getAttribute("pl").equals("de"))?"lang_select active":"lang_select"%>"><fmt:message key="maalr.langSelect.german" /></a></li>
	</ul>
</div>