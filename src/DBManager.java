/**
 * @author: marian
 * A helper class designed to contain all the methods that communicate with the database
 * (basic CRUD ops. are here, and they are used throughout the files
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {
	
	private static String prepInsertStmt(int howMany) {
		String nrOfQuestions = "";
		String prepStmt = null;
		for(int i=0;i<howMany;i++) {
			nrOfQuestions += "?,";
		}
		prepStmt = nrOfQuestions.substring(0, nrOfQuestions.length()-1);
		return prepStmt;
	}
	
	private static String prepUpdateStmt(String[] whatToSet) {
		String prepStmt = null;
		int sizeWhatToSet = whatToSet.length;
		for(int i=0;i<sizeWhatToSet;i++) {
			prepStmt += whatToSet[i] + " = ?,";
		}
		prepStmt = prepStmt.substring(0, prepStmt.length() - 1);
		return prepStmt;
	}
	
	/**
	 * Generic method of getting all the data from the table specified at "table"
	 * @param table
	 * @return
	 */
	public static ResultSet getAll(String table) {
		try {
			Connection conn = DBConn.getInstance().getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return stmt.executeQuery("SELECT * FROM " + table); 
		} catch(SQLException e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * General insert method that uses a reflexive method - went thru all the hassle so I could copy paste in future projects
	 * @param location
	 * @param what
	 * @param params
	 * @return boolean
	 * @throws SQLException
	 */
	public static boolean insert(String location, String what, ArrayList<Object> params) throws SQLException {
		String prepStmt = prepInsertStmt(params.size());
		String sql = "INSERT into " + location + " (" + what + ") VALUES(" + prepStmt + ")";
		System.out.println(sql);
		
		ResultSet keys = null;
		try {
			Connection conn = DBConn.getInstance().getConnection();
			PreparedStatement  stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			for(int i=0;i<params.size();i++) {
				System.out.println(params.get(i).toString() + "  &  ");
				String className = params.get(i).getClass().getSimpleName();
				System.out.println(className + "\n");
				switch(className) {
				case "String":
					stmt.setString(i+1, (String)params.get(i));
					break;
				case "Integer":
					stmt.setInt(i+1, (Integer)params.get(i));
					break;
				}
			}
			
			int affected  = stmt.executeUpdate();
			
			if(affected == 1) {
				keys = stmt.getGeneratedKeys();
				keys.next();
				int newKey = keys.getInt(1); // doar prima coloana
				System.out.println("Id-ul la coloana inserata: " + Integer.toString(newKey) + "\n");
			} else {
				System.err.println("No rows affected");
				return false;
			}
			
			
		} catch(SQLException e) {
			System.err.println(e);
			return false;
		} finally {
			if(keys != null) keys.close();
		}
		
		return true;
	}
	
	
	/**
	 * General update method that uses reflexiveness to determine parameter type - hassle free on general structures
	 * @param location
	 * @param whatToSet
	 * @param where
	 * @param params
	 * @return boolean
	 */
	public static boolean update(String location, String[] whatToSet, String where, ArrayList<Object> params) {
		String prepStmt = prepUpdateStmt(whatToSet);
		String sql = "UPDATE " + location + " SET " + prepStmt + " WHERE " + where + "= ?";
		System.out.println(sql);
		
		try {
			Connection conn = DBConn.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			for(int i=0;i<params.size();i++) {
				String className = params.get(i).getClass().getName();
				switch(className) {
				case "String":
					stmt.setString(i, (String)params.get(i));
					break;
				case "Integer":
					stmt.setInt(i, (Integer)params.get(i));
					break;
				}
			}
			
			int affected  = stmt.executeUpdate();
			
			if(affected == 1) {
				return true;
			} else {
				return false;
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		}
	}
	
	/**
	 * Id based deletion
	 * @param location
	 * @param where
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean delete(String location, String where, Integer id) throws SQLException {
		String sql = "DELETE  FROM " + location + " WHERE " + where + " = ?";
		System.out.println(sql);
		
		try {
			Connection conn = DBConn.getInstance().getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			int affected = stmt.executeUpdate();
			
			if(affected == 1) {
				return true;
			} else {
				return false;
			}			
		} catch (SQLException e ) {
			System.err.println(e);
			return false;
		}
	}
	
}
