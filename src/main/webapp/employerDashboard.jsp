<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employer Dashboard | <%=application.getAttribute("appName")%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">

<!-- ✅ SweetAlert2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</head>

<style>
 /* Make all cards glass style */
.card {
    background: rgba(255, 255, 255, 0.08) !important;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    color: white;
}
  
</style>
<body>

	<%@include file="includes/header.jsp"%>

	<%
	if ((session.getAttribute("userId") == null)
			|| !"employer".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
		response.sendRedirect("login.jsp");
		return;
	}
	%>

	<main class="container py-5">
		<h2 class="mb-4">
			Welcome,
			<%=session.getAttribute("userName")%>
			👔
		</h2>

		<!-- ================= POST JOB FORM ================= -->
		<div class="card bg-glass p-4 mb-5">
			<h5>📢 Post a New Job</h5>

			<form action="PostJobServlet" method="post">

				<div class="mb-3">
					<input type="text" name="title" class="form-control text-black"
						placeholder="Job Title" required />
				</div>

				<div class="mb-3">
					<textarea name="description" class="form-control text-black"
						placeholder="Job Description" rows="3" required></textarea>
				</div>

				<div class="mb-3">
					<input type="text" name="skills" class="form-control text-black"
						placeholder="Required Skills (comma-separated)" required />
				</div>

				<div class="mb-3">
					<input type="text" name="company" class="form-control text-black"
						placeholder="Company Name" required />
				</div>

				<div class="mb-3">

					<!-- LOCATION SELECT -->
					<select id="locationSelect" name="location" class="form-select"
						required>
						<option disabled selected value="">Select Location</option>
						<option value="Bangalore">Bangalore</option>
						<option value="Mumbai">Mumbai</option>
						<option value="Pune">Pune</option>
						<option value="Delhi">Delhi</option>
						<option value="Other">Other</option>
					</select>

					<!-- CUSTOM INPUT (became same place, not below) -->
					<div id="customLocationContainer"
						style="display: none; margin-top: 10px;">
						<div class="input-group">
							<button type="button" id="backToSelect" class="btn border">⮌</button>

							<input type="text" id="customLocation" name="customLocation"
								class="form-control" placeholder="Enter custom location" />
						</div>
					</div>

				</div>

				<div class="mb-3">
					<select name="experience" class="form-select text-black" required>
						<option value="">Select Experience</option>
						<option>Fresher</option>
						<option>0 - 1 year</option>
						<option>1 - 2 years</option>
						<option>2 - 3 years</option>
						<option>3 - 5 years</option>
						<option>5+ years</option>
					</select>
				</div>

				<div class="mb-3">
					<select name="packageLpa" class="form-select text-black" required>
						<option value="">Select Package</option>
						<option>1-2 Lacs P.A.</option>
						<option>2-3 Lacs P.A.</option>
						<option>3-4 Lacs P.A.</option>
						<option>4-5 Lacs P.A.</option>
						<option>5-10 Lacs P.A.</option>
						<option>10-20 Lacs P.A.</option>
						<option>Not disclosed</option>
					</select>
				</div>

				<div class="mb-3">
					<input type="number" name="vacancies" min="1"
						class="form-control text-black" placeholder="Number of Vacancies"
						required />
				</div>

				<button type="submit" class="btn btn-login">Post Job</button>

			</form>
		</div>


		<!-- ================= JOB LIST ================= -->
		<%
		java.util.List<in.hiresense.models.JobPojo> jobList = (java.util.List<in.hiresense.models.JobPojo>) request.getAttribute("jobList");

		if (jobList != null && !jobList.isEmpty()) {
		%>

		<div class="card bg-glass p-4">
			<h5>
				📄
				<%=jobList.get(0).getCompany()%>'s Posted Jobs
			</h5>

			<table class="table text-white mt-3">
				<thead>
					<tr>
						<th>Job Title</th>
						<th>Applicants</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				</thead>

				<tbody>
					<%
					for (in.hiresense.models.JobPojo job : jobList) {
					%>

					<tr>
						<td><%=job.getTitle()%></td>
						<td><%=job.getApplicantCount()%></td>
						<td><%=job.getStatus().toUpperCase()%></td>
						<td><a href="ViewApplicantsServlet?jobId=<%=job.getId()%>"
							class="btn btn-sm btn-info">View</a> 
							<a
							href="ToggleJobStatusServlet?jobId=<%=job.getId()%>"
							class="btn btn-sm <%="active".equals(job.getStatus()) ? "btn-warning" : "btn-success"%>">
								<%="active".equals(job.getStatus()) ? "Deactivate" : "Activate"%>
						</a></td>
					</tr>

					<%
}
%>
				</tbody>
			</table>
		</div>

		<%
} else {
%>
		<p class="text-center text-white">No jobs posted yet.</p>
		<%
}
%>

	</main>

	<%@include file="includes/footer.jsp"%>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


	<!-- ================= SWEET ALERT ================= -->

	<script>
		const params = new URLSearchParams(window.location.search);

		if (params.get("success") === "1") {
			Swal.fire({
				icon : 'success',
				title : 'Job Posted!',
				text : 'Your job has been posted successfully.',
				timer : 2000,
				showConfirmButton : false
			});
			window.history.replaceState({}, document.title,
					window.location.pathname);
		}

		if (params.get("error") === "1") {
			Swal.fire({
				icon : 'error',
				title : 'Error!',
				text : 'Something went wrong. Please try again.',
				confirmButtonText : 'Okay'
			});
			window.history.replaceState({}, document.title,
					window.location.pathname);
		}
	</script>
	
	<!-- ================= Below section for custom location ================= -->

	<script>
(function() {

    const selectBox = document.getElementById('locationSelect');
    const customContainer = document.getElementById('customLocationContainer');
    const customInput = document.getElementById('customLocation');
    const backBtn = document.getElementById('backToSelect');

    function checkSelection() {
        if (selectBox.value === 'Other') {
            // Hide select
            selectBox.style.display = 'none';

            // Show input
            customContainer.style.display = 'block';
            customInput.setAttribute('required', 'required');
            customInput.focus();
        } else {
            // Show select
            selectBox.style.display = 'block';

            // Hide input
            customContainer.style.display = 'none';
            customInput.removeAttribute('required');
            customInput.value = "";
        }
    }

    selectBox.addEventListener("change", checkSelection);

    // Back button → go back to dropdown
    backBtn.addEventListener("click", () => {
        selectBox.value = "";
        checkSelection();
    });

})();
</script>


	<!-- ================= Above Section for custom location ================= -->
	

</body>
</html>
