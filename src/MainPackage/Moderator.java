package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Moderator extends Base_user{
	
	Scanner scan = new Scanner(System.in);
	UserLogin user_Login = new UserLogin();

	Moderator(String username) {
		super(username); //This class inherit to the parent class Base_user, the super keyword refers to the parent class constructor
		 				 //the param username came from the UserLogin Class
	}
	
	void moderator_Dashboard(String name) {
		char choice;
		boolean ctr = true;
		
		do {
			System.out.println("\nD A S H B O A R D\n\n"  
					+ "[a] View Sale\n"
					+ "[b] Add Stocks\n"
					+ "[c] Pull-out Stocks\n"
					+ "[d] Logout");
			System.out.print("Enter your choice: ");
			choice = Character.toLowerCase(scan.next().charAt(0)); // input is automatically made into lowercase to avoid error in comparing the value at switch
			scan.nextLine();
			
			switch(choice){
			case 'a': 
				break;
			case 'b':
				break;
			case 'c':
				break;
			case 'd':
				break;
			default:
				invalid_input_error_Message();
			}
			
			user_Login.newLine();
			
		}while(ctr);
	
	}


	
}
