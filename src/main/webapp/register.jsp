<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">

<script>
	function showSpinner() {
		document.querySelector(".btn-login").disabled = true;
		document.getElementById("loadingSpinner").classList.remove("d-none");
	}
</script>
</head>

<body>

<%@include file="includes/header.jsp"%>

<div class="login-container">
	<div class="login-card shadow">

		<h3 class="text-center mb-4">Create Your Account</h3>

		<!-- SUCCESS MESSAGE -->
		<%
		if ("registered".equals(request.getParameter("success"))) {
		%>
			<div class="alert alert-success text-center">✅ Registration successful. Please login.</div>
		<%
		}
		%>

		<!-- DUPLICATE EMAIL ERROR -->
		<%
		if ("1".equals(request.getParameter("duplicate"))) {
		%>
			<div class="alert alert-danger text-center">
				⚠ Email already registered. Please login instead.
			</div>
		<%
		}
		%>

		<!-- SERVER ERROR -->
		<%
		if ("server".equals(request.getParameter("error"))) {
		%>
			<div class="alert alert-danger text-center">
				❌ Something went wrong. Try again.
			</div>
		<%
		}
		%>

		<!-- Spinner -->
		<div id="loadingSpinner" class="text-center d-none mt-3">
			<div class="spinner-border text-light" role="status"></div>
			<p class="mt-2">Sending OTP, please wait...</p>
		</div>

		<form action="SendRegisterOTPServlet" method="post" onsubmit="showSpinner()">
			<div class="mb-3">
				<input type="text" name="name" class="form-control"
					placeholder="Full Name" required />
			</div>

			<div class="mb-3">
				<input type="email" name="email" class="form-control"
					placeholder="Email" required />
			</div>

			<div class="mb-3">
				<input type="password" name="password" class="form-control"
					placeholder="Password" required />
			</div>

			<div class="mb-3">
				<select name="role" class="form-select" required>
					<option value="">Select Role</option>
					<option value="user">Job Seeker</option>
					<option value="employer">Employer</option>
				</select>
			</div>

			<button type="submit" class="btn btn-login mt-2">Register</button>
		</form>

		<div class="text-center mt-3">
			<small>Already have an account? 
				<a href="login.jsp" class="text-warning">Login</a>
			</small>
		</div>

	</div>
</div>


<!-- ================= OTP MODAL ================= -->
<%
if ("true".equals(request.getParameter("showOtp"))) {
%>
<div class="modal fade show" tabindex="-1" style="display: block;">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content bg-dark text-white">

			<div class="modal-header">
				<h5 class="modal-title">Enter OTP sent to your email</h5>
				<button type="button" class="btn-close btn-close-white"
					onclick="window.location.href='register.jsp'"></button>
			</div>

			<form action="VerifyRegisterOTPServlet" method="post">

				<div class="modal-body">

					<%
					if ("invalid".equals(request.getParameter("error"))) {
					%>
						<div class="alert alert-danger text-center">
							❌ Invalid OTP. Please try again.
						</div>
					<%
					}
					%>

					<input type="text" name="otp" class="form-control mt-2"
						placeholder="Enter OTP" required />
				</div>

				<div class="modal-footer">
					<button type="submit" class="btn btn-success w-100">
						Verify & Register
					</button>
				</div>

			</form>
		</div>
	</div>
</div>

<script>
	document.body.classList.add('modal-open');
</script>
<%
}
%>


<%@include file="includes/footer.jsp"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
