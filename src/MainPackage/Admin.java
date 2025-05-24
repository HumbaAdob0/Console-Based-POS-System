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
					"[d] Make a Sale\n" +
					"[e] Logout\n");
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
					break;
				case 'd':
					make_a_sale(name);
					break;
				case 'e':
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


	void admin_inventory() {
		try {
			System.out.print("I N V E N T O R Y\n\n");
			System.out.print("[a] Add New Product" +
					"[b] Edit Product" +
					"[c] Add Stocks" +
					"[d] Pull-out Stocks" +
					"[e] Delete Product" +
					"[f] Back\n");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
