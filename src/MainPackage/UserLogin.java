package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;



/*
 * This UserLogin Class is the one resposible for determining whether th account login exist
 * in the database. This Class also verify what role you are trying to login.
 */
public class UserLogin {
	private static final String database_URL = "jdbc:mysql://127.0.0.1:3306/pos_database";
	private static final String database_username = "root";
	private static final String database_password = "computerengineering";
	
	Scanner scan = new Scanner(System.in);
	
	/*
	 * Since eclipse dont support clear console function, I premitively made a bunch of 
	 * newlines, as if the console is getting clear after a certain frame.
	 */
	void newLine() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	
	
	/*
	 * This method is where you input your username & password.
	 * I've enclosed the method with a do while loop so that everytime we input a wrong credential it would exit out the program.
	 */
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
			
			//this connection here creates a connection to our databse using the private global variable initialized above.
			try(Connection connect = DriverManager.getConnection(database_URL,database_username, database_password)){
				String SQLquery = "SELECT acc_type, name FROM accounts WHERE username = ?  AND password = ?"; 
				
				
				PreparedStatement statement = connect.prepareStatement(SQLquery);
					statement.setString(1, username); // number 1 replaces the first '?' in the query with the value of username
					statement.setString(2, password); // number 2 replaces the second '?' in the query with the value of password
					
					
				ResultSet result = statement.executeQuery(); //this execute our query and returns rows of data stored in the ResultSet
	
					if(result.next()){ //the rows of data is iterated through .next(), if this iterates it returns a true value
	
						String acc_type = result.getString("acc_type"); // this retrives data from the column named 'acc_type' from our ResultSet
						switch(acc_type.toLowerCase()) { // this makes out retrived data lowercase, so that it wont get an error if we compare it to a predermined value 
							case "admin":
								newLine();
								Admin admin = new Admin(); // execute class admin
								admin.greetings(username);
								admin.admin_Dashboard(result.getString("name"));
							break;
							
							case "moderator":
								newLine();
								Moderator moderator = new Moderator(); // execute class moderator
								moderator.greetings(username);
								moderator.moderator_Dashboard(result.getString("name"));
							break;
							
							case "cashier":
								newLine();
								Base_user base_user = new Base_user(); // execute class base_user
								base_user.greetings(username);
								base_user.base_user_Dashboard(result.getString("name"));
							break;
						}
						
					}
					else {
						System.out.println("Invalid Username and Password"); // error handling, if .next() dont iterate, the query didn't retieve anything and therefore returns false
						System.out.print("Press Enter to continue...");
						scan.nextLine();
					}
				
					statement.close();
					connect.close();
				
			}catch(Exception e) {
				System.out.println("Invalid Username and Password"); // error handling
				System.out.print("Press Enter to continue...");
				scan.nextLine();
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
