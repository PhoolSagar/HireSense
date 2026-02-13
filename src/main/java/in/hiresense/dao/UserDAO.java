package in.hiresense.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.hiresense.dbutils.DBConnection;
import in.hiresense.models.UserPojo;

public class UserDAO {


	public static int registerUser(UserPojo user) throws Exception {

	    Connection conn = null;
	    PreparedStatement ps = null;

	    try {
	        conn = DBConnection.getConnection();

	        // 🔴 Check duplicate email first
	        if (getUserByEmail(user.getEmail()) != null) {
	            return 0;
	        }

	        String sql = "INSERT INTO users(name, email, password, role, status) VALUES (?, ?, ?, ?, 'active')";
	        ps = conn.prepareStatement(sql);

	        ps.setString(1, user.getName());
	        ps.setString(2, user.getEmail());
	        ps.setString(3, user.getPassword());
	        ps.setString(4, user.getRole());

	        return ps.executeUpdate();

	    } finally {
	        if (ps != null) ps.close();
	    }
	}

	public static int updatePassword(String email, String newPassword) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBConnection.getConnection();

			String sql = "UPDATE users SET password=? WHERE email=?";
			ps = conn.prepareStatement(sql);

			ps.setString(1, newPassword);
			ps.setString(2, email);

			return ps.executeUpdate();

		} finally {
			if (ps != null)
				ps.close();
		}
	}


    public static UserPojo getUserByEmail(String email) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE email=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new UserPojo(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getDate("created_at")
                );
            }
            return null;
        } finally {
            if (rs != null) {
				rs.close();
			}
            if (ps != null) {
				ps.close();
			}
        }
    }

    public static UserPojo getUserById(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new UserPojo(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getDate("created_at")
                );
            }
            return null;
        } finally {
            if (rs != null) {
				rs.close();
			}
            if (ps != null) {
				ps.close();
			}
        }
    }

    public static List<UserPojo> getAllUsers() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserPojo> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users ORDER BY id DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new UserPojo(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getDate("created_at")
                ));
            }
            return list;
        } finally {
            if (rs != null) {
				rs.close();
			}
            if (ps != null) {
				ps.close();
			}
        }
    }

    public static int updateStatus(int userId, String status) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE users SET status=? WHERE id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
				ps.close();
			}
        }
    }

    public static List<UserPojo> getFilteredUsers(String search, String role, String status) {
        List<UserPojo> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection(); // Replace with your connection method

            // Base query
            StringBuilder query = new StringBuilder("SELECT * FROM users WHERE role != 'admin'");

            // Dynamic filters
            if (search != null && !search.trim().isEmpty()) {
                query.append(" AND (name LIKE ? OR email LIKE ?)");
            }
            if (role != null && !role.trim().isEmpty() && !role.equalsIgnoreCase("all")) {
                query.append(" AND role = ?");
            }
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
                query.append(" AND status = ?");
            }

            query.append(" ORDER BY created_at DESC");

            ps = conn.prepareStatement(query.toString());

            // Set dynamic parameters
            int index = 1;
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
                ps.setString(index++, "%" + search.trim() + "%");
            }
            if (role != null && !role.trim().isEmpty() && !role.equalsIgnoreCase("all")) {
                ps.setString(index++, role);
            }
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
                ps.setString(index++, status);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                UserPojo user = new UserPojo();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return users;
    }



}