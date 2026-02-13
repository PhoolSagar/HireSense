package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.UserDAO;
import in.hiresense.models.UserPojo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyRegisterOTPServlet")
public class VerifyRegisterOTPServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // 🔒 Session safety
        if (session == null || session.getAttribute("regOTP") == null) {
            res.sendRedirect("register.jsp");
            return;
        }

        String inputOtp = req.getParameter("otp");
        String actualOtp = (String) session.getAttribute("regOTP");

        if (inputOtp == null || !inputOtp.equals(actualOtp)) {
            res.sendRedirect("register.jsp?showOtp=true&error=invalid");
            return;
        }

        // ✅ Fetch stored temporary registration data
        String name = (String) session.getAttribute("regName");
        String email = (String) session.getAttribute("regEmail");
        String password = (String) session.getAttribute("regPassword");
        String role = (String) session.getAttribute("regRole");

        try {

            // 🔴 Step 1: Check if email already exists
            UserPojo existingUser = UserDAO.getUserByEmail(email);

            if (existingUser != null) {
                res.sendRedirect("register.jsp?duplicate=1");
                return;
            }

            // 🔵 Step 2: Create new user
            UserPojo user = new UserPojo();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);  // Later we can hash this
            user.setRole(role);

            int inserted = UserDAO.registerUser(user);

            if (inserted > 0) {

                // 🧹 Clear session registration data
                session.removeAttribute("regOTP");
                session.removeAttribute("regName");
                session.removeAttribute("regEmail");
                session.removeAttribute("regPassword");
                session.removeAttribute("regRole");

                res.sendRedirect("login.jsp?registered=1");

            } else {
                res.sendRedirect("register.jsp?error=server");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("register.jsp?error=server");
        }
    }
}
