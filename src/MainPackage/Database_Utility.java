package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/*
 * This Class make it easy to connect and ask for a query and update database.
 */
public class Database_Utility {
	
	/*
	 * This method can connect to the database—with its reusability, we are able to call this method with a short syntax.
	 * saving code complexity and space.
	 */
	public static Connection connect() {
		try {
			return DriverManager.getConnection(UserLogin.getDatabase_URL(), UserLogin.getDatabase_username(), UserLogin.getDatabase_password());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/*
	 * @param sqlUpdate - this string variable is where the sql syntax is stored
	 * @Object...params - lets pass zero or more arguments to a method, treats them like an array
	 * 
	 * This method updates the database, by passing our update to the 'sqlUpdate' parameter.
	 *  
	 */
	public static Object[] update(String sqlUpdate, Object...params) {
		try{
			
			Connection connect = connect();
			PreparedStatement statement = connect.prepareStatement(sqlUpdate);
			
			for(int i=0; i < params.length; i++) { 
				statement.setObject(i+1, params[i]); // the '?' in our sqlUpdate syntax is being replace by the value of params[i]
			}
			
			int result = statement.executeUpdate(); // this return the number of rows affected by this update
			
			return new Object[] {connect, result};
			
		}catch(Exception e) {
			return null;
		}
	}
	
	
	/*
	 * @param sqlQuery - this string variable is where the sql syntax is stored
	 * @Object...params - lets pass zero or more arguments to a method, treats them like an array
	 * 
	 * This method ask for queries to the database, by passing our query to the 'sqlQuery' parameter.
	 */
	public static Object[] query(String sqlQuery, Object...params) {
		try{
			Connection connect = connect();
			PreparedStatement statement = connect.prepareStatement(sqlQuery);
			
			for(int i = 0; i < params.length; i++) {
				statement.setObject(i+1, params[i]); // the '?' in our sqlQuery syntax is being replace by the value of params[i]
			}
			
			ResultSet result = statement.executeQuery(); // this execute our query and returns rows of data stored in the ResultSet
			return new Object[]{connect,result};
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static void close(Connection connect) {
		if(connect != null){
			try {
				connect.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}
