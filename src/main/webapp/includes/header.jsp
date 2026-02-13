<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow">
	<div class="container-fluid">
		<a class="navbar-brand fw-bold"
			href="${pageContext.request.contextPath}/index.jsp"> <%=application.getAttribute("appName")%>
		</a>

		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ms-auto">
				<%
				String role = (String) session.getAttribute("userRole");
				if (role == null) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/register.jsp">Register</a>
				</li>
				<%
				} else if ("user".equals(role)) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/userDashboard">Dashboard</a>
				</li>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
				</li>
				<%
				} else if ("employer".equals(role)) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/employerDashboard">Dashboard</a>
				</li>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
				</li>
				<%
				} else if ("admin".equals(role)) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/adminPanel">Admin
						Panel</a></li>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
				</li>
				<%
				}
				%>
			</ul>
		</div>
	</div>
</nav>