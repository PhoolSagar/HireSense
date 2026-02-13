<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Resume Details | HireSense</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<%@ include file="includes/header.jsp"%>

	<main class="container py-5">
		<h2 class="mb-4">📄 Resume Analysis Details</h2>

		<%
		String error = (String) request.getAttribute("error");
		if (error != null) {
		%>
		<div class="alert alert-warning"><%=error%></div>
		<%
		} else {
		String summary = (String) request.getAttribute("summary");
		List<String> skills = (List<String>) request.getAttribute("skillsList");
		String personal = (String) request.getAttribute("personalDetails");
		String education = (String) request.getAttribute("education");
		String work = (String) request.getAttribute("workExperience");
		%>
		<div class="card mb-4">
			<div class="card-header bg-primary text-white">🔎 Personal
				Details</div>
			<div class="card-body"><%=personal != null ? personal : "No personal details available."%></div>
		</div>

		<div class="card mb-4">
			<div class="card-header bg-success text-white">🛠️ Skills</div>
			<div class="card-body">
				<ul>
					<%
					for (String skill : skills) {
					%>
					<li><%=skill%></li>
					<%
					}
					%>
				</ul>
			</div>
		</div>

		<div class="card mb-4">
			<div class="card-header bg-primary text-white">🔎 Summary</div>
			<div class="card-body"><%=summary != null ? summary : "No summary available."%></div>
		</div>

		<div class="card mb-4">
			<div class="card-header bg-primary text-white">🔎 Education</div>
			<div class="card-body"><%=education != null ? education : "No education available."%></div>
		</div>

		<div class="card mb-4">
			<div class="card-header bg-primary text-white">🔎 Work
				Experience</div>
			<div class="card-body"><%=work != null ? work : "No work experience available."%></div>
		</div>

		<%
		}
		%>
	</main>

	<%@ include file="includes/footer.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>