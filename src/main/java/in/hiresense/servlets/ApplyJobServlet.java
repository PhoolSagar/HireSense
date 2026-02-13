package in.hiresense.servlets;

import java.io.IOException;
import java.util.List;

import in.hiresense.dao.ApplicationDAO;
import in.hiresense.dao.JobDAO;
import in.hiresense.dao.ResumeAnalysisLogDAO;
import in.hiresense.dao.UserDAO;
import in.hiresense.models.ApplicationPojo;
import in.hiresense.models.JobPojo;
import in.hiresense.models.ResumeAnalysisLogPojo;
import in.hiresense.models.UserPojo;
import in.hiresense.utils.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ApplyJobServlet")
public class ApplyJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null
                || !"user".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int jobId = Integer.parseInt(req.getParameter("jobId"));
        double score = Double.parseDouble(req.getParameter("score"));

        try {

            // ✅ Get latest resume path from analysis log
            String resumePath = "N/A";
            List<ResumeAnalysisLogPojo> logs = ResumeAnalysisLogDAO.getLogsByUser(userId);

            if (logs != null && !logs.isEmpty()) {
                String resultJson = logs.get(0).getResultJson();
                org.json.JSONObject obj = new org.json.JSONObject(resultJson);
                org.json.JSONObject data = obj.getJSONObject("data");
                resumePath = data.optString("resumePath", "N/A");
            }

            // ✅ Save application in DB
            ApplicationPojo app = new ApplicationPojo(
                    0, userId, jobId, resumePath, score, "applied", null
            );

            ApplicationDAO.apply(app);

            // ✅ Fetch required data BEFORE thread
            UserPojo user = UserDAO.getUserById(userId);
            JobPojo job = JobDAO.getJobById(jobId);
            UserPojo employer = UserDAO.getUserById(job.getEmployerId());

            // ✅ Send Emails in Background Thread
            new Thread(() -> {
                try {
                    MailUtil.sendApplicationConfirmation(
                            user.getName(),
                            user.getEmail(),
                            job.getTitle(),
                            job.getCompany()
                    );

                    MailUtil.sendNewApplicationNotificationToEmployer(
                            employer.getName(),
                            employer.getEmail(),
                            user.getName(),
                            job.getTitle()
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // ✅ Redirect immediately (VERY IMPORTANT)
            res.sendRedirect("userDashboard?applied=1");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("userDashboard?error=apply_failed");
        }
    }
}
