package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Base_user{
	
	Scanner scan = new Scanner(System.in);
	UserLogin user_login = new UserLogin();
	
	/*
	 * This parameter username came from the query of UserLogin class.
	 * The username is then used to query for the acc_type and name of the account.
	 */
	Base_user(String username){
		try {
			String sqlQuery = "SELECT name, acc_type FROM accounts WHERE username = ?";	
			Object[] result_from_query = Database_Utility.query(sqlQuery, username);
			
			Connection connect = (Connection)result_from_query[0];
			ResultSet result = (ResultSet)result_from_query[1];
			
			if(result.next()){
				System.out.println("Hi " +result.getString("acc_type") +" " +result.getString("name") +"!");
				base_user_Dashboard(result.getString("name"));
			}
			else {
				System.out.println("\nERROR: Name and role not found!");
			}
			
			Database_Utility.close(connect);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	void base_user_Dashboard(String name) {
		int choice; //the variable where our input is stored
		boolean ctr = true; //this is the control variable that help us check whether the inputed value is correct
		
		do {
		System.out.println("\nD A S H B O A R D\n\n"  
				+ "[a] Make a sale\n"
				+ "[b] Logout\n");
		System.out.print("Enter your choice: ");
		choice = Character.toLowerCase(scan.next().charAt(0));
		scan.nextLine();
		
		if(choice =='a') {
			make_a_sale(name);
		}
		else if(choice =='b') {
			ctr = false;
		}
		else{
			System.out.println("\nERROR: Invalid input!\n"
					+ "Press Enter to try again...");
			scan.nextLine();
		}
		
		user_login.newLine();
		
		}while(ctr);
		
	}
	
	
	void make_a_sale(String name) {
		boolean ctr = true;
		String item_code;
		int quantity;
		char choice;
		
		do {
			try {
					user_login.newLine();
					System.out.println("M A K E  A  S A L E\n");
					System.out.print("Enter product code: ");
					item_code = scan.nextLine();
					
					
					System.out.print("Enter quantity: ");
					quantity = scan.nextInt();
					scan.nextLine();
					
					
					System.out.println("\n[a]Done\n"
							+ "[b]Back\n");
					
					System.out.print("Enter choice: ");
					choice = Character.toLowerCase(scan.next().charAt(0));
					scan.nextLine();
					
					switch(choice) {
						case 'a':
							if(store_to_current_sale(item_code, quantity, name)!=true) {
								break;
							}
							list_of_purchase();
							break;
							
						case 'b':
							ctr = false;
							break;
							
						default:
							System.out.println("\nERROR: Invalid input!\n"
									+ "Press Enter to try again...");
							scan.nextLine();
				}
			}catch(Exception e) {
				System.out.println("\nERROR: Invalid input!\n"
						+ "Press Enter to try again...");
				scan.nextLine();
			}
				
		}while(ctr);
	
	}
	
	void current_sale_total(){
		try {
		String sqlQuery = "select sum(total_price) as total_amount from current_sale";
		Object[] result_from_query = Database_Utility.query(sqlQuery);
		
		Connection connect = (Connection) result_from_query[0];
		ResultSet result = (ResultSet) result_from_query[1];
		
		if(result.next()) {
			System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t Total Amount: " +result.getString("total_amount"));
		}
		else {
			System.out.println("ERROR: No data found!");
		}
		Database_Utility.close(connect);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void current_sale_table(ResultSet result) {
		try {
		
		System.out.print("-------------------------------------------------------------------------------------------------------------------------------------\n");
		System.out.printf("%-15s %-25s %-26s %-10s %-13s %-17s %-20s","Sale Code", "Item Code", "Item Name", "Price", "Quantity", "Total Price", "Transaction Date");
		System.out.print("\n-------------------------------------------------------------------------------------------------------------------------------------");
		while(result.next()){
			System.out.printf("\n    %-12d %-19s %-31s %-14s %-13d %-14.2f %tY-%<tm-%<td %<tH:%<tM:%<tS%n" ,result.getInt("sale_code"), result.getString("item_code"),  result.getString("item_name"), result.getBigDecimal("price"), result.getInt("quantity"), result.getBigDecimal("total_price"), result.getTimestamp("transac_datetime"));
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void list_of_purchase() {
		
		char choice;
		boolean ctr = true;
		do {
			try{
				String sqlQuery = "select current_sale.item_code, sale_code, item_name, price, quantity, total_price, name, transac_datetime from current_sale inner join item_info on current_sale.item_code = item_info.item_code order by transac_datetime asc";
				
				Object[] result_from_query = Database_Utility.query(sqlQuery);
				Connection connect = (Connection) result_from_query[0];
				ResultSet result = (ResultSet) result_from_query[1];
				
				user_login.newLine();
				System.out.println("L I S T  O F  P U R C H A S E\n");
				current_sale_table(result);
				
				Database_Utility.close(connect);
				
				current_sale_total();
				
				System.out.println("\n\n[a]Complete\n"
								+ "[b]Add\n"
								+ "[c]Edit\n"
								+ "[d]Delete an Item\n"
								+ "[e]Delete All & Back\n");
				
				System.out.print("Enter choice: ");
				choice = Character.toLowerCase(scan.next().charAt(0));
				scan.nextLine();
				
				if(choice == 'a') {
					complete_current_sale();
				}
				else if(choice == 'b') {
					ctr = false;
				}
				else if(choice == 'c') {
					edit_current_sale();
				}
				else if(choice == 'd') {
					current_sale_delete_an_item();
				}
				else if(choice == 'e') {
					current_sale_deleteAll_back();
					//add delete all method
					ctr = false;
				}
				else {
					System.out.println("\nERROR: Invalid input!\n"
							+ "Press Enter to try again...");
					scan.nextLine();
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}while(ctr);
	}
	
	void complete_current_sale() {
		boolean ctr = true;
		do {
			user_login.newLine();
			System.out.print("Confirm to complete the sale\n\n"
					+ "[a]Confirm\n"
					+ "[b]Back\n"
					+ "Enter your choice: ");
			switch(scan.next().charAt(0)) {
				case 'a':
					try{
						String sqlUpdate1 = "INSERT INTO sales select * from current_sale";
						Object[] result_from_update1 = Database_Utility.update(sqlUpdate1);
						int result1 = (int) result_from_update1[1];
						Database_Utility.close((Connection) result_from_update1[0]); 
						
						String sqlUpdate2 = "DELETE FROM current_sale";
						Object[] result_from_update2 = Database_Utility.update(sqlUpdate2);
 						int result2 = (int) result_from_update2[1];
 						Database_Utility.close((Connection) result_from_update2[0]);
							if(result1>0 && result2>0) {
								
							}
							else {
								System.out.println("ERROR: Can't Upadate sales!\n"
										+ "Press Enter to try again...");
								scan.nextLine();
							}
						ctr = false;
					}catch(Exception e) {
						e.printStackTrace();
					}
					break;
				case 'b':
					ctr = false;
					break;
				default:
					System.out.println("ERROR:Invalid input!\n"
							+ "Press Enter to try again...");
					scan.nextLine();
					break;
			}
		}while(ctr);	
		}
	

	void edit_current_sale(){
		
		boolean ctr = true;
		do {
			try {
			String sqlQuery = "select current_sale.item_code, sale_code, item_name, price, quantity, total_price, name, transac_datetime from current_sale inner join item_info on current_sale.item_code = item_info.item_code order by transac_datetime asc";
			Object[] result_from_query = Database_Utility.query(sqlQuery);
			
			Connection connect = (Connection) result_from_query[0];
			ResultSet result = (ResultSet) result_from_query[1];
			
			user_login.newLine();
			System.out.println("E D I T\n");
			current_sale_table(result);
			
			current_sale_total();
			
			System.out.print("\nEnter the number of the product you want to edit: ");
			int item_want_to_edit = scan.nextInt();
			scan.nextLine();
			
			System.out.println("\n[a]Item Code\n"
					+ "[b]Quantity\n"
					+ "[c]Back");
			
			System.out.print("\nEnter the part you want to edit: ");
			int part_to_edit = Character.toLowerCase(scan.next().charAt(0));
			scan.nextLine();
			
	
			
			for(int i = 1; i<=item_want_to_edit; i++) {
				result.next();
				if(i == item_want_to_edit) {
					
					switch(part_to_edit) {
					case 'a':
							String sqlUpdate_item_code = "UPDATE current_sale SET item_code = ? WHERE sale_code = ?";
							System.out.print("Enter new item code: ");
							String new_item_code = scan.nextLine();
							
							Object[] result_from_update_item_code = Database_Utility.update(sqlUpdate_item_code, new_item_code, item_want_to_edit);
							Connection connect_in_edit_item_code = (Connection) result_from_update_item_code[0];
							int result_in_edit =(int) result_from_update_item_code[1];
							
							if(result_in_edit>0) {
								
							}
							else {
								System.out.println("ERROR: Update failed!");
							}
							
							Database_Utility.close(connect_in_edit_item_code);
						break;
					
					case 'b':
							String sqlUpdate_quantity = "UPDATE current_sale SET quantity = ? WHERE sale_code = ?";
							System.out.print("Enter new item quantity: ");
							String new_quantity = scan.nextLine();
							
							Object[] result_from_update_quantity = Database_Utility.update(sqlUpdate_quantity, new_quantity, item_want_to_edit);
							Connection connect_in_edit_quantity = (Connection) result_from_update_quantity[0];
							int result_in_edit_quantity =(int) result_from_update_quantity[1];
							
							if(result_in_edit_quantity>0) {
								
							}
							else {
								System.out.println("ERROR: Update failed!");
							}
							
							Database_Utility.close(connect_in_edit_quantity);
						break;
						
					case 'c':
						
							ctr = false;
							
						break;
						
					default:
						System.out.println("\nERROR: Invalid input!\n"
								+ "Press Enter to try again...");
						scan.nextLine();
				}
					
				}
			}
			
			Database_Utility.close(connect);
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			}while(ctr);
			
	}
	
	
	void current_sale_delete_an_item(){
		user_login.newLine();
		boolean ctr = true;
		do{
				String sqlQuery = "select current_sale.item_code, sale_code, item_name, price, quantity, total_price, name, transac_datetime from current_sale inner join item_info on current_sale.item_code = item_info.item_code order by transac_datetime asc";
				
				Object[] result_from_query = Database_Utility.query(sqlQuery);
				Connection connect1 = (Connection) result_from_query[0];
				ResultSet result1 = (ResultSet) result_from_query[1];
				
				System.out.println("D E L E T E  A N  I T E M\n");
				current_sale_table(result1);
				current_sale_total();
				
				System.out.print("\n\nEnter the sale code you of the item you want to delete: ");
				int sale_code = scan.nextInt();
				scan.nextLine();
				
				String sqlUpdate_specific_item = "DELETE FROM current_sale WHERE sale_code = ?";
				Object[] result_from_update = Database_Utility.update(sqlUpdate_specific_item,sale_code);
				Connection connect2 = (Connection) result_from_update[0];
				int result2 = (int) result_from_update[1];
				
				if(result2>0) {
					ctr = false;
				}
				else {
					System.out.print("ERROR: Invalid input!"
							+ "Press Enter to try again...");
					scan.nextLine();
				}
		}
		while(ctr);
	}
	
	
	void current_sale_deleteAll_back() {
		String sqlUpdate = "DELETE FROM current_sale";
		Object[] result_from_update = Database_Utility.update(sqlUpdate);
		
	}
	
	boolean store_to_current_sale(String item_code, int quantity, String name){
		
		try {
			String sqlUpdate = "INSERT INTO current_sale(item_code, quantity, name) values(?,?,?)";
			Object[] result_from_update = Database_Utility.update(sqlUpdate, item_code, quantity, name);
			Connection connect = (Connection) result_from_update[0];
			int result = (int) result_from_update[1];
			
				if(result>0) {
					Database_Utility.close(connect);
					return true;
				}
				else {
					return false;
				}
		}catch(Exception e) {
			System.out.print("\nERROR: Invalid input!\n"
							+ "Press Enter to try again...");
			scan.nextLine();
			return false;
		}
	}
	
	
	
	
	
}
