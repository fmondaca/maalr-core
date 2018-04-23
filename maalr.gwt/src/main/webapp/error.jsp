<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@	page import="java.util.ArrayList" %>
<%@	page import="java.util.List" %>

<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale") %>'/>
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- HTML HEADER --%>
<%@ include file="/maalr_modules/misc/htmlhead.jsp" %>

	<body>
		
		<%-- NAVIGATION --%>
		<%@ include file="/maalr_modules/misc/header.jsp" %>
		
		<%-- FOOTER --%>
		<%@ include file="/maalr_modules/misc/footer.jsp" %>
		
		<div>
+			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
			
			<%-- INTERNAL SIGN IN --%>
			<div class="container well">
				Sorry, but an unexpected error occurred.
			</div>
		</div>

	</body>
</html>