package MainPackage;

import java.sql.Connection;
import java.sql.ResultSet;

public class Admin extends Base_user {
	Moderator moderator = new Moderator();

	Admin() {
	}

	void admin_Dashboard(String name) {
		boolean ctr = true;
		char choice;
		do {
			System.out.print("\n[a] Account Management\n" +
					"[b] Inventory\n" +
					"[c] Sales\n" +
					"[d] Logout\n");
			System.out.print("Enter your choice: ");
			choice = scan.next().charAt(0);
			scan.nextLine();

			switch (choice) {
				case 'a':
					account_management();
					break;
				case 'b':
					admin_inventory();
					break;
				case 'c':
					admin_sales();
					break;
				case 'd':
					ctr = false;
			}

			user_login.newLine();
		} while (ctr);
	}

	void account_management() {

		boolean ctr = true;
		do {
			user_login.newLine();
			System.out.print("A C C O U T  M A N A G E M E N T\n");
			accounts_table();
			System.out.print("[a] Add New Account\n" +
					"[b] Edit Account\n" +
					"[c] Delete Account\n" +
					"[d] Back\n");

			switch (choice()) {
				case 'a':
					add_new_account();
					break;
				case 'b':
					edit_account();
					break;
				case 'c':
					delete_account();
					break;
				case 'd':
					ctr = false;
					break;
				default:
					invalid_input_error_Message();
			}
		}while (ctr);

}

	void add_new_account() {
		do {
			try {
				user_login.newLine();
				System.out.print("A D D  N E W  A C C O U T\n\n");
				System.out.print("Enter username: ");
				String username = scan.nextLine();
				String sql_query = "SELECT * FROM accounts WHERE username = ?";
				Object[] result_from_query = Database_Utility.query(sql_query, username);
				ResultSet result = (ResultSet) result_from_query[1];
				if (result.next()) {
					invalid_input_error_Message();
					user_login.newLine();
					break;
				} else {
					System.out.print("Enter password: ");
					String password = scan.nextLine();
					System.out.print("Enter name: ");
					String name = scan.nextLine();
					System.out.print("\nEnter the role of the account: ");
					System.out.print("\n[a] Admin\n" +
							"[b] Moderator\n" +
							"[c] Cashier\n");
					String acc_type = "";
					switch (choice()) {
						case 'a':
							acc_type = "admin";
							add_new_account_a(username, password, name, acc_type);
							break;
						case 'b':
							acc_type = "moderator";
							add_new_account_a(username, password, name, acc_type);
							break;
						case 'c':
							acc_type = "cashier";
							add_new_account_a(username, password, name, acc_type);
							break;
						default:
							invalid_input_error_Message();
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while(true);
	}

	void add_new_account_a(String username, String password, String name, String acc_type){
		try{
			String sql_update = "INSERT INTO accounts (username, password, name, acc_type) VALUES (?, ?, ?, ?)";
			Object[] result_from_update = Database_Utility.update(sql_update, username, password, name, acc_type);
			int result = (int) result_from_update[1];

			if(result>0) {
				success_Message();
			}
			else {
				System.out.println("\nAccount creation failed!");
			}

			Database_Utility.close((Connection) result_from_update[0]);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	void accounts_table() {
		try {
			String sql_query = "SELECT * FROM accounts";
			Object[] result_from_query = Database_Utility.query(sql_query);

			ResultSet result = (ResultSet) result_from_query[1];
			System.out.println("----------------------------------------------------------------");
			System.out.printf("%-13s %-13s %-21s %-20s\n", "Username", "Password", "Account Type", "Name");
			System.out.println("----------------------------------------------------------------");
			while (result.next()) {
				System.out.printf("%-13s %-15s %-15s %-20s\n\n", result.getString("username"), result.getString("password"), result.getString("acc_type"), result.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



	void edit_account() {
		boolean ctr = true;
		do {
			try {
				user_login.newLine();
				System.out.print("E D I T  A C C O U N T S\n\n");
				accounts_table();
				System.out.print("\nEnter the username of the account you want to edit: ");
				String username = scan.nextLine();
				String sql_query = "SELECT * FROM accounts WHERE username = ?";
				Object[] result_from_query = Database_Utility.query(sql_query, username);
				ResultSet result = (ResultSet) result_from_query[1];
				if(!result.next()) {
					System.out.println("\nERROR: Account not found!");
					Database_Utility.close((Connection) result_from_query[0]);
					ctr = false;
				}
				else {
					System.out.println("\nEnter the field you want to edit:");
					System.out.print("[a] Change Username\n" +
									"[b] Change Password\n" +
									"[c] Change Role\n" +
									"[d] Change Name\n");
					switch (choice()) {
						case 'a':
							user_login.newLine();
							System.out.print("\nEnter the new username: ");
							String new_username = scan.nextLine();
							String sql_update_username = "UPDATE accounts SET username = ? WHERE username = ?";
							Object[] result_from_update_username = Database_Utility.update(sql_update_username, new_username, username);
							if((int) result_from_update_username[1] > 0) {
								user_login.newLine();
								accounts_table();
								success_Message();
								ctr = false;
							}
							Database_Utility.close((Connection) result_from_update_username[0]);
							break;
						case 'b':
							user_login.newLine();
							System.out.print("\nEnter the new password: ");
							String new_password = scan.nextLine();
							String sql_update_password = "UPDATE accounts SET password = ? WHERE username = ?";
							Object[] result_from_update_password = Database_Utility.update(sql_update_password, new_password, username);
							if((int) result_from_update_password[1] > 0) {
								user_login.newLine();
								accounts_table();
								success_Message();
								ctr = false;
							}
							Database_Utility.close((Connection) result_from_update_password[0]);
							break;
						case 'c':
							user_login.newLine();
							System.out.print("\nEnter the new role: ");
							System.out.print("\n[a] Admin\n" +
									"[b] Moderator\n" +
									"[c] Cashier\n");
							String new_role = "";
							switch (choice()) {
								case 'a':
									new_role = "admin";
									break;
								case 'b':
									new_role = "moderator";
									break;
								case 'c':
									new_role = "cashier";
									break;
								default:
									invalid_input_error_Message();
							}

							String sql_update_role = "UPDATE accounts SET acc_type = ? WHERE username = ?";
							Object[] result_from_update_role = Database_Utility.update(sql_update_role, new_role, username);
							if((int) result_from_update_role[1] > 0) {
								user_login.newLine();
								accounts_table();
								success_Message();
								ctr = false;
							}
							Database_Utility.close((Connection) result_from_update_role[0]);
							break;
						case 'd':
							user_login.newLine();
							System.out.print("\nEnter the new name: ");
							String new_name = scan.nextLine();
							String sql_update_name = "UPDATE accounts SET name = ? WHERE username = ?";
							Object[] result_from_update_name = Database_Utility.update(sql_update_name, new_name, username);
							if((int) result_from_update_name[1] > 0) {
								user_login.newLine();
								accounts_table();
								success_Message();
								ctr = false;
							}
							Database_Utility.close((Connection) result_from_update_name[0]);
							break;
						default:
								System.out.println("\nERROR: Invalid input!");
								invalid_input_error_Message();
								Database_Utility.close((Connection) result_from_query[0]);
								ctr = false;
					}

				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}while(ctr);

	}

	void delete_account() {
		try {
			user_login.newLine();
			System.out.print("D E L E T E  A C C O U N T\n\n");
			accounts_table();
			System.out.print("\nEnter the username of the account you want to delete: ");
			String username = scan.nextLine();
			String sql_query = "DELETE FROM accounts WHERE username = ?";
			Object[] result_from_update = Database_Utility.update(sql_query, username);
			int result = (int) result_from_update[1];
			if(result>0) {
				success_Message();
			}
			else {
				invalid_input_error_Message();
			}
		}catch (Exception e) {
		 e.printStackTrace();
		}
	}

	/*
	 * Helper method to display the inventory table
	 */
	void display_inventory_table() {
		try {
			String sql_query = "SELECT * FROM item_info";
			Object[] result_from_query = Database_Utility.query(sql_query);
			Connection connect = (Connection) result_from_query[0];
			ResultSet result = (ResultSet) result_from_query[1];
			
			System.out.println("\nCurrent Inventory:");
			System.out.println("----------------------------------------------------------------");
			System.out.printf("%-21s %-22s %-11s %-10s\n", "Item Code", "Item Name", "Price", "Stocks");
			System.out.println("----------------------------------------------------------------");
			
			while(result.next()) {
				System.out.printf("%-15s %-28s %-13.2f %-10d\n",
						result.getString("item_code"),
						result.getString("item_name"),
						result.getDouble("price"),
						result.getInt("stocks"));
			}
			System.out.println();
			
			Database_Utility.close(connect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void admin_inventory() {
		boolean ctr = true;
		do {
			user_login.newLine();
			System.out.print("I N V E N T O R Y  M A N A G E M E N T\n");

			display_inventory_table();
			// Display menu options
			System.out.print("[a] Add New Item\n" +
					"[b] Edit Item\n" +
					"[c] Delete Item\n" +
					"[d] Back\n");
			System.out.print("Enter your choice: ");
			char choice = scan.next().charAt(0);
			scan.nextLine();
			
			switch(choice) {
				case 'a':
					add_new_item();
					break;
				case 'b':
					edit_item();
					break;
				case 'c':
					delete_item();
					break;
				case 'd':
					ctr = false;
					break;
				default:
					System.out.println("Invalid choice!");
					break;
			}
		} while(ctr);
	}

	/*
	 * This method adds a new item to the inventory
	 * It asks for item code, name, price, and initial stock
	 */
	void add_new_item() {
		try {
			user_login.newLine();
			System.out.println("A D D  N E W  I T E M\n");
			
			boolean validItemCode = false;
			String item_code = "";
			
			while (!validItemCode) {
				System.out.print("Enter Item Code (must be exactly 7 digits): ");
				item_code = scan.nextLine();
				
				if (item_code.length() != 7) {
					System.out.println("\nError: Item code must be exactly 7 digits!");
					System.out.print("Do you want to try again? (y/n): ");
					char retry = scan.next().charAt(0);
					scan.nextLine();
					user_login.newLine();
					if (retry != 'y' && retry != 'Y') {
						return;
					}
				} else if (!item_code.matches("\\d+")) {
					System.out.println("\nError: Item code must contain only digits!");
					System.out.print("Do you want to try again? (y/n): ");
					char retry = scan.next().charAt(0);
					scan.nextLine();
					user_login.newLine();
					if (retry != 'y' && retry != 'Y') {
						return;
					}
				} else {
					validItemCode = true;
				}
			}
			
			System.out.print("Enter Item Name: ");
			String item_name = scan.nextLine();
			
			System.out.print("Enter Price: ");
			double price = scan.nextDouble();
			
			System.out.print("Enter Initial Stock: ");
			int stocks = scan.nextInt();
			scan.nextLine();
			
			String sql_update = "INSERT INTO item_info (item_code, item_name, price, stocks) VALUES (?, ?, ?, ?)";
			Object[] result_from_update = Database_Utility.update(sql_update, item_code, item_name, price, stocks);
			int result = (int) result_from_update[1];
			
			if(result > 0) {
				System.out.println("\nItem added successfully!");
			} else {
				System.out.println("\nFailed to add item.");
			}
			
			Database_Utility.close((Connection) result_from_update[0]);
			System.out.print("Press Enter to continue...");
			scan.nextLine();
			
		} catch (Exception e) {
			System.out.println("Error adding item: " + e.getMessage());
			scan.nextLine();
		}
	}
	
	/*
	 * This method allows editing of existing items in the inventory
	 * User can modify item name, price, or stock quantity
	 */
	void edit_item() {
		try {
			user_login.newLine();
			System.out.println("E D I T  I T E M\n");
			
			// Display current inventory for reference
			display_inventory_table();
			
			System.out.print("Enter Item Code to edit: ");
			String item_code = scan.nextLine();
			
			// Verify item exists
			String sql_query = "SELECT * FROM item_info WHERE item_code = ?";
			Object[] result_from_query = Database_Utility.query(sql_query, item_code);
			ResultSet result = (ResultSet) result_from_query[1];
			
			if(result.next()) {
				System.out.print("\n[a] Edit Item Code\n" +
						"[b] Edit Item Name\n" +
						"[c] Edit Price\n" +
						"[d] Edit Stock\n" +
						"[e] Back\n");
				System.out.print("Enter your choice: ");
				char choice = scan.next().charAt(0);
				scan.nextLine();
				
				String sql_update = "";
				Object update_value = null;
				
				switch(choice) {
					case 'a':
						boolean validItemCode = false;
						String new_item_code = "";
						
						while (!validItemCode) {
							System.out.print("Enter new Item Code (must be exactly 7 digits): ");
							new_item_code = scan.nextLine();
							
							if (new_item_code.length() != 7) {
								System.out.println("\nError: Item code must be exactly 7 digits!");
								System.out.print("Do you want to try again? (y/n): ");
								char retry = scan.next().charAt(0);
								scan.nextLine();
								if (retry != 'y' && retry != 'Y') {
									return;
								}
							} else if (!new_item_code.matches("\\d+")) {
								System.out.println("\nError: Item code must contain only digits!");
								System.out.print("Do you want to try again? (y/n): ");
								char retry = scan.next().charAt(0);
								scan.nextLine();
								if (retry != 'y' && retry != 'Y') {
									return;
								}
							} else {
								validItemCode = true;
							}
						}
						update_value = new_item_code;
						sql_update = "UPDATE item_info SET item_code = ? WHERE item_code = ?";
						break;
					case 'b':
						System.out.print("Enter new Item Name: ");
						update_value = scan.nextLine();
						sql_update = "UPDATE item_info SET item_name = ? WHERE item_code = ?";
						break;
					case 'c':
						System.out.print("Enter new Price: ");
						update_value = scan.nextDouble();
						sql_update = "UPDATE item_info SET price = ? WHERE item_code = ?";
						break;
					case 'd':
						System.out.print("Enter new Stock quantity: ");
						update_value = scan.nextInt();
						sql_update = "UPDATE item_info SET stocks = ? WHERE item_code = ?";
						break;
					case 'e':
						return;
					default:
						System.out.println("Invalid choice!");
						return;
				}
				
				Object[] result_from_update = Database_Utility.update(sql_update, update_value, item_code);
				int update_result = (int) result_from_update[1];
				
				if(update_result > 0) {
					System.out.println("\nItem updated successfully!");

				} else {
				 System.out.println("\nFailed to update item.");
				}
				
				Database_Utility.close((Connection) result_from_update[0]);
			} else {
				System.out.println("\nItem not found!");
			}
			
			Database_Utility.close((Connection) result_from_query[0]);
			System.out.print("\nPress Enter to continue...");
			scan.nextLine();
			
		} catch (Exception e) {
		 System.out.println("Error editing item: " + e.getMessage());
		 scan.nextLine();
		}
	}
	
	/*
	 * This method deletes an item from the inventory
	 * Requires item code confirmation before deletion
	 */
	void delete_item() {
		try {
			user_login.newLine();
			System.out.println("D E L E T E  I T E M");
			
			// Display current inventory for reference
			display_inventory_table();
			
			System.out.print("Enter Item Code to delete: ");
			String item_code = scan.nextLine();
			
			System.out.print("Are you sure you want to delete this item? (y/n): ");
			char confirm = scan.next().charAt(0);
			scan.nextLine();
			
			if(confirm == 'y' || confirm == 'Y') {
				String sql_update = "DELETE FROM item_info WHERE item_code = ?";
				Object[] result_from_update = Database_Utility.update(sql_update, item_code);
				int result = (int) result_from_update[1];
				
				if(result > 0) {
					System.out.println("\nItem deleted successfully!");


				} else {
					System.out.println("\nFailed to delete item or item not found.");
				}
				
				Database_Utility.close((Connection) result_from_update[0]);
			}

			System.out.print("Press Enter to continue...");
			scan.nextLine();
			
		} catch (Exception e) {
			System.out.println("Error deleting item: " + e.getMessage());
			scan.nextLine();
		}
	}

	void admin_sales() {
		boolean ctr = true;
		do {
			user_login.newLine();
			System.out.print("S A L E S  M A N A G E M E N T\n\n");
			System.out.print("[a] View Sales\n" +
					"[b] Create New Sale\n" +
					"[c] Edit Sale\n" +
					"[d] Delete Sale\n" +
					"[e] Back\n");
			switch (choice()) {
				case 'a':
					view_sales();
					break;
				case 'b':
					create_new_sale();
					break;
				case 'c':
					edit_sale();
					break;
				case 'd':
					delete_sale();
					break;
				case 'e':
					ctr = false;
					break;
				default:
					invalid_input_error_Message();
			}
		} while(ctr);
	}

	/*
	 * Display sales records with filtering options
	 */
	void view_sales() {
		try {
			user_login.newLine();
			System.out.println("V I E W  S A L E S\n");
			
			// Display filtering options
			System.out.print("[a] View All Sales\n" +
					"[b] View Today's Sales\n" +
					"[c] View Sales by Date\n" +
					"[d] Back\n");
			System.out.print("Enter your choice: ");
			String sql_query = "";
			String date = "";
			switch (choice()) {
				case 'a':
					sql_query = "SELECT * FROM sales ORDER BY transac_datetime DESC";
					break;
				case 'b':
					sql_query = "SELECT * FROM sales WHERE DATE(transac_datetime) = CURDATE() ORDER BY transac_datetime DESC";
					break;
				case 'c':
					System.out.print("\nEnter date (YYYY-MM-DD): ");
					date = scan.nextLine();
					sql_query = "SELECT * FROM sales WHERE DATE(transac_datetime) = ? ORDER BY transac_datetime DESC";
					Object[] result_from_query = Database_Utility.query(sql_query,date);
					display_sales_table((ResultSet) result_from_query[1]);
					Database_Utility.close((Connection) result_from_query[0]);
					System.out.print("\nPress Enter to continue...");
					scan.nextLine();
					return;
				case 'd':
					return;
				default:
					invalid_input_error_Message();
					return;
			}
			
			Object[] result_from_query = Database_Utility.query(sql_query);
			display_sales_table((ResultSet) result_from_query[1]);
			Database_Utility.close((Connection) result_from_query[0]);
			
		} catch (Exception e) {
			System.out.println("Error viewing sales: " + e.getMessage());
		}
		System.out.print("\nPress Enter to continue...");
		scan.nextLine();
	}

	/*
	 * Helper method to display sales records in a formatted table
	 */
	void display_sales_table(ResultSet result) {
		try {
			System.out.println("\n-------------------------------------------------------------------------------------");
			System.out.printf("%-10s %-10s %-20s %-10s %-15s %-15s\n", 
				"Sale Code", "Item Code", "Transaction Date", "Quantity", "Total Price", "Cashier");
			System.out.println("-------------------------------------------------------------------------------------");
			
			while(result.next()) {
				System.out.printf("%-10s %-10s %-20s %-10d %-15.2f %-15s\n",
					result.getString("sale_code"),
					result.getString("item_code"),
					result.getString("transac_datetime"),
					result.getInt("quantity"),
					result.getDouble("total_price"),
					result.getString("name"));
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println("Error displaying sales table: " + e.getMessage());
		}
	}

	/*
	 * Create a new sale record
	 */
	void create_new_sale() {
		try {
			user_login.newLine();
			System.out.println("C R E A T E  N E W  S A L E\n");
			
			// Display inventory for reference
			display_inventory_table();
			
			System.out.print("Enter Cashier Name: ");
			String cashier_name = scan.nextLine();
			
			boolean adding_items = true;
			while (adding_items) {
				System.out.print("\nEnter Item Code (or 0 to finish): ");
				String item_code = scan.nextLine();
				
				if (item_code.equals("0")) {
					adding_items = false;
					list_of_purchase(); // Show the current sale items and options
					continue;
				}
				
				// Verify item exists and check stock
				String check_query = "SELECT * FROM item_info WHERE item_code = ?";
				Object[] check_result = Database_Utility.query(check_query, item_code);
				ResultSet item_result = (ResultSet) check_result[1];
				
				if (item_result.next()) {
					int available_stock = item_result.getInt("stocks");
					
					System.out.print("Enter quantity: ");
					int quantity = scan.nextInt();
					scan.nextLine();
					
					if (quantity <= 0) {
						System.out.println("Invalid quantity!");
						continue;
					}
					
					if (quantity > available_stock) {
						System.out.println("Insufficient stock! Available: " + available_stock);
						continue;
					}
					
					// Store to current_sale using inherited method from Base_user
					if (store_to_current_sale(item_code, quantity, cashier_name)) {
						System.out.println("\nItem added to current sale successfully!");
					} else {
						System.out.println("\nFailed to add item to current sale.");
					}
					
				} else {
					System.out.println("Item not found!");
				}
				Database_Utility.close((Connection) check_result[0]);
			}
			
		} catch (Exception e) {
			System.out.println("Error creating sale: " + e.getMessage());
		}
		System.out.print("\nPress Enter to continue...");
		scan.nextLine();
	}
	
	/*
	 * Edit an existing sale record with proper transaction handling and validation
	 */
	void edit_sale() {
		try {
			user_login.newLine();
			System.out.println("E D I T  S A L E\n");
			
			// Display all sales for reference with detailed item info
			String list_query = "SELECT s.*, i.item_name, i.price FROM sales s " +
                          "JOIN item_info i ON s.item_code = i.item_code " +
                          "ORDER BY s.transac_datetime DESC";
			Object[] list_result = Database_Utility.query(list_query);
			display_sales_table((ResultSet) list_result[1]);
			Database_Utility.close((Connection) list_result[0]);
			
			System.out.print("\nEnter Sale Code to edit: ");
			String sale_code = scan.nextLine();
			
			// Get current sale details including item information
			String check_query = "SELECT s.*, i.item_name, i.price, i.stocks " +
                           "FROM sales s " +
                           "JOIN item_info i ON s.item_code = i.item_code " +
                           "WHERE s.sale_code = ?";
			Object[] check_result = Database_Utility.query(check_query, sale_code);
			ResultSet sale_result = (ResultSet) check_result[1];
			
			if (sale_result.next()) {
				// Display current sale details
				System.out.println("\nCurrent Sale Details:");
				System.out.printf("Sale Code: %s\n", sale_result.getString("sale_code"));
				System.out.printf("Item Code: %s (%s)\n", 
					sale_result.getString("item_code"),
					sale_result.getString("item_name"));
				System.out.printf("Quantity: %d\n", sale_result.getInt("quantity"));
				System.out.printf("Price per Item: %.2f\n", sale_result.getDouble("price"));
				System.out.printf("Total Price: %.2f\n", sale_result.getDouble("total_price"));
				System.out.printf("Cashier: %s\n", sale_result.getString("name"));
				System.out.printf("Transaction Date: %s\n", sale_result.getString("transac_datetime"));
				
				System.out.print("\n[a] Edit Quantity\n" +
						"[b] Edit Cashier Name\n" +
						"[c] Back\n");
				
				switch (choice()) {
					case 'a':
						String item_code = sale_result.getString("item_code");
						double price_per_item = sale_result.getDouble("price");
						int current_quantity = sale_result.getInt("quantity");
						int available_stock = sale_result.getInt("stocks") + current_quantity; // Include current sale quantity
						
						System.out.printf("\nCurrent stock available: %d\n", available_stock);
						System.out.print("Enter new quantity: ");
						int new_quantity = scan.nextInt();
						scan.nextLine();
						
						if (new_quantity <= 0) {
							System.out.println("\nError: Quantity must be greater than 0!");
							break;
						}
						
						if (new_quantity > available_stock) {
							System.out.println("\nError: Insufficient stock available!");
							break;
						}
						
						// Calculate stock adjustment and new total
						int stock_difference = new_quantity - current_quantity;
						double new_total = price_per_item * new_quantity;
						
						// Update stock
						String update_stock = "UPDATE item_info SET stocks = stocks - ? WHERE item_code = ?";
						Object[] stock_update = Database_Utility.update(update_stock, stock_difference, item_code);
						
						if ((int) stock_update[1] > 0) {
							// Update sale with new quantity and total price 
							String update_sale = "UPDATE sales SET quantity = ?, total_price = ? WHERE sale_code = ?";
							Object[] sale_update = Database_Utility.update(update_sale, new_quantity, new_total, sale_code);
							
							if ((int) sale_update[1] > 0) {
								System.out.println("\nSale quantity updated successfully!");
								System.out.printf("New Total Price: %.2f\n", new_total);
								
								// Display updated sale details
								Object[] updated_result = Database_Utility.query(check_query, sale_code);
								System.out.println("\nUpdated Sale Details:");
								display_sales_table((ResultSet) updated_result[1]);
								Database_Utility.close((Connection) updated_result[0]);
							} else {
								// Rollback stock update if sale update failed
								Database_Utility.update(update_stock, -stock_difference, item_code);
								System.out.println("\nError: Failed to update sale!");
							}
							Database_Utility.close((Connection) sale_update[0]);
						} else {
							System.out.println("\nError: Failed to update stock!");
						}
						Database_Utility.close((Connection) stock_update[0]);
						break;
						
					case 'b':
						System.out.print("Enter new Cashier Name: ");
						String new_name = scan.nextLine();
						
						if (new_name.trim().isEmpty()) {
							System.out.println("\nError: Cashier name cannot be empty!");
							break;
						}
						
						String update_name = "UPDATE sales SET name = ? WHERE sale_code = ?";
						Object[] name_update = Database_Utility.update(update_name, new_name, sale_code);
						
						if ((int) name_update[1] > 0) {
							System.out.println("\nCashier name updated successfully!");
							// Display updated sale
							Object[] updated_name_result = Database_Utility.query(check_query, sale_code);
							System.out.println("\nUpdated Sale Details:");
							display_sales_table((ResultSet) updated_name_result[1]);
							Database_Utility.close((Connection) updated_name_result[0]);
						} else {
							System.out.println("\nError: Failed to update cashier name!");
						}
						Database_Utility.close((Connection) name_update[0]);
						break;
						
					case 'c':
						return;
						
					default:
						System.out.println("\nInvalid choice!");
						return;
				}
				
			} else {
				System.out.println("\nError: Sale not found!");
			}
			Database_Utility.close((Connection) check_result[0]);
			
		} catch (Exception e) {
			System.out.println("Error editing sale: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.print("\nPress Enter to continue...");
		scan.nextLine();
	}

	/*
	 * Delete a sale record with proper validation and stock restoration
	 */
	void delete_sale() {
		try {
			user_login.newLine();
			System.out.println("D E L E T E  S A L E\n");
			
			// Display all sales for reference with detailed item info
			String list_query = "SELECT s.*, i.item_name, i.price FROM sales s " +
							  "JOIN item_info i ON s.item_code = i.item_code " +
							  "ORDER BY s.transac_datetime DESC";
			Object[] list_result = Database_Utility.query(list_query);
			display_sales_table((ResultSet) list_result[1]);
			Database_Utility.close((Connection) list_result[0]);
			
			System.out.print("\nEnter Sale Code to delete: ");
			String sale_code = scan.nextLine();
			
			// Get full sale details including item information
			String check_query = "SELECT s.*, i.item_name, i.price, i.stocks " +
							   "FROM sales s " +
							   "JOIN item_info i ON s.item_code = i.item_code " +
							   "WHERE s.sale_code = ?";
			Object[] check_result = Database_Utility.query(check_query, sale_code);
			ResultSet sale_result = (ResultSet) check_result[1];
			
			if (sale_result.next()) {
				// Show sale details before deletion
				System.out.println("\nSale Details to Delete:");
				System.out.printf("Sale Code: %s\n", sale_result.getString("sale_code"));
				System.out.printf("Item Code: %s (%s)\n", 
					sale_result.getString("item_code"),
					sale_result.getString("item_name"));
				System.out.printf("Quantity: %d\n", sale_result.getInt("quantity"));
				System.out.printf("Total Price: %.2f\n", sale_result.getDouble("total_price"));
				System.out.printf("Cashier: %s\n", sale_result.getString("name"));
				System.out.printf("Transaction Date: %s\n", sale_result.getString("transac_datetime"));
				
				System.out.print("\nAre you sure you want to delete this sale? (y/n): ");
				char confirm = scan.next().charAt(0);
				scan.nextLine();
				
				if (confirm == 'y' || confirm == 'Y') {
					String item_code = sale_result.getString("item_code");
					int quantity = sale_result.getInt("quantity");
					
					// First restore the stock
					String restore_stock = "UPDATE item_info SET stocks = stocks + ? WHERE item_code = ?";
					Object[] stock_update = Database_Utility.update(restore_stock, quantity, item_code);
					
					if ((int) stock_update[1] > 0) {
						// Now delete the sale
						String delete_sale = "DELETE FROM sales WHERE sale_code = ?";
						Object[] sale_delete = Database_Utility.update(delete_sale, sale_code);
						
						if ((int) sale_delete[1] > 0) {
							System.out.println("\nSale deleted successfully!");
							System.out.printf("Stock quantity (%d) has been restored to item %s (%s)\n", 
								quantity, item_code, sale_result.getString("item_name"));
						} else {
							// Rollback stock update if sale deletion failed
							Database_Utility.update(restore_stock, -quantity, item_code);
							System.out.println("\nError: Failed to delete sale!");
						}
						Database_Utility.close((Connection) sale_delete[0]);
					} else {
						System.out.println("\nError: Failed to restore stock!");
					}
					Database_Utility.close((Connection) stock_update[0]);
				} else {
					System.out.println("\nDeletion cancelled.");
				}
			} else {
				System.out.println("\nError: Sale not found!");
			}
			Database_Utility.close((Connection) check_result[0]);
			
		} catch (Exception e) {
			System.out.println("Error deleting sale: " + e.getMessage());
		}
		System.out.print("\nPress Enter to continue...");
		scan.nextLine();
	}


}
