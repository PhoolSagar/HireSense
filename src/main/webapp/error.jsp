<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error | <%= application.getAttribute("appName") %></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

<%@ include file="includes/header.jsp" %>

<div class="login-container">
	<div class="login-card shadow text-center text-danger">
		<h3 class="mb-3">🚨 Something went wrong!</h3>

		<p class="text-white">We're sorry, an unexpected error occurred.</p>

		<% if (exception != null) { %>
			<div class="alert alert-danger text-start mt-3">
				<strong>Error Details:</strong><br>
				<%= exception.getClass().getName() %><br>
				<%= exception.getMessage() %>
			</div>
		<% } else { %>
			<div class="alert alert-warning">An unknown error occurred.</div>
		<% } %>

		<a href="index.jsp" class="btn btn-outline-light mt-3">🔙 Back to Home</a>
	</div>
</div>

<%@ include file="includes/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>