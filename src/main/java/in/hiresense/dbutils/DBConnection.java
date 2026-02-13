package in.hiresense.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static Connection conn;

	public static void openConnection(String url, String username, String password) {
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection opened successfully");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static Connection getConnection() throws SQLException {
		if (conn == null) {
			throw new SQLException("Connection is null");
		}
		return conn;
	}

	public static void closeConnection() {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

	}

}
