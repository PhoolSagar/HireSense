package in.hiresense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.hiresense.dbutils.DBConnection;
import in.hiresense.models.ResumeAnalysisLogPojo;

public class ResumeAnalysisLogDAO {

	public static void saveLog(int userId, String resultJson) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBConnection.getConnection();
			String sql = "INSERT INTO resume_analysis_logs (user_id, result_json) VALUES (?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, resultJson);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	

	public static List<ResumeAnalysisLogPojo> getLogsByUser(int userId) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			List<ResumeAnalysisLogPojo> list = new ArrayList<>();
			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM resume_analysis_logs WHERE user_id=? ORDER BY created_at DESC";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new ResumeAnalysisLogPojo(rs.getInt("id"), rs.getInt("user_id"), rs.getString("result_json"),
						rs.getString("created_at")));
			}
			return list;
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}
}