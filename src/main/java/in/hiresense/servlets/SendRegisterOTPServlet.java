package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.utils.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SendRegisterOTPServlet")
public class SendRegisterOTPServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();

		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String role = req.getParameter("role");

		String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
		session.setAttribute("regOTP", otp);
		session.setAttribute("regName", name);
		session.setAttribute("regEmail", email);
		session.setAttribute("regPassword", password);
		session.setAttribute("regRole", role);

		String appName = (String)super.getServletContext().getAttribute("appName");

		try {
			MailUtil.sendTextEmail(email, "Your OTP for"+appName+" Registration",
				"Hi " + name + ",\n\nYour OTP to complete registration is: " + otp);
			res.sendRedirect("register.jsp?showOtp=true");
		} catch (Exception e) {
			throw new ServletException("Failed to send OTP", e);
		}
	}
}