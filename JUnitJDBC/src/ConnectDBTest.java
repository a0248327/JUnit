import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.Test;


public class ConnectDBTest {
	
	private static Connection conn = null;
	
	public ConnectDBTest () {
		conn = ConnectDB.getConnection();
	}
	
	@Test
	public void testGetDBConnection() {
		assertNotNull(conn);
	}

	@AfterClass
	public static void closeConnectDB() {
		ConnectDB.closeConnection(conn);
	}

}
