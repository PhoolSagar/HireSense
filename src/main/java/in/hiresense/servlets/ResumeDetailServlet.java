// === Step 2: ResumeDetailServlet.java ===
package in.hiresense.servlets;

import java.io.IOException;
import java.util.List;

import in.hiresense.dao.ResumeAnalysisLogDAO;
import in.hiresense.models.ResumeAnalysisLogPojo;
import in.hiresense.utils.AffindaAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ResumeDetailServlet")
public class ResumeDetailServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            List<ResumeAnalysisLogPojo> logs = ResumeAnalysisLogDAO.getLogsByUser(userId);
            if (logs.isEmpty()) {
                req.setAttribute("error", "No analysis found for this user.");
            } else {
                ResumeAnalysisLogPojo latest = logs.get(0);
                String resultJson = latest.getResultJson();
                req.setAttribute("summary", AffindaAPI.extractSummary(resultJson));
                req.setAttribute("skillsList", AffindaAPI.extractSkills(resultJson));
                req.setAttribute("personalDetails", AffindaAPI.extractPersonalDetails(resultJson));
                req.setAttribute("education", AffindaAPI.extractEducation(resultJson));
                req.setAttribute("workExperience", AffindaAPI.extracteWorkExperience(resultJson));

            }
            req.getRequestDispatcher("resumeDetails.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException("Error fetching resume details", e);
        }
    }
}