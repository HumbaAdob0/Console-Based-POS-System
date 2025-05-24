package MainPackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class Base_user{
	
	Scanner scan = new Scanner(System.in);
	UserLogin user_login = new UserLogin();
	
	/*
	 * This method queries for 'acc_type' and 'name' for a greetings.
	 * 
	 * @param username came from the query of UserLogin class
	 * the username is then used to query for the acc_type and name of the account
	 * 
	 */
	Base_user(){
		
	}

	void greetings(String username){
		try {
			String sqlQuery = "SELECT name, acc_type FROM accounts WHERE username = ?";
			Object[] result_from_query = Database_Utility.query(sqlQuery, username);

			Connection connect = (Connection)result_from_query[0];
			ResultSet result = (ResultSet)result_from_query[1];

			if(result.next()){
				System.out.println("Hi " +result.getString("acc_type") +" " +result.getString("name") +"!");
			}
			else {
				System.out.println("\nERROR: Name and role not found!");
			}

			Database_Utility.close(connect);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * This is the dashboard for our base user, it only has two option either to make a sale or logout
	 */
	void base_user_Dashboard(String name) {
		char choice; //the variable where our input is stored
		boolean ctr = true; //this is the control variable that help us check whether the inputed value is correct
		
		do {
		System.out.println("\nD A S H B O A R D\n\n"  
				+ "[a] Make a sale\n"
				+ "[b] Logout\n");

		
		switch(choice()) {
		case 'a':
			make_a_sale(name);
			break;
			
		case 'b':
			ctr = false;
			break;
			
		default:
			invalid_input_error_Message();
		}
		
		user_login.newLine();
		
		}while(ctr);
	}
	
	
	
	
	/*
	 * This method is where the item's code/ barcode is inputed and the quantity of the item.
	 * 
	 */
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

					
					switch(choice()) {
						case 'a':
							if(store_to_current_sale(item_code, quantity, name)!=true) { // the method 'store_to_current_sale' returns boolean value
								break;													 // the logic here is that when 'store_to_current_sale' returns false, it means that updating the database wasn't successful
							}															 // this conditional statement would be true, executing the break keyword
							list_of_purchase();											 //  and terminating the whole switch statement and going back to the start since its a do-while loop
							ctr = false;
							break;
							
						case 'b':
							ctr = false;
							break;
							
						default:
							invalid_input_error_Message(); // this error handling is when user input value that isnt within the switch case option
						
				}
			}catch(Exception e) {
				invalid_input_error_Message(); // this error handling is when I inputted the quantity values that is not int data type
				scan.nextLine();
			}
				
		}while(ctr);
	
	}
	
	
	
	/*
	 * This sums up the 'total_price' in our database
	 */
	void current_sale_total(){
		try {
		String sqlQuery = "select sum(total_price) as total_amount from current_sale";
		Object[] result_from_query = Database_Utility.query(sqlQuery);
		
		Connection connect = (Connection) result_from_query[0];
		ResultSet result = (ResultSet) result_from_query[1];
		
		if(result.next()) {
			System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t Total Amount: "); 
			if(result.getString("total_amount")==null) { // Every time the 'current_sale' table is empty it would return and print null
				System.out.println("0.00");				 //so what i did is when it's null it would print '0.00' and when it has value it would print the actual value
			}
			else {
				System.out.println(result.getString("total_amount"));
			}
		}
		
		Database_Utility.close(connect);
		
		}catch(Exception e) {
			invalid_input_error_Message();
		}
	}
	
	
	
	/*
	 * This method recieves a ResultSet object(similar to a table) then the loop below would iterate the contents of the ResultSet
	 * made this method for reusability
	 * 
	 * used the format specifier to display the values of the table (e.g. %d(int)
	 * the '-' means left aligned
	 * the number int the format specifier means the reserve spaces
	 * 
	 */
	void table(ResultSet result) {
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
	
	
	
	
	/*
	 * This method is where the Query is, and then being passed to the 'current_sale_table'
	 * this method also redirect us to the methods of the actions we want to do through switch
	 */
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
				table(result);
				
				Database_Utility.close(connect);
				
				current_sale_total();
				
				System.out.println("\n\n[a]Complete\n"
								+ "[b]Add\n"
								+ "[c]Edit\n"
								+ "[d]Delete an Item\n"
								+ "[e]Delete All & Back\n");

				switch (choice()){
					case 'a':
						complete_current_sale();
						ctr = false;
						break;
					case 'b':
						ctr = false;
						break;
					case 'c':
						edit_current_sale();
						break;
					case 'd':
						current_sale_delete_an_item();
						break;
					case 'e':
						current_sale_deleteAll_back();
						ctr = false;
						break;
					default:
						invalid_input_error_Message();
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}while(ctr);
	}
	
	
	
	/*
	 * When a sale(current_sale) is completed the info is then transfered to the 'sale' table where all successful transaction is stored
	 */
	void complete_current_sale() {
		boolean ctr = true;
		char choice;
		do {
				user_login.newLine();
				System.out.print("Confirm to complete the sale\n\n"
						+ "[a]Confirm\n"
						+ "[b]Back\n"
						+ "Enter your choice: ");
				switch(choice()) {
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
							ctr = false;
							
						}catch(Exception e) {
							System.out.println("\nERROR: Can't complete transaction!\n"
									+ "Press Enter to try again...");
							scan.nextLine();
						}
						break;
					case 'b':
						ctr = false;
						break;
					default:
						invalid_input_error_Message();
				}
		}while(ctr);	
		}
	

	
	/*
	 * This method lets you pick which info you want to edit
	 * In this role(base user or cashier) it is only allowed to edit two info which is the item_code and quantity
	 * 
	 */
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
			table(result);
			
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
			
	
			
			for(int i = 1; i<=item_want_to_edit; i++) { // this loop iterates on the ResultSet(table) until 'i' equals to our 'item_want_to_edit' 
				result.next();
				if(i == item_want_to_edit) {
					
					switch(part_to_edit) {	//after looking for the item we want to edit, this switch statement picks the info we want to edit
					case 'a':
							String sqlUpdate_item_code = "UPDATE current_sale SET item_code = ? WHERE sale_code = ?";
							System.out.print("Enter new item code: ");
							String new_item_code = scan.nextLine();
							
							Object[] result_from_update_item_code = Database_Utility.update(sqlUpdate_item_code, new_item_code, item_want_to_edit);
							Connection connect_in_edit_item_code = (Connection) result_from_update_item_code[0];
							int result_in_edit =(int) result_from_update_item_code[1];
							
							if(result_in_edit>0) {
								ctr = false;
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
								ctr = false;
							}
							else {
								invalid_input_error_Message();
							}
							
							Database_Utility.close(connect_in_edit_quantity);
						break;
						
					case 'c':
							ctr = false;
						break;
						
					default:
						invalid_input_error_Message();
				}
					
				}
			}
			
			Database_Utility.close(connect);
			
			
			}catch(Exception e) {
				invalid_input_error_Message();
			}
			
			}while(ctr);
			
	}
	
	
	/*
	 * This deletes an item to our 'current_sale'
	 */
	void current_sale_delete_an_item(){
		user_login.newLine();
		boolean ctr = true;
		do{
				String sqlQuery = "select current_sale.item_code, sale_code, item_name, price, quantity, total_price, name, transac_datetime from current_sale inner join item_info on current_sale.item_code = item_info.item_code order by transac_datetime asc";
				
				Object[] result_from_query = Database_Utility.query(sqlQuery);
				Connection connect1 = (Connection) result_from_query[0];
				ResultSet result1 = (ResultSet) result_from_query[1];
				
				System.out.println("D E L E T E  A N  I T E M\n");
				table(result1);
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
					invalid_input_error_Message();
				}
		}
		while(ctr);
	}
	
	/*
	 * This deletes all the info inside the 'current_sale'
	 */
	void current_sale_deleteAll_back() {
		String sqlUpdate = "DELETE FROM current_sale";
		Object[] result_from_update = Database_Utility.update(sqlUpdate);
		
	}
	
	
	
	/*
	 * This is used when storing new items to the database
	 * it returns true if it was stored sucessfull
	 * and false when not
	 */
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
			invalid_input_error_Message();
			return false;
		}
	}
	
	void invalid_input_error_Message() {
		System.out.print("\nERROR: Invalid input!\n"
				+ "Press Enter to continue...");
		scan.nextLine();
	}

	void success_Message() {
		System.out.print("\nSUCCESS!\n"
				+ "Press Enter to continue...");
		scan.nextLine();
	}


	char choice(){
		System.out.print("Enter your choice: ");
		char choice = Character.toLowerCase(scan.next().charAt(0)); // input is automatically made into lowercase to avoid error in comparing the value at switch
		scan.nextLine();
		return choice;
	}
	
	
}
