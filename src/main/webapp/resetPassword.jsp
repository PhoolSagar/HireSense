<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Verify OTP | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

<%@ include file="includes/header.jsp" %>

<%
/* Security check */
if (session.getAttribute("resetEmail") == null) {
	response.sendRedirect("forgotPassword.jsp");
	return;
}
%>

<div class="login-container">
	<div class="login-card shadow">
		<h3 class="text-center mb-4">Enter OTP & New Password</h3>

		<!-- Invalid OTP -->
		<%
		if ("invalid".equals(request.getParameter("error"))) {
		%>
			<div class="alert alert-danger text-center py-2">
				❌ Invalid OTP. Try again.
			</div>
		<%
		}
		%>

		<form action="ResetPasswordServlet" method="post">
			
			<div class="mb-3">
				<input type="text" name="otp" class="form-control"
					placeholder="Enter OTP" required />
			</div>

			<div class="mb-3">
				<input type="password" name="newPassword" class="form-control"
					placeholder="Enter New Password" required />
			</div>

			<button type="submit" class="btn btn-success w-100">
				Reset Password
			</button>
		</form>

	</div>
</div>

<%@ include file="includes/footer.jsp" %>

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
