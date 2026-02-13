package in.hiresense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.hiresense.dbutils.DBConnection;
import in.hiresense.models.JobPojo;

public class JobDAO {

	public static boolean postJob(JobPojo job) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.getConnection();
			String sql = "INSERT INTO jobs (title, description, skills, company, location, experience, package_lpa, vacancies, employer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, job.getTitle());
			ps.setString(2, job.getDescription());
			ps.setString(3, job.getSkills());
			ps.setString(4, job.getCompany());
			ps.setString(5, job.getLocation());
			ps.setString(6, job.getExperience());
			ps.setString(7, job.getPackageLpa());
			ps.setInt(8, job.getVacancies());
			ps.setInt(9, job.getEmployerId());
			int ans = ps.executeUpdate();
			return ans > 0;
		} finally {
			ps.close();
		}
	}

	public static List<JobPojo> getJobsByEmployer(int employerId, String search, String status, String sort)
			throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// Create a list to hold the resulting JobPojo objects
			List<JobPojo> list = new ArrayList<>();

			// Get a connection to the database
			conn = DBConnection.getConnection();

			// Base SQL query to fetch job details along with the count of applicants
			StringBuilder sql = new StringBuilder(
					"SELECT j.*, (SELECT COUNT(*) FROM applications a WHERE a.job_id = j.id) AS applicant_count "
							+ "FROM jobs j WHERE j.employer_id = ?");

			// Prepare list of parameters for the query
			List<Object> params = new ArrayList<>();
			params.add(employerId); // First parameter is always employerId

			// Add search condition if the search term is provided
			if (search != null && !search.trim().isEmpty()) {
				sql.append(" AND j.title LIKE ?");
				params.add("%" + search + "%");
			}

			// Add job status filter if provided
			if (status != null && !status.trim().isEmpty()) {
				sql.append(" AND j.status = ?");
				params.add(status);
			}

			// Handle sorting based on applicant count or created_at
			if ("asc".equalsIgnoreCase(sort)) {
				sql.append(" ORDER BY applicant_count ASC");
			} else if ("desc".equalsIgnoreCase(sort)) {
				sql.append(" ORDER BY applicant_count DESC");
			} else {
				sql.append(" ORDER BY j.created_at DESC");
			}

			// Prepare and bind parameters to the SQL statement
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			// Execute the query and process the result set
			rs = ps.executeQuery();
			while (rs.next()) {
				// Populate the JobPojo object with data from result set
				JobPojo job = new JobPojo();
				job.setId(rs.getInt("id"));
				job.setTitle(rs.getString("title"));
				job.setDescription(rs.getString("description"));
				job.setSkills(rs.getString("skills"));
				job.setCompany(rs.getString("company"));
				job.setLocation(rs.getString("location"));
				job.setExperience(rs.getString("experience"));
				job.setPackageLpa(rs.getString("package_lpa"));
				job.setVacancies(rs.getInt("vacancies"));
				job.setEmployerId(rs.getInt("employer_id"));
				job.setCreatedAt(rs.getTimestamp("created_at"));
				job.setStatus(rs.getString("status"));
				job.setApplicantCount(rs.getInt("applicant_count"));
				list.add(job); // Add job to the list
				
				
				System.out.println("Job Skills: " + job.getSkills());
			
			}

			// Return the final list of jobs
			return list;
		} finally {
			// Clean up resources to avoid memory leaks
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
		
			}
			
			
			
		}
	}

	public static void toggleJobStatus(int jobId) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBConnection.getConnection();

			// If current status is 'active', it becomes 'inactive', and vice versa
			String sql = "UPDATE jobs SET status = CASE WHEN status = 'active' THEN 'inactive' ELSE 'active' END WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, jobId);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public static List<JobPojo> getAllJobsWithEmployerAndApplicantCount() throws Exception {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        // Step 1: Establish database connection
	        conn = DBConnection.getConnection();

	        // Step 2: SQL query to fetch all jobs with applicant count
	        String sql = "SELECT j.*, " +
	                     "(SELECT COUNT(*) FROM applications a WHERE a.job_id = j.id) AS applicant_count " +
	                     "FROM jobs j";

	        // Step 3: Prepare and execute SQL statement
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();

	        // Step 4: Parse the result into a list of JobPojo objects
	        List<JobPojo> jobs = new ArrayList<>();
	        while (rs.next()) {
	            JobPojo job = new JobPojo();
	            job.setId(rs.getInt("id"));
	            job.setTitle(rs.getString("title"));
	            job.setCompany(rs.getString("company"));
	            job.setLocation(rs.getString("location"));
	            job.setExperience(rs.getString("experience"));
	            job.setPackageLpa(rs.getString("package_lpa"));
	            job.setEmployerId(rs.getInt("employer_id"));
	            job.setApplicantCount(rs.getInt("applicant_count"));
	            job.setCreatedAt(rs.getTimestamp("created_at"));
	            job.setStatus(rs.getString("status"));
	            jobs.add(job);
	        }

	        // Step 5: Return the populated job list
	        return jobs;

	    } finally {
	        // Step 6: Cleanup database resources
	        if (ps != null) {
				ps.close();
			}
	        if (rs != null) {
				rs.close();
			}
	    }
	}

	public static boolean deleteJob(int jobId) throws Exception {
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {
			// Step 1: Establish a database connection
			conn = DBConnection.getConnection();

			// Step 2: Begin transaction (disable auto-commit)
			conn.setAutoCommit(false);

			// Step 3: Delete related applications for the job
			String deleteAppsSql = "DELETE FROM applications WHERE job_id = ?";
			ps1 = conn.prepareStatement(deleteAppsSql);
			ps1.setInt(1, jobId);
			ps1.executeUpdate(); // Removes all applications for the given job

			// Step 4: Delete the job entry from jobs table
			String deleteJobSql = "DELETE FROM jobs WHERE id = ?";
			ps2 = conn.prepareStatement(deleteJobSql);
			ps2.setInt(1, jobId);
			int affectedRows = ps2.executeUpdate(); // Returns number of rows deleted (should be 1)

			// Step 5: Commit the transaction after both deletes are successful
			conn.commit();

			return affectedRows > 0;

		} catch (Exception e) {
			// Step 6: Rollback the transaction in case of any error
			if (conn != null) {
				conn.rollback();
			}
			throw e; // Rethrow exception after rollback
		} finally {
			// Step 7: Close prepared statements to release resources
			if (ps1 != null) {
				ps1.close();
			}
			if (ps2 != null) {
				ps2.close();
			}
		}
	}

	public static List<JobPojo> getAllJobsForUserDashboard(String search, String sort, String location,
			String experience, String packageLpa) throws Exception {

		List<JobPojo> jobs = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// Step 1: Establish DB connection
			Connection conn = DBConnection.getConnection();

			// Step 2: Build base SQL query to fetch active jobs and applicant count
			StringBuilder sql = new StringBuilder(
					"SELECT j.*, " + "(SELECT COUNT(*) FROM applications a WHERE a.job_id = j.id) AS applicant_count "
							+ "FROM jobs j WHERE j.status = 'active'");

			List<Object> params = new ArrayList<>();

			// Step 3: Add optional filters based on provided parameters

			if (search != null && !search.trim().isEmpty()) {
				sql.append(" AND (j.title LIKE ? OR j.company LIKE ?)");
				String keyword = "%" + search.trim() + "%";
				params.add(keyword);
				params.add(keyword);
			}

			if (location != null && !location.trim().isEmpty()) {
				sql.append(" AND j.location LIKE ?");
				params.add("%" + location.trim() + "%");
			}

			if (experience != null && !experience.trim().isEmpty()) {
				sql.append(" AND j.experience = ?");
				params.add(experience.trim());
			}

			if (packageLpa != null && !packageLpa.trim().isEmpty()) {
				sql.append(" AND j.package_lpa = ?");
				params.add(packageLpa.trim());
			}

			// Step 4: Add sorting logic
			if ("asc".equalsIgnoreCase(sort)) {
				sql.append(" ORDER BY j.vacancies ASC");
			} else if ("desc".equalsIgnoreCase(sort)) {
				sql.append(" ORDER BY j.vacancies DESC");
			} else {
				sql.append(" ORDER BY j.created_at DESC"); // Default: latest first
			}

			// Step 5: Prepare and execute SQL statement
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			// Step 6: Parse result set into JobPojo list
			while (rs.next()) {
				JobPojo job = new JobPojo();
				job.setId(rs.getInt("id"));
				job.setTitle(rs.getString("title"));
				job.setDescription(rs.getString("description"));
				job.setSkills(rs.getString("skills"));
				job.setCompany(rs.getString("company"));
				job.setVacancies(rs.getInt("vacancies"));
				job.setEmployerId(rs.getInt("employer_id"));
				job.setCreatedAt(rs.getTimestamp("created_at"));
				job.setStatus(rs.getString("status"));
				job.setLocation(rs.getString("location"));
				job.setExperience(rs.getString("experience"));
				job.setPackageLpa(rs.getString("package_lpa"));
				job.setApplicantCount(rs.getInt("applicant_count"));
				jobs.add(job);
			}

			return jobs;

		} finally {
			// Step 7: Close resources
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close(); // fixed mistake: was rs.close() earlier
			}
		}
	}

	public static JobPojo getJobById(int jobId) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM jobs WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, jobId);
			rs = ps.executeQuery();

			JobPojo job = null;
			if (rs.next()) {
				job = new JobPojo();
				job.setId(rs.getInt("id"));
				job.setTitle(rs.getString("title"));
				job.setDescription(rs.getString("description"));
				job.setSkills(rs.getString("skills"));
				job.setCompany(rs.getString("company"));
				job.setLocation(rs.getString("location"));
				job.setExperience(rs.getString("experience"));
				job.setPackageLpa(rs.getString("package_lpa"));
				job.setVacancies(rs.getInt("vacancies"));
				job.setEmployerId(rs.getInt("employer_id"));
				job.setCreatedAt(rs.getTimestamp("created_at"));
				job.setStatus(rs.getString("status"));
			}
			return job;
		} finally {
			rs.close();
			ps.close();
		}
	}

}