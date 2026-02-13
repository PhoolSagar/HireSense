// UploadResumeServlet.java - Handles resume upload & Affinda integration
package in.hiresense.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;

import in.hiresense.dao.ResumeAnalysisLogDAO;
import in.hiresense.models.ResumeAnalysisLogPojo;
import in.hiresense.utils.AffindaAPI;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/UploadResumeServlet")
@MultipartConfig
public class UploadResumeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        Part filePart = request.getPart("resume");

        // ✅ Make filename unique
        String originalFileName = Paths.get(filePart.getSubmittedFileName())
                .getFileName().toString();

        String extension = originalFileName.substring(
                originalFileName.lastIndexOf("."));

        String fileName = "resume_" + userId + "_" +
                System.currentTimeMillis() + extension;

        // ✅ Save inside /resumes folder
        String uploadDir = getServletContext().getRealPath("/resumes");
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File resumeFile = new File(dir, fileName);

        // ✅ Delete previous resume (if any)
        try {
            List<ResumeAnalysisLogPojo> logs =
                    ResumeAnalysisLogDAO.getLogsByUser(userId);

            if (!logs.isEmpty()) {
                String prevJson = logs.get(0).getResultJson();
                JSONObject obj = new JSONObject(prevJson);

                String prevPath = obj.getJSONObject("data")
                        .optString("resumePath", null);

                if (prevPath != null) {
                    File oldFile = new File(prevPath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Save new resume file
        try (InputStream input = filePart.getInputStream();
             FileOutputStream out = new FileOutputStream(resumeFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        try {
            // ✅ Analyze using Affinda
            String resultJson = AffindaAPI.analyzeResume(resumeFile);
            JSONObject result = new JSONObject(resultJson);

            result.getJSONObject("data")
                    .put("resumePath", resumeFile.getAbsolutePath());

          //  ResumeAnalysisLogDAO.saveLog(userId, result.toString());
            
         // After getting result JSON
            String resultJson1 = result.toString();

            // Save resume log
            ResumeAnalysisLogDAO.saveLog(userId, resultJson1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("userDashboard");
    }
}
