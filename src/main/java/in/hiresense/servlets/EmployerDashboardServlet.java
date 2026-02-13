package in.hiresense.servlets;

import java.io.IOException;
import java.util.List;

import in.hiresense.dao.JobDAO;
import in.hiresense.models.JobPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/employerDashboard")
public class EmployerDashboardServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int employerId = (Integer) session.getAttribute("userId");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String status = request.getParameter("status");

        try {
            List<JobPojo> jobs = JobDAO.getJobsByEmployer(employerId, search, status, sort);
            request.setAttribute("jobList", jobs);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("sort", sort);

            request.getRequestDispatcher("employerDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}