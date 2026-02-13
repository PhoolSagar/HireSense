<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

<%@include file="includes/header.jsp" %>

<div class="login-container">
	<div class="login-card shadow">
		<h3 class="text-center mb-4">Welcome Back</h3>

		<% String error = (String) request.getAttribute("error"); %>
		<% if (error != null) { %>
			<div class="alert alert-danger text-center py-1"><%= error %></div>
		<% } %>

		<% if ("true".equals(request.getParameter("registered"))) { %>
			<div class="alert alert-success text-center py-1">✅ Registration successful. Please login.</div>
		<% } %>
		
		    <!-- FORGOT PASSWORD SUCCESS -->
        <%
        if ("resetSuccess".equals(request.getParameter("msg"))) {
        %>
            <div class="alert alert-success text-center py-2">
                ✅ Password reset successful. Please login.
            </div>
        <%
        }
        %>

		<form action="LoginServlet" method="post">
			<div class="mb-3">
				<input type="email" name="email" class="form-control"
					placeholder="Email" required />
			</div>
			<div class="mb-3">
				<input type="password" name="password" class="form-control"
					placeholder="Password" required />
			</div>
			<button type="submit" class="btn btn-login mt-2">Login</button>
		</form>
		
	 <div class="text-end mt-2 text-center">
    <a href="forgotPassword.jsp" class="text-warning">
        Forgot Password?
    </a>
</div>
	 

		<div class="text-center mt-3">
			<small>Don't have an account? <a href="register.jsp"
				class="text-warning">Register</a></small>
		</div>
	</div>
</div>



<%@include file="includes/footer.jsp" %>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>