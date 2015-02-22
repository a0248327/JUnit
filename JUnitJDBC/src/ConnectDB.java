import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/test";
			conn = DriverManager.getConnection(url, "root", "1201");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static ResultSet getResultSet(Connection conn, String sql) {
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
		} catch (Exception e) {
			System.out.println("query failed !");
		} finally {
			try {
				statement.close();
				closeConnection(conn);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return rs;
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}