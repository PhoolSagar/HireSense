package in.hiresense.servlets;

import java.io.IOException;
import in.hiresense.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        String enteredOtp = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");

        String actualOtp = (String) session.getAttribute("resetOTP");
        String email = (String) session.getAttribute("resetEmail");

        if (actualOtp == null || email == null) {
            res.sendRedirect("forgotPassword.jsp");
            return;
        }

        if (!actualOtp.equals(enteredOtp)) {
            res.sendRedirect("forgotPassword.jsp?showOtp=true&error=invalid");
            return;
        }

        try {

            int updated = UserDAO.updatePassword(email, newPassword);

            if (updated > 0) {

                session.removeAttribute("resetOTP");
                session.removeAttribute("resetEmail");

                res.sendRedirect("login.jsp?msg=resetSuccess");

            } else {
                res.sendRedirect("forgotPassword.jsp?error=server");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("forgotPassword.jsp?error=server");
        }
    }
}
