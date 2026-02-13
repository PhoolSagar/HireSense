package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.JobDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RemoveJobServlet")
public class RemoveJobServlet extends HttpServlet {
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int jobId = Integer.parseInt(req.getParameter("jobId"));
        try {
            JobDAO.deleteJob(jobId);
            res.sendRedirect("adminPanel");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp");
        }
    }
}