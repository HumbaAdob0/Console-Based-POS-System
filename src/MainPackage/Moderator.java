package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Moderator extends Base_user{

	Moderator(String username) {
		super(username); //This class inherit to the parent class Base_user, the super keyword refers to the parent class constructor
		 				 //the param username came from the UserLogin Class
	}


	
}
