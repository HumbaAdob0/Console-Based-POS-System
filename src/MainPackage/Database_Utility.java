package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database_Utility {
	
	public static Connection connect() {
		try {
			return DriverManager.getConnection(UserLogin.getDatabase_URL(), UserLogin.getDatabase_username(), UserLogin.getDatabase_password());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Object[] update(String sqlUpdate, Object...params) {
		try{
			
			Connection connect = connect();
			PreparedStatement statement = connect.prepareStatement(sqlUpdate);
			for(int i=0; i < params.length; i++) {
				statement.setObject(i+1, params[i]);
			}
			
			int result = statement.executeUpdate();
			
			return new Object[] {connect, result};
			
		}catch(Exception e) {
			return null;
		}
	}
	
	public static Object[] query(String sqlQuery, Object...params) {
		try{
			
			Connection connect = connect();
			PreparedStatement statement = connect.prepareStatement(sqlQuery);
			
			for(int i = 0; i < params.length; i++) {
				statement.setObject(i+1, params[i]);
			}
			
			ResultSet result = statement.executeQuery();
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
	
	// you have to change the connection processes on the base_user class to utilize this Database_utility class
	

}
