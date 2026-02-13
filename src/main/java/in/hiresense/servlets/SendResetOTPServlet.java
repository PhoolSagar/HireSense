package in.hiresense.servlets;

import java.io.IOException;
import java.util.Random;

import in.hiresense.dao.UserDAO;
import in.hiresense.models.UserPojo;
import in.hiresense.utils.MailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/SendResetOTPServlet")
public class SendResetOTPServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        try {

            UserPojo user = UserDAO.getUserByEmail(email);

            if (user == null) {
                res.sendRedirect("forgotPassword.jsp?error=notFound");
                return;
            }

            // Generate 6-digit OTP
            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            HttpSession session = req.getSession();
            session.setAttribute("resetOTP", otp);
            session.setAttribute("resetEmail", email);

            // Send OTP
            MailUtil.sendTextEmail(
                email,
                "HireSense Password Reset OTP",
                "Your OTP is: " + otp + "\n\nDo not share this with anyone."
            );

            res.sendRedirect("forgotPassword.jsp?showOtp=true");

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("forgotPassword.jsp?error=server");
        }
    }
}
