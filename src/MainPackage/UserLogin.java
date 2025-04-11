package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserLogin {
	private static final String database_URL = "jdbc:mysql://127.0.0.1:3306/pos_database";
	private static final String database_username = "root";
	private static final String database_password = "computerengineering";
	
	Scanner scan = new Scanner(System.in);
	
	
	void newLine() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	void Login(){
		boolean ctr = true;
		do{
			newLine();
			System.out.println("====================\n"
								+ " L O G I N  M E N U \n"
								+"====================");
			System.out.print("Username: ");
			String username = scan.nextLine();
			System.out.print("Password: ");
			String password = scan.nextLine();
			
			try(Connection connect = DriverManager.getConnection(database_URL,database_username, database_password)){
				String SQLquery = "SELECT acc_type FROM accounts WHERE username = ?  AND password = ?";
				
				
				PreparedStatement statement = connect.prepareStatement(SQLquery);
					statement.setString(1, username);
					statement.setString(2, password);
					
					
				ResultSet result = statement.executeQuery();
	
					if(result.next()){
	
						String acc_type = result.getString("acc_type");
						switch(acc_type.toLowerCase()) {
							case "admin":
						
								newLine();
								//System.out.println("admin account");
								Admin admin = new Admin(username);
							break;
							
							case "moderator":
						
								newLine();
								//System.out.println("moderator account");
								Moderator moderator = new Moderator(username);
							break;
							
							case "cashier":
						
								newLine();
								//System.out.println("cashier account");
								Base_user base_user = new Base_user(username);
							break;
							
							default:
							System.out.println("\nERROR: Role not found!");
						}
						
					}
					else {
						System.out.println("Invalid Username and Password");
						System.out.print("Press Enter to continue...");
						scan.nextLine();
					}
				
					statement.close();
					connect.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}while(ctr);
	}

	static String getDatabase_URL() {
		return database_URL;
	}
	
	static String getDatabase_username() {
		return database_username;
	}
	
	static String getDatabase_password() {
		return database_password;
	}
	
}
