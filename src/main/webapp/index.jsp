<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=application.getAttribute("appName")%> - Smart Job
	Portal</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">

</head>
<body>

	<!-- Navbar -->
	<%@include file="/includes/header.jsp"%>

	<main>
		<!-- Hero Section -->
		<div class="hero">
			<h1>Get Hired Smarter with AI</h1>
			<p>AI-powered resume analysis and smart job matching in one
				platform.</p>
			<a href="register.jsp" class="btn btn-cta mt-4">Get Started</a>
		</div>

		<!-- Features Section -->
		<div class="container py-5">
			<div class="row text-center">

				<div class="col-md-4 mb-4">
					<div class="feature-card h-100">
						<h4>🧠 AI Resume Insights</h4>
						<p>Let our AI analyze your resume and extract deep insights
							like skills, experience, and summary.</p>
					</div>
				</div>

				<div class="col-md-4 mb-4">
					<div class="feature-card h-100">
						<h4>🛠️ Skill Gap Analyzer</h4>
						<p>Identify missing skills by comparing your resume with job
							requirements — instantly.</p>
					</div>
				</div>

				<div class="col-md-4 mb-4">
					<div class="feature-card h-100">
						<h4>🎯 Smart Job Matching</h4>
						<p>Get job suggestions that best match your resume, skills,
							and goals — powered by intelligent AI.</p>
					</div>
				</div>

			</div>
		</div>
	</main>

	<!-- Footer -->
	<%@include file="includes/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>