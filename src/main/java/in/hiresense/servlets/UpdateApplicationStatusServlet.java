package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.ApplicationDAO;
import in.hiresense.dao.JobDAO;
import in.hiresense.dao.UserDAO;
import in.hiresense.models.JobPojo;
import in.hiresense.models.UserPojo;
import in.hiresense.utils.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateApplicationStatusServlet")
public class UpdateApplicationStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null ||
                !"employer".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {

            int appId = Integer.parseInt(req.getParameter("appId"));
            String newStatus = req.getParameter("status");
            int jobId = Integer.parseInt(req.getParameter("jobId"));

            if (!newStatus.equals("shortlisted") &&
                !newStatus.equals("rejected")) {

                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId);
                return;
            }

            boolean updated = ApplicationDAO.updateApplicationStatus(appId, newStatus);

            if (updated) {

                // ✅ Get applicant userId
                int applicantUserId = ApplicationDAO.getUserIdByApplicationId(appId);

                // ✅ Fetch applicant details
                UserPojo applicant = UserDAO.getUserById(applicantUserId);

                // ✅ Fetch job details
                JobPojo job = JobDAO.getJobById(jobId);

                // ✅ Send email to applicant
                try {
                    MailUtil.sendApplicationStatusUpdate(
                            applicant.getName(),
                            applicant.getEmail(),
                            job.getTitle(),
                            job.getCompany(),
                            newStatus
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId + "&status=" + newStatus);

            } else {
                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId + "&error=update_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp");
        }
    }
}
