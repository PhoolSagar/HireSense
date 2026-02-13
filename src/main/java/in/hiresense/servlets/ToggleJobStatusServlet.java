package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.JobDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ToggleJobStatusServlet")
public class ToggleJobStatusServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int jobId = Integer.parseInt(request.getParameter("jobId"));
        try {
            JobDAO.toggleJobStatus(jobId);
            response.sendRedirect("employerDashboard");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("employerDashboard?error=1");
        }
    }
}
