package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {
	
	private Connection connection;
	
	public JDBCConnector() {
		String filePath = "src/main/sql/sql_username_and_password";
		try {
			BufferedReader configReader = new BufferedReader(new FileReader(filePath));
			String username = configReader.readLine();
			String password = configReader.readLine();
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/BirdsAndBees?user=" + username + "&password=" + password);
			
			System.out.println("Successfully connected to the database");
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: couldn't find config file at " + filePath + " : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Couldn't read username or password from " + filePath + " : " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't read MySQL Driver class. Make sure the JAR is included in webapp/WEB-INF/lib : " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQLException occurred when trying to connect to the database: " + e.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
		JDBCConnector db = new JDBCConnector();
	}
}
