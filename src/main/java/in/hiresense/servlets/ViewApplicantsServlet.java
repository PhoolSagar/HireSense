package in.hiresense.servlets;

import java.io.IOException;
import java.util.List;

import in.hiresense.dao.ApplicationDAO;
import in.hiresense.dao.JobDAO;
import in.hiresense.models.ApplicationPojo;
import in.hiresense.models.JobPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ViewApplicantsServlet")
public class ViewApplicantsServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !"employer".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {
            int jobId = Integer.parseInt(req.getParameter("jobId"));
            String status = req.getParameter("status") != null ? req.getParameter("status") : "applied";

            // Fetch job details for jobId
            JobPojo job = JobDAO.getJobById(jobId);
            if (job == null) {
                res.sendRedirect("employerDashboard?error=InvalidJob");
                return;
            }

            List<ApplicationPojo> applicants = ApplicationDAO.getApplicationsByJobAndStatus(jobId, status);

            req.setAttribute("job", job); // Send job object
            req.setAttribute("applicants", applicants);
            req.setAttribute("selectedStatus", status);

            req.getRequestDispatcher("viewApplicants.jsp").forward(req, res);

        } catch (Exception e) {
            throw new ServletException("Unable to fetch applicants or job details", e);
        }
    }
}