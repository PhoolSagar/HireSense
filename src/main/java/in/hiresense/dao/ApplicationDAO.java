package in.hiresense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.hiresense.dbutils.DBConnection;
import in.hiresense.models.ApplicationPojo;

public class ApplicationDAO {

    // ================= APPLY =================
    public static boolean apply(ApplicationPojo app) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "INSERT INTO applications(user_id, job_id, resume_path, score) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, app.getUserId());
            ps.setInt(2, app.getJobId());
            ps.setString(3, app.getResumePath());
            ps.setDouble(4, app.getScore());

            int ans = ps.executeUpdate();
            return ans > 0;

        } finally {
            if (ps != null) ps.close();
        }
    }

    // ================= GET BY USER =================
    public static List<ApplicationPojo> getApplicationsByUser(int userId) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            List<ApplicationPojo> list = new ArrayList<>();

            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM applications WHERE user_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ApplicationPojo(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("job_id"),
                        rs.getString("resume_path"),
                        rs.getFloat("score"),
                        rs.getString("status"),
                        rs.getString("applied_at")
                ));
            }

            return list;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    // ================= GET BY JOB AND STATUS =================
    public static List<ApplicationPojo> getApplicationsByJobAndStatus(int jobId, String status) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            List<ApplicationPojo> list = new ArrayList<>();

            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM applications WHERE job_id = ? AND status = ? ORDER BY score DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            ps.setString(2, status);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ApplicationPojo(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("job_id"),
                        rs.getString("resume_path"),
                        rs.getFloat("score"),
                        rs.getString("status"),
                        rs.getString("applied_at")
                ));
            }

            return list;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    // ================= UPDATE STATUS =================
    public static boolean updateApplicationStatus(int appId, String status) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE applications SET status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, appId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } finally {
            if (ps != null) ps.close();
        }
    }
    
    

    // ================= GET USER ID BY APPLICATION ID (IMPORTANT) =================
    public static int getUserIdByApplicationId(int appId) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT user_id FROM applications WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, appId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

            return 0;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }
}
