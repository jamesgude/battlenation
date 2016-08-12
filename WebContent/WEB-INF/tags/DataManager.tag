<%@tag isELIgnored="false" %>
<%@ tag import="com.google.gson.Gson" %>
<%@ tag import="com.businesshaps.oi.*" %>
<%@ tag import="com.businesshaps.am.businessobjects.*" %>
<%@ tag import="java.util.Collections" %>
<%@ tag import="java.util.Arrays" %><%@ tag import="java.lang.reflect.Field" %>
<%@ tag import="java.util.Comparator" %><%@ tag import="java.util.ArrayList" %>
<%@attribute name="attribute" type="java.lang.String" required="false"%>
<%@attribute name="doScript" type="java.lang.Boolean" required="false"%>
<%@attribute name="doTemplate" type="java.lang.Boolean" required="false"%>
<%@attribute name="type" type="java.lang.String" required="false"%>
<%@attribute name="sort" type="java.lang.String" required="false"%>
<%@attribute name="where" type="java.lang.String" required="false"%>
<%@attribute name="template" type="java.lang.String" required="false"%>
<%@attribute name="container" type="java.lang.String" required="false"%>
<%@attribute name="pageSize" type="java.lang.Integer" required="false"%>
<%@attribute name="startPage" type="java.lang.Integer" required="false"%>
<%
if (startPage==null) {
	startPage = 0;
}
if (attribute==null) {
	attribute = "list";
}
if (pageSize==null||pageSize==0) {
	pageSize = 20;
}
String qwhere = where;
if (qwhere==null) {
	qwhere = "";
} else {
	qwhere = "where " + qwhere;
}
sort = "order by id";
if (sort==null) {
}
ObInject oi = ObInject.getInstance();
Class cls = oi.getClassByName(type); 
int index = 0;

ArrayList<Object> list = new ArrayList<Object>();
if (cls!=null) {
	Object[] data = (Object[])oi.get(cls, qwhere + " " + sort);
	String fldName;
	for (Object o : data) {
		if (template!=null&&doTemplate!=null&&doTemplate) {
	        for (Field fld : cls.getDeclaredFields()) {
	            fldName = fld.getName();
	            fldName = Character.toUpperCase(fldName.charAt(0)) + fldName.substring(1);
	           	if (fld.getType().getName().equals("boolean")||fld.getType().getName().equals("java.lang.Boolean")) {
	                request.setAttribute(fldName.toLowerCase(), cls.getMethod("is" + fldName).invoke(o));
	            } else {
	                request.setAttribute(fldName.toLowerCase(), cls.getMethod("get" + fldName).invoke(o));
	            }
	           	
	        }
			%>
	
			<jsp:include page="${template}" />  
			<%
		}
		
		list.add(o);
		index ++; 
		if (index>=pageSize) {
			break; 
		}
	}
}

if (doScript!=null&&doScript) {
	%>
<script>
	 dataManagers["<%= type %>"] = new DataManager({pageNum:<%= startPage %>,pageSize:<%= pageSize %>, where:"<%= where %>", type:"<%= type %>", template:new JST({url:"<%= template %>", varName:"item"}), container:"<%= container %>"});
</script>
	<%
}
request.setAttribute(attribute, list.toArray(new Object[list.size()]));
%>
