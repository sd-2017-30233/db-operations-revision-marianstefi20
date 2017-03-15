import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn
{
	private static DBConn instance = null;

	private final String USERNAME = "root";
	private final String PASSWORD = "marian20";
	private final String M_CONN_STRING ="jdbc:mysql://localhost/portal?autoReconnect=true&useSSL=false";

	private DBType dbType = DBType.MYSQL;

	private Connection conn = null;

	private DBConn() {
	}
	
	// singleton power 
	public static DBConn getInstance() {
		if (instance == null) {
			instance = new DBConn();
		}
		return instance;
	}

	public void setDBType(DBType dbType) {
		this.dbType = dbType;
	}

	private boolean openConnection()
	{
		try {
			switch (dbType) {
			case MYSQL:
				conn = DriverManager.getConnection(M_CONN_STRING, USERNAME, PASSWORD);
				return true;
			default: 
				return false;
			}
		}
		catch (SQLException e) {
			System.err.println(e);
			return false;
		}

	}
	
	// if we have a connection we don't bother to create a new one...
	public Connection getConnection()
	{
		if (conn == null) {
			if (openConnection()) {
				System.out.println("Connection opened");
				return conn;
			} else {
				return null;
			}
		}
		return conn;
	}

	public void close() {
		System.out.println("Closing connection");
		try {
			conn.close();
			conn = null;
		} catch (Exception e) {
		}
	}

}
