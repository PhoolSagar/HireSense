package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.JobDAO;
import in.hiresense.models.JobPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PostJobServlet")
public class PostJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null
                || !"employer".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {
            int employerId = (int) session.getAttribute("userId");

            // ===================== GET FORM DATA =====================
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String skills = req.getParameter("skills");
            String company = req.getParameter("company");

            String location = req.getParameter("location");
            String customLocation = req.getParameter("customLocation");

            // ✅ IMPORTANT FIX: if "Other" selected, use custom value
            if ("Other".equals(location)) {
                if (customLocation != null && !customLocation.trim().isEmpty()) {
                    location = customLocation.trim();
                } else {
                    location = "Other";
                }
            }

            String experience = req.getParameter("experience");
            String packageLpa = req.getParameter("packageLpa");
            int vacancies = Integer.parseInt(req.getParameter("vacancies"));

            // ===================== CREATE JOB OBJECT =====================
            JobPojo job = new JobPojo();
            job.setTitle(title);
            job.setDescription(description);
            job.setSkills(skills);
            job.setCompany(company);
            job.setLocation(location);   // ✅ This now saves correct value
            job.setExperience(experience);
            job.setPackageLpa(packageLpa);
            job.setVacancies(vacancies);
            job.setEmployerId(employerId);

            boolean ans = JobDAO.postJob(job);

            if (ans) {
                res.sendRedirect("employerDashboard?success=1");
            } else {
                res.sendRedirect("employerDashboard?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("employerDashboard?error=1");
        }
    }
}
