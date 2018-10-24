<%@page import="org.apache.jackrabbit.api.security.user.Group"%>
<%@page import="org.apache.sling.jcr.resource.JcrResourceUtil"%>
<%@page import="org.apache.sling.api.request.RequestParameter"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.jackrabbit.api.security.user.User"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.day.cq.wcm.foundation.forms.FormsHelper"%>
<%@page import="org.apache.jackrabbit.api.security.user.Authorizable"%>
<%@page import="org.apache.sling.jcr.base.util.AccessControlUtil"%>
<%@page import="org.apache.jackrabbit.api.security.user.UserManager"%>
<%@page import="org.apache.sling.jcr.api.SlingRepository"%>
<%@page import="org.apache.sling.api.request.RequestParameterMap"%>
<%@page session="false"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<%@include file="/libs/foundation/global.jsp"%>
<sling:defineObjects />
<%
log.info("Started");

String username = request.getParameter("username");
String password = request.getParameter("password");
String firstName = request.getParameter("firstName");
String lastName = request.getParameter("lastName");
String title = request.getParameter("title");

String groupName = properties.get("groupName", String.class);
String existsMsg = properties.get("existsMsg", "User already exists");

boolean error = false;
String errorMessage = "";

final SlingRepository repos = sling.getService(SlingRepository.class);
final Session session = repos.loginAdministrative(null);

try {
	UserManager userManager = AccessControlUtil.getUserManager(session);
	Authorizable authorizable = userManager.getAuthorizable(username);
	
	if(authorizable == null) {
		User user = userManager.createUser(username, password);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("firstName", "profile/givenName");
		map.put("lastName", "profile/familyName");
		map.put("title", "profile/title");
		
		RequestParameter value;
		String item;
		for (Map.Entry<String, RequestParameter[]> items : slingRequest.getRequestParameterMap().entrySet()) {
			item = map.get(items.getKey());
			
			if(item != null) {
				value = items.getValue()[0];
				log.info("******************************" + value);
				
				user.setProperty(item, JcrResourceUtil.createValue(value.getString(), session));
			}
		}
		
		Authorizable groupAuth = (Group) userManager.getAuthorizable(groupName);
		Group group;
		if(groupAuth == null) {
			group = userManager.createGroup(groupName);
		} else {
			group = (Group) groupAuth;
		}
		
		group.addMember(user);
		
		session.save();
		
	} else {
		error = true;
		errorMessage = existsMsg;
	}
} finally {
    if (session != null) {
        session.logout();
    }
}

log.info("Error ststua : " + error);
String path = properties.get("ThankYouPage", "");
if(error) {
	path = currentPage.getPath();
	log.info(path + ".html?errorMessage=" + errorMessage);
	response.sendRedirect(path + ".html?errorMessage=" + errorMessage);
} else if(path.equals("")) {
	path = currentPage.getPath();
	log.info(path + ".html");
	response.sendRedirect(path + ".html");
} else {
    if (path.indexOf(".") < 0) {
        path += ".html";
    }
    response.sendRedirect(path);
}
%>