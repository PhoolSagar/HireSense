<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page
	import="in.hiresense.models.ApplicationPojo, in.hiresense.models.JobPojo"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Applicants | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
	rel="stylesheet">
<link href="/css/style.css"
	rel="stylesheet">
<style>
body {
    min-height: 100vh;
    display: flex;
    flex-direction: column;

    background: linear-gradient(to right, #4e54c8, #8f94fb);
    color: #212529;
}

main {
    flex: 1;
}


.applicant-card {
	background-color: white;
	border-radius: 1rem;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
	padding: 1.5rem;
	height: 100%;
	position: relative;
	transition: all 0.3s ease;
}

.applicant-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.match-badge {
	position: absolute;
	bottom: 15px;
	right: 15px;
	background-color: #007bff;
	color: white;
	font-size: 12px;
	padding: 4px 10px;
	border-radius: 12px;
}

 /* Make all cards glass style */
.card {
    background: rgba(255, 255, 255, 0.08) !important;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    color: white;
}

.small-btn {
	font-size: 0.8rem;
	padding: 3px 8px;
}

.text-dark p, .text-dark span, .text-dark strong {
	color: #000 !important;
}

  footer {
    background-color: rgba(0, 0, 0, 0.6);
    color: #eee;
    text-align: center;
    padding: 15px;
  }

  /* Animations */
  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(-20px); }
    to { opacity: 1; transform: translateY(0); }
  }

  @keyframes fadeInUp {
    from { opacity: 0; transform: translateY(30px); }
    to { opacity: 1; transform: translateY(0); }
  }

</style>
</head>
<body>
	<%@ include file="includes/header.jsp"%>
	<%
	if (session.getAttribute("userId") == null || !"employer".equals(session.getAttribute("userRole"))) {
		response.sendRedirect("login.jsp");
		return;
	}
	JobPojo job = (JobPojo) request.getAttribute("job");
	%>

	<main class="container py-5">
		<div class="card bg-white p-4 mb-4 border-0 shadow-sm rounded-4">
			<h4><%=job.getTitle()%>
				@
				<%=job.getCompany()%></h4>
			<p class="text-dark">
				<i class="bi bi-geo-alt"></i>
				<%=job.getLocation()%>
				&nbsp; <i class="bi bi-briefcase"></i>
				<%=job.getExperience()%>
				&nbsp; <i class="bi bi-currency-rupee"></i>
				<%=job.getPackageLpa()%>
				&nbsp; <i class="bi bi-people"></i>
				<%=job.getVacancies()%>
			</p>
		</div>

		<div class="card bg-white p-4 mb-4 border-0 shadow-sm rounded-4">
			<form method="get" action="ViewApplicantsServlet">
				<input type="hidden" name="jobId" value="<%=job.getId()%>" />
				<div class="row align-items-center">
					<div class="col-md-4">
						<label for="status" class="form-label">Filter by Status</label> <select
							name="status" class="form-select" onchange="this.form.submit()">
							<option value="applied"
								<%="applied".equals(request.getAttribute("selectedStatus")) ? "selected" : ""%>>Applied</option>
							<option value="shortlisted"
								<%="shortlisted".equals(request.getAttribute("selectedStatus")) ? "selected" : ""%>>Shortlisted</option>
							<option value="rejected"
								<%="rejected".equals(request.getAttribute("selectedStatus")) ? "selected" : ""%>>Rejected</option>
						</select>
					</div>
				</div>
			</form>
		</div>

		<h5 class="mb-3">👤 Applicants List</h5>
		<div class="row g-4">
			<%
			List<ApplicationPojo> applicants = (List<ApplicationPojo>) request.getAttribute("applicants");
			if (applicants != null && !applicants.isEmpty()) {
				for (ApplicationPojo a : applicants) {
			%>
			<div class="col-md-6 col-lg-4">
				<div class="applicant-card">
					<h6 class="fw-bold">
						👤 User ID:
						<%=a.getUserId()%></h6>
					<p class="mb-1">
						<i class="bi bi-bar-chart"></i> <strong>Status:</strong> <span
							class="text-capitalize"><%=a.getStatus()%></span>
					</p>
					<p class="mb-1">
						<i class="bi bi-calendar-check"></i> <strong>Applied:</strong>
						<%=a.getAppliedAt()%></p>
					<p class="mb-1">
    <strong>Resume:</strong>

    <%
    if (a.getResumePath() != null && !a.getResumePath().isEmpty()) {
    %>

    <a href="<%=request.getContextPath()%>/DownloadResume?file=<%=new java.io.File(a.getResumePath()).getName()%>"
       class="btn btn-outline-primary btn-sm"
       target="_blank">
        Download
    </a>

    <%
    } else {
    %>
        <span class="text-danger">No Resume</span>
    <%
    }
    %>
</p>


					<form method="post" action="UpdateApplicationStatusServlet"
						class="d-flex gap-2 mt-3">
						<input type="hidden" name="appId" value="<%=a.getId()%>" /> <input
							type="hidden" name="jobId" value="<%=job.getId()%>" />
						<button type="submit" name="status" value="shortlisted"
							class="btn btn-success btn-sm small-btn">Shortlist</button>
						<button type="submit" name="status" value="rejected"
							class="btn btn-danger btn-sm small-btn">Reject</button>
					</form>
					<form action="ResumeDetailServlet" method="get">
						<input type="hidden" name="userId" value="<%=a.getUserId()%>">
						<button type="submit"
							class="btn btn-secondary btn-sm small-btn mt-2">
							<i class="bi bi-eye"></i> View Full Details
						</button>
					</form>
					<div class="match-badge">
						Match:
						<%=a.getScore()%>%
					</div>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="col-12">
				<div class="alert alert-warning text-center">No applicants
					found for this status.</div>
			</div>
			<%
			}
			%>
		</div>
	</main>

	<%@ include file="includes/footer.jsp"%>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
