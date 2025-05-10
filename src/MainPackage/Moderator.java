package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Moderator extends Base_user {

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
					+ "[a] View Sales\n"
					+ "[b] Add Stocks\n"
					+ "[c] Pull-out Stocks\n"
					+ "[d] Logout");
			System.out.print("Enter your choice: ");
			choice = Character.toLowerCase(scan.next().charAt(0)); // input is automatically made into lowercase to avoid error in comparing the value at switch
			scan.nextLine();

			switch (choice) {
				case 'a':
					view_sale();
					break;
				case 'b':
					add_stocks();
					break;
				case 'c':
					pull_out_stocks();
					break;
				case 'd':
					ctr = false;
					break;
				default:
					invalid_input_error_Message();
			}
		}while (ctr) ;
		}

	void view_sale() {
		user_Login.newLine();
		boolean ctr = true;
		char choice;
		do {
			try {
				Object[] result_from_query = Database_Utility.query("select sales.item_code, sale_code, item_name, price, quantity, total_price, name, transac_datetime from sales inner join item_info on sales.item_code = item_info.item_code order by transac_datetime asc");
				Connection connect = (Connection) result_from_query[0];
				ResultSet result = (ResultSet) result_from_query[1];
				user_Login.newLine();
				table(result);

				System.out.println("\n\n[a] back");
				System.out.print("Enter your choice: ");
				choice = scan.next().charAt(0);
				scan.nextLine();
				switch(choice){
					case 'a':
						ctr = false;
						break;
					default:
						invalid_input_error_Message();
				}
				Database_Utility.close(connect);
				user_Login.newLine();
			}catch(Exception e) {
				invalid_input_error_Message();
			}

		} while (ctr);
	}

	void item_info_table(ResultSet result) {
		try {
			System.out.print("--------------------------------------------------------------------------\n");
			System.out.printf("  %-21s %-23s %-18s %-10s", "Item Code", "Item Name", "Price", "Stocks");
			System.out.print("\n--------------------------------------------------------------------------");
			while (result.next()) {
				System.out.printf("\n   %-13s %-30s %-20.2f %-14d\n", result.getString("item_code"), result.getString("item_name"), result.getBigDecimal("price"), result.getInt("stocks"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	void add_stocks() {
		user_Login.newLine();
		boolean ctr = true;
		char choice;
		do {
			try {
				Object[] result_from_query = Database_Utility.query("select * from item_info");
				Connection connect = (Connection) result_from_query[0];
				ResultSet result = (ResultSet) result_from_query[1];
				item_info_table(result);

				System.out.println("\n\n[a] Add Stock\n"
								+ "[b] Back");
				System.out.print("Enter your choice: ");
				choice = scan.next().charAt(0);
				scan.nextLine();

				switch(choice){
					case 'a':
						add_stocks_case_a();
						break;
					case 'b':
						ctr = false;
						break;
					default:
						invalid_input_error_Message();
				}
				Database_Utility.close(connect);
				user_Login.newLine();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}while(ctr);
	}

	void add_stocks_case_a(){
			try {
				user_Login.newLine();
				String sql_query = "select * from item_info";
				Object[] result_from_query = Database_Utility.query(sql_query);
				Connection connect = (Connection) result_from_query[0];
				ResultSet result = (ResultSet) result_from_query[1];
				item_info_table(result);

				System.out.print("\n\nEnter the item code to add stocks: ");
				String item_code = scan.nextLine();
				System.out.print("Enter the number of stocks to add: ");
				int stocks_to_add = scan.nextInt();
				scan.nextLine();
				String sql_update = "UPDATE item_info SET stocks = stocks + ? WHERE item_code = ?";
				Object[] result_from_update = Database_Utility.update(sql_update, stocks_to_add, item_code);
				Database_Utility.close((Connection) result_from_update[0]);

				Database_Utility.close(connect);
				user_Login.newLine();

			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	void pull_out_stocks() {
		boolean ctr = true;
			do {
				try {
					user_Login.newLine();
					String sql_query = "select * from item_info";
					Object[] result_from_query = Database_Utility.query(sql_query);
					Connection connect = (Connection) result_from_query[0];
					ResultSet result = (ResultSet) result_from_query[1];
					item_info_table(result);

					System.out.println("\n\n[a] Pull-out Stock\n" +
							"[b] Back");
					System.out.print("Enter your choice: ");
					char choice = scan.next().charAt(0);
					scan.nextLine();
					switch (choice) {
						case 'a':
							pull_out_stocks_case_a();
							break;
						case 'b':
							ctr = false;
						default:
							invalid_input_error_Message();
					}
					Database_Utility.close(connect);
					user_Login.newLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}while(ctr);
	}

	void pull_out_stocks_case_a(){
		try {
			user_Login.newLine();
			String slq_query = "select * from item_info";
			Object[] result_from_query = Database_Utility.query(slq_query);
			Connection connect = (Connection) result_from_query[0];
			item_info_table((ResultSet) result_from_query[1]);

			System.out.print("\n\nEnter the item code to pull out stocks: ");
			String item_code = scan.nextLine();
			System.out.print("Enter the number of stocks to pull out: ");
			int stocks = scan.nextInt();
			scan.nextLine();

			String sql_update = "UPDATE item_info SET stocks = stocks - ? WHERE item_code = ?";
			Object[] result_from_update = Database_Utility.update(sql_update, stocks, item_code);
			Database_Utility.close((Connection) result_from_update[0]);

			Database_Utility.close(connect);
			user_Login.newLine();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}



