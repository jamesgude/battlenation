<%@page import="com.businesshaps.am.businessobjects.*" %> 
<%@page import="com.businesshaps.am.*" %>
<%@page import="com.businesshaps.oi.*" %>  
<%
AppUser user = SessionHandler.getSessionUser(session);
boolean direct = request.getParameter("top")==null;
boolean top = (request.getParameter("top") != null && Boolean.parseBoolean(request.getParameter("top")));
String sp = (String)request.getAttribute("javax.servlet.forward.request_uri");
if (sp==null) {
	sp = request.getServletPath();
}


String nav = (String)request.getAttribute("navigation");
String subnav = (String)request.getAttribute("section_navigation");
String organizationKey = request.getParameter("organizationKey");
AppUserOrganization userOrg = null; 
if (organizationKey==null) {
	organizationKey = (String)session.getAttribute("organizationKey");
} else {
}

 
ObInject oi = ObInject.getInstance();
if (top||direct) {
	if (user.getId()>0) {
		AppUserOrganization[] userOrgs = (AppUserOrganization[]) oi.get(AppUserOrganization.class, "where email = '"+user.getEmail() +"'"); 

		if (userOrgs.length>0) {
			//userOrg = userOrgs[0];
		} else {
			AppUserOrganization nuserOrg = new AppUserOrganization(); 
			nuserOrg.setOrganizationKey("organization_" + user.getId());
			nuserOrg.setEmail(user.getEmail());
			nuserOrg.setWorkScheduleId(1);
				oi.set(nuserOrg);
		}

	}
	AppUserOrganization[] userOrgs = (AppUserOrganization[]) oi.get(AppUserOrganization.class, "where email = '"+user.getEmail() +"'");
	if (user.getId()>0) {
		for (AppUserOrganization uo : userOrgs) {
			userOrg = uo;
			
			if (organizationKey==null||uo.getOrganizationKey().equals(organizationKey)) {
				organizationKey = uo.getOrganizationKey();
				break;
			}
		}
	} else {
		userOrg = new AppUserOrganization();
	}
 
	request.setAttribute("organizationKey", organizationKey);
	session.setAttribute("organizationKey", organizationKey);
	request.setAttribute("userOrg", userOrg);
	session.setAttribute("userOrg", userOrg);
}
%>