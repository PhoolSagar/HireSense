package in.hiresense.servlets;

import java.io.IOException;
import java.util.List;

import in.hiresense.dao.JobDAO;
import in.hiresense.dao.UserDAO;
import in.hiresense.models.JobPojo;
import in.hiresense.models.UserPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/adminPanel")
public class AdminPanelServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null ||
                !"admin".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        String search = req.getParameter("search");
        String role = req.getParameter("role");
        String status = req.getParameter("status");

        try {
            List<UserPojo> users = UserDAO.getFilteredUsers(search, role, status);
            List<JobPojo> jobs = JobDAO.getAllJobsWithEmployerAndApplicantCount();

            req.setAttribute("users", users);
            req.setAttribute("jobs", jobs);

            req.getRequestDispatcher("adminPanel.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp");
        }
    }
}