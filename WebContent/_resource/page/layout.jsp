<%@include file="/_resource/common/app.jsp" %>
<% if (top||direct) { %>
<!doctype html>
<html>
	<head>
		<%@include file="/_resource/common/head.jsp" %>
	</head>
	<body>
		<%} else if (!top||direct) {%>
	</body>
</html>
<%}%>   