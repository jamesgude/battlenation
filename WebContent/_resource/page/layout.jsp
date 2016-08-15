<%@include file="/_resource/common/app.jsp" %>
<% if (top||direct) { %>
<!doctype html>
<html>
	<head>
		<%@include file="/_resource/common/head.jsp" %>
	</head>
	<body>
		<jsp:include page="/components/header/header.jsp" />
		<div class="page-body">
			<jsp:include page="/components/leftbar/leftbar.jsp" />
			<div class="content-body">
				<%} else if (!top||direct) {%>
			</div>
			<jsp:include page="/components/rightbar/rightbar.jsp" />
			<jsp:include page="/components/footer/footer.jsp" />
		</div>
			
	</body>
</html>
<%}%>    