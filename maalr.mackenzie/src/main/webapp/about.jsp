<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer" %>
<%@ page import="java.util.Locale" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />


<%-- HTML HEADER --%>
<%@ include file="/maalr_modules/misc/htmlhead.jsp" %>

	<body>
	
		<div id="top">
			<%@ include file="/maalr_modules/misc/header.jsp" %>
		</div>
		
		<div class="content">
			<%-- CONTENT AREA --%>
			<div id="about_frame">
			
                <div class="container_full">
                
                    <%-- About --%>
                
		            <div class="title" id="about_title"><h2><fmt:message key="about.title"/></h2></div>					
					<div class="exp" id="about_content"><fmt:message key="about.content"/></div>
					
					 <%-- General description --%>
					
					<div class="subtitle" id="general_title"><h3><fmt:message key="general.title"/></h3></div>
					<div class="exp" id="general_content"><fmt:message key="general.content"/></div>
					
					
					<%-- Entry's Structure --%>	
					
					<div class="subtitle" id="entry_title"><h3><fmt:message key="entry.title"/></h3></div>
					
						<%-- Printed Entry --%>						
						<div class="section"><h4><fmt:message key="entry.printed.title"/></h4></div>
						<div class ="dict_img"><span class ="anchor" id="img_printed"></span>
						<figure>
						<img class="img_dict" src="${dictContext}/assets/img/cachiyuyo_printed.png" alt="img_entry_printed" width="600px" height="300px"> 
						</figure>
						</div>
						<div class="img_desc"><fmt:message key="entry.printed.img.description"/></div>
						<div class="exp"><fmt:message key="entry.printed.content"/></div>
						
						<%-- Devochdelia Entry --%>	
						<div class="section"><h4><fmt:message key="entry.devochdelia.title"/></h4></div>
						<div class ="dict_img">
						<figure>
						<img class="img_dict" src="${dictContext}/assets/img/cachiyuyo_devochdelia_<%=session.getAttribute("locale")%>.png" alt="img_entry_devochdelia" width="600px" height="300px"> 
						</figure>
						</div>
						<div class="img_desc"><fmt:message key="entry.devochdelia.img.description"/></div>
						<div class="exp"><fmt:message key="entry.devochdelia.content"/></div>
						
					<%-- Abbreviations --%>	
						<div class="subtitle"><h4><fmt:message key="entry.abbr.title"/></h4></div>
						<div class="exp"><fmt:message key="entry.abbr.content"/></div>
						
						
					<%-- Signs --%>	
						<div class="subtitle"><h4><fmt:message key="entry.signs.title"/></h4></div>
						<div class="exp"><fmt:message key="entry.signs.description"/></div>

	
					<%-- Search --%>
									
					<div class="subtitle" id="search_title"><h3><fmt:message key="search.title"/></h3></div>
					<div class="exp" id="search_content"><fmt:message key="search.content"/></div>
					
					
										
					<%-- Correct --%>		
					<div class="subtitle" id="correct_title"><h3><fmt:message key="correct.title"/></h3></div>
					<div class ="dict_img">
					<figure>
						<img class="img_dict" src="${dictContext}/assets/img/peumo_editor_<%=session.getAttribute("locale")%>.png" alt="img-editor" width="600px" height="300px">
					</figure>	
				     </div>
				    <div class="img_desc"><fmt:message key="correct.img.description"/></div>
					<div class="exp" id="correct_content">
						<fmt:message key="correct.content"/>
					</div>
					
					<%-- Contact --%>		
					<div class="subtitle" id="correct_title"><h3><fmt:message key="contact.title"/></h3></div>
					<div class="exp">
						<fmt:message key="contact.content"/>
					</div>
					
					
					
					
				</div>
			</div>
		</div>
		
		<%@ include file="/maalr_modules/misc/footer.jsp"%>

		<%-- GWT AJAX BROWSER HISTORY SUPPORT --%>
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
		
	</body>
</html>