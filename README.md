Simple Point-of-Sale System

This is a console-based Point-of-Sale (POS) system built in Java that interacts with a MySQL database. The system is designed to manage accounts, inventory, and sales for a small business.
	
Project Overview
The application features a user login system with different access levels: Admin, Moderator, and Cashier. Each role has specific permissions and access to different parts of the system's functionality, such as managing user accounts, adding/pulling out stock, and viewing sales reports.
Features

	Secure Login: Authenticates users based on their username and password.
		
	Role-Based Access: Provides different dashboards and functionalities for three distinct user roles:
		
	Admin: Manages user accounts (add, edit, delete), inventory, and sales.
		
	Moderator: Manages inventory (add and pull out stocks) and views sales.
		
	Cashier: Handles sales transactions.
		
	Inventory Management: Allows for tracking and updating item stock.
		
	Sales Tracking: Records and displays sales information.

Prerequisites

	To run this application, you will need to have the following installed on your machine:
	
	Java Development Kit (JDK): Version 8 or higher.
	
	MySQL Server: The database system that stores all user, item, and sales data.
	
	MySQL Connector/J: The JDBC driver that allows Java to connect to the MySQL database. Make sure this .jar file is included in your project's build path.


Database Setup
    
    Create the Database: First, you need to create a database named pos_database in your MySQL server. You can do this by running the following command in your MySQL client:
    
    CREATE DATABASE pos_database;
    
    Import the Schema: The database structure (tables) is defined in a .sql file. You should import this file into the pos_database you just created. This file contains the Data Definition Language (DDL) for all the required tables.
    
    Update Connection Details: Open the databaseCredential.properties file under the src directory and update the db.url, db.username, and db.password variables to match your MySQL server's credentials.


How to Run

    Build the Project: Compile all the .java files.
    
    Run the Main Class: Execute the Main.java file. The application will start in the console, prompting you to log in.
    
    Log in with a user account created in your database to access the dashboard.


Key Files

    Main.java: The entry point of the application. It initiates the login process.
    
    UserLogin.java: Manages the user authentication process and routes users to their respective dashboards based on their role. This is where you configure your database connection details.
    
    Database_Utility.java: A utility class for handling database connections and executing SQL queries and updates.
    
    Base_user.java: A base class for all user roles, containing common functionalities like greetings, viewing items, and managing sales.
    
    Admin.java: Extends Base_user and implements the Admin-specific functionalities, such as managing other user accounts.
    
    Moderator.java: Extends Base_user and handles moderator-specific tasks, primarily inventory management.

