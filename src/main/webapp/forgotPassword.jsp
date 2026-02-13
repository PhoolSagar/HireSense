<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Forgot Password</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>

<body>

<%@ include file="includes/header.jsp" %>

<div class="login-container">
    <div class="login-card shadow">

        <h3 class="text-center mb-4">Forgot Password</h3>

        <% String error = request.getParameter("error"); %>

        <% if ("notFound".equals(error)) { %>
            <div class="alert alert-danger">Email not registered.</div>
        <% } %>

        <% if ("server".equals(error)) { %>
            <div class="alert alert-danger">Server error. Try again.</div>
        <% } %>

        <% if (request.getParameter("showOtp") == null) { %>

        <!-- STEP 1 -->
        <form action="SendResetOTPServlet" method="post">
            <div class="mb-3">
                <input type="email" name="email" class="form-control"
                    placeholder="Enter Registered Email" required>
            </div>

            <button type="submit" class="btn btn-login w-100">
                Send OTP
            </button>
        </form>

        <% } else { %>

        <!-- STEP 2 -->
        <% if ("invalid".equals(error)) { %>
            <div class="alert alert-danger">Invalid OTP</div>
        <% } %>

        <form action="ResetPasswordServlet" method="post">
            <div class="mb-3">
                <input type="text" name="otp" class="form-control"
                    placeholder="Enter OTP" required>
            </div>

            <div class="mb-3">
                <input type="password" name="newPassword" class="form-control"
                    placeholder="Enter New Password" required>
            </div>

            <button type="submit" class="btn btn-success w-100">
                Reset Password
            </button>
        </form>

        <% } %>

        <div class="text-center mt-3">
            <a href="login.jsp" class="text-warning">Back to Login</a>
        </div>

    </div>
</div>

<%@ include file="includes/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
