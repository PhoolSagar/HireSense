package in.hiresense.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyResetOTPServlet")
public class VerifyResetOTPServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        String inputOtp = req.getParameter("otp");
        String actualOtp = (String) session.getAttribute("resetOTP");

        if (inputOtp != null && inputOtp.equals(actualOtp)) {
            res.sendRedirect("resetPassword.jsp");
        } else {
            res.sendRedirect("forgotPassword.jsp?showOtp=true&error=invalid");
        }
    }
}
