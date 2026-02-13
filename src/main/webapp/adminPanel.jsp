<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="in.hiresense.models.UserPojo"%>
<%@ page import="in.hiresense.models.JobPojo"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Panel | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<%
	if (session.getAttribute("userId") == null || !"admin".equals(session.getAttribute("userRole"))) {
		response.sendRedirect("login.jsp");
		return;
	}
	%>
	<%@include file="includes/header.jsp"%>

	<main class="container py-5">
		<h2 class="mb-4">👑 Admin Dashboard</h2>

		<!-- Filter Users -->
		<div class="card bg-glass p-4 mb-4">
			<h5>👥 Filter Users</h5>
			<form method="get" action="adminPanel">
				<div class="row g-2 align-items-center">
					<div class="col-md-4">
						<input type="text" name="search" class="form-control text-black"
							placeholder="Search by name or email..." value="${param.search}" />
					</div>
					<div class="col-md-3">
						<select name="role" class="form-select text-black">
							<option value="">All Roles</option>
							<option value="user" ${param.role == 'user' ? 'selected' : ''}>Job
								Seeker</option>
							<option value="employer"
								${param.role == 'employer' ? 'selected' : ''}>Employer</option>
						</select>
					</div>
					<div class="col-md-3">
						<select name="status" class="form-select text-black">
							<option value="">All Status</option>
							<option value="active"
								${param.status == 'active' ? 'selected' : ''}>Active</option>
							<option value="blocked"
								${param.status == 'blocked' ? 'selected' : ''}>Blocked</option>
						</select>
					</div>
					<div class="col-md-2">
						<button type="submit" class="btn btn-login">Apply</button>
					</div>
				</div>
			</form>
		</div>

		<!-- Manage Users -->
		<div class="card bg-glass p-4 mb-5">
			<h5>📄 Users</h5>
			<table class="table text-white mt-3">
				<thead>
					<tr>
						<th>Name</th>
						<th>Email</th>
						<th>Role</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<%
					List<UserPojo> users = (List<UserPojo>) request.getAttribute("users");
					if (users != null && !users.isEmpty()) {
						for (UserPojo user : users) {
					%>
					<tr>
						<td><%=user.getName()%></td>
						<td><%=user.getEmail()%></td>
						<td><%=user.getRole().equals("user") ? "Job Seeker" : "Employer"%></td>
						<td><%=user.getStatus()%></td>
						<td>
							<%
							if ("active".equals(user.getStatus())) {
							%> <a href="BlockUserServlet?userId=<%=user.getId()%>"
							class="btn btn-sm btn-danger">Block</a> <%
 } else {
 %> <a href="UnblockUserServlet?userId=<%=user.getId()%>"
							class="btn btn-sm btn-success">Unblock</a> <%
 }
 %>
						</td>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="5" class="text-center text-warning">No users
							found.</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>

		<!-- Manage Jobs -->
		<div class="card bg-glass p-4">
			<h5>💼 Manage Job Listings</h5>
			<table class="table text-white mt-3">
				<thead>
					<tr>
						<th>Job Title</th>
						<th>Company</th>
						<th>Applicants</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<%
					List<JobPojo> jobs = (List<JobPojo>) request.getAttribute("jobs");
					if (jobs != null && !jobs.isEmpty()) {
						for (JobPojo job : jobs) {
					%>
					<tr>
						<td><%=job.getTitle()%></td>
						<td><%=job.getCompany()%></td>
						<td><%=job.getApplicantCount()%></td>
						<td><%=job.getStatus()%></td>
						<td><a href="RemoveJobServlet?jobId=<%=job.getId()%>"
							class="btn btn-sm btn-danger">Remove</a></td>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="4" class="text-center text-warning">No job
							listings found.</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>
	</main>

	<%@include file="includes/footer.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>