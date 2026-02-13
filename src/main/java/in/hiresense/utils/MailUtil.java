package in.hiresense.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    // ================= COMMON METHOD =================
    public static void sendTextEmail(String toEmail, String subject, String body)
            throws MessagingException {

        javax.mail.Session session = MailConfig.getSession();

        Message msg = new MimeMessage(session);
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(body);

        Transport.send(msg);
    }

    // ================= REGISTER OTP =================
    public static void sendRegisterOTP(String name, String toEmail, String otp)
            throws MessagingException {

        String subject = "🔐 HireSense Registration OTP";

        String body =
                "Dear " + name + ",\n\n"
              + "Thank you for registering on HireSense.\n\n"
              + "Your OTP for completing registration is:\n\n"
              + "👉 " + otp + "\n\n"
              + "This OTP is valid for a short time.\n\n"
              + "If you did not request this, please ignore this email.\n\n"
              + "Regards,\nHireSense Team";

        sendTextEmail(toEmail, subject, body);
    }

    // ================= RESET PASSWORD OTP =================
    public static void sendPasswordResetOTP(String name, String toEmail, String otp)
            throws MessagingException {

        String subject = "🔑 HireSense Password Reset OTP";

        String body =
                "Dear " + name + ",\n\n"
              + "You requested to reset your password.\n\n"
              + "Your OTP is:\n\n"
              + "👉 " + otp + "\n\n"
              + "If you did not request a password reset, please ignore this email.\n\n"
              + "Regards,\nHireSense Team";

        sendTextEmail(toEmail, subject, body);
    }

    // ================= APPLICATION SUBMITTED =================
    public static void sendApplicationConfirmation(String name,
                                                   String toEmail,
                                                   String jobTitle,
                                                   String company)
            throws MessagingException {

        String subject = "✅ Application Submitted - " + jobTitle;

        String body =
                "Dear " + name + ",\n\n"
              + "You have successfully applied for the position of \""
              + jobTitle + "\" at " + company + ".\n\n"
              + "We wish you the best of luck!\n\n"
              + "Regards,\nHireSense Team";

        sendTextEmail(toEmail, subject, body);
    }

    // ================= APPLICATION STATUS UPDATE =================
    public static void sendApplicationStatusUpdate(String name,
                                                   String toEmail,
                                                   String jobTitle,
                                                   String company,
                                                   String status)
            throws MessagingException {

        String subject = "📢 Application Status Update - " + jobTitle;

        String body;

        if ("shortlisted".equalsIgnoreCase(status)) {

            body = "Dear " + name + ",\n\n"
                 + "🎉 Congratulations! You have been shortlisted for:\n"
                 + jobTitle + " at " + company + ".\n\n"
                 + "The employer may contact you soon.\n\n"
                 + "Best Wishes,\nHireSense Team";

        } else if ("rejected".equalsIgnoreCase(status)) {

            body = "Dear " + name + ",\n\n"
                 + "We regret to inform you that you were not selected for:\n"
                 + jobTitle + " at " + company + ".\n\n"
                 + "We encourage you to apply for other opportunities.\n\n"
                 + "Regards,\nHireSense Team";

        } else {

            body = "Dear " + name + ",\n\n"
                 + "Your application status has been updated to: "
                 + status + " for the job "
                 + jobTitle + " at " + company + ".\n\n"
                 + "Regards,\nHireSense Team";
        }

        sendTextEmail(toEmail, subject, body);
    }

    // ================= ACCOUNT ACTION =================
    public static void sendAccountActionNotice(String toEmail,
                                               String userName,
                                               String action)
            throws MessagingException {

        String subject = "⚠️ Account Update - HireSense";
        String body;

        switch (action.toLowerCase()) {

            case "blocked":
                body = "Hello " + userName + ",\n\n"
                     + "Your account has been BLOCKED.\n"
                     + "Please contact support for details.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            case "unblocked":
                body = "Hello " + userName + ",\n\n"
                     + "Your account has been UNBLOCKED.\n"
                     + "You may now log in again.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            case "removed":
                body = "Hello " + userName + ",\n\n"
                     + "Your account has been permanently removed.\n\n"
                     + "Regards,\nAdmin Team";
                break;

            default:
                body = "Hello " + userName + ",\n\n"
                     + "There has been an update on your account: "
                     + action + "\n\n"
                     + "Regards,\nAdmin Team";
        }

        sendTextEmail(toEmail, subject, body);
    }

    // ================= EMPLOYER NEW APPLICATION =================
    public static void sendNewApplicationNotificationToEmployer(
            String employerName,
            String toEmail,
            String applicantName,
            String jobTitle)
            throws MessagingException {

        String subject = "🆕 New Application Received";

        String body =
                "Dear " + employerName + ",\n\n"
              + "A new candidate '" + applicantName + "' has applied for:\n"
              + "\"" + jobTitle + "\".\n\n"
              + "Please check your dashboard for details.\n\n"
              + "Regards,\nHireSense Team";

        sendTextEmail(toEmail, subject, body);
    }
}
