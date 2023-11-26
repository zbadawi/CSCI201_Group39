package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.User;

public class JDBCConnector {
	
	private static Connection connection = null;
	private static String mySQLusername = null, mySQLpassword = null;
	
	public JDBCConnector() {
		String filePath = "src/main/sql/sql_username_and_password";
		try {
			
			if (mySQLusername == null || mySQLpassword == null) {				
				BufferedReader configReader = new BufferedReader(new FileReader(filePath));
				mySQLusername = configReader.readLine();
				mySQLpassword = configReader.readLine();
			}
			
			if (connection == null) {				
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost/BirdsAndBees?user=" + mySQLusername + "&password=" + mySQLpassword);
				System.out.println("Successfully connected to the database");
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: couldn't find config file at " + filePath + " : " + e.getMessage());
			mySQLusername = null;
			mySQLpassword = null;
		} catch (IOException e) {
			System.out.println("Couldn't read username or password from " + filePath + " : " + e.getMessage());
			mySQLusername = null;
			mySQLpassword = null;
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't read MySQL Driver class. Make sure the JAR is included in webapp/WEB-INF/lib : " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQLException occurred when trying to connect to the database: " + e.getMessage());
		}
		
	}
	
	public void close() {
		try {
			connection.close();
			connection = null;
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
	
	/**
	 * Inserts a new user into the database.
	 * 
	 * @return 0 if successful, -1 if username already exists
	 */
	public int insertNewUser(User user) {

		int status = 0;

		try {
			String sql = "INSERT INTO Users (username, password, account_type) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.username);
			statement.setString(2, user.password);
			statement.setInt(3, user.account_type);
			statement.executeUpdate();
			
			status = loginUser(user.username, user.password);
		} 
		
		catch (SQLException e) {
			// handle duplicate entries
			String message = e.getMessage();
			if (message.contains("Duplicate entry")) {
				status = -1;
			} 
			else {
				// other problem occurred
				System.out.println("SQLException: " + e.getMessage());
			}
		}
		return status;
	} 
	
	/**
	 * Returns user_id if successful, -1 if username doesn't exist, -2 if
	 * password is incorrect
	 */
	public int loginUser(String username, String password) {
		int status = 0;
		
		try {
			String sql = "SELECT user_id, password FROM Users WHERE username=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);

			ResultSet results = statement.executeQuery();

			if (!results.next()) { // username does not exist
				status = -1;
			} 
			else if (!results.getString(2).equals(password)) { // password is wrong
				status = -2;
			} 
			else {
				status = results.getInt(1); // status = userID
			}
		} 
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		return status;
	}
	
	/**
	 * Adds product into the database. Returns 0 if successful, -1 if error
	 * */
	public int addProduct(Product p) {
		int status = 0;
		
		try {
			String sql = "INSERT INTO Products (name, price, vendor_id, image_url, quantity_available) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, p.name);
			statement.setDouble(2, p.price);
			statement.setInt(3, p.vendor_id);
			statement.setString(4, p.image_url);
			statement.setInt(5, p.quantity);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			status = -1;
		}
		return status;
	}
	
	/**
	 * Removes product from the database. Returns 0 if successful, -1 if error
	 * */
	public int removeProduct(int product_id) {
		int status = 0;
		
		try {
			String sql = "DELETE FROM Products WHERE product_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, product_id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			status = -1;
		}
		return status;
	}
	
	/**
	 * Returns list of vendor's products (empty if none)
	 */
	public List<Product> getVendorProducts(int vendor_id) {
		List<Product> products = new ArrayList<Product>();
		
		try {
			String sql = "SELECT * FROM Products WHERE vendor_id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, vendor_id);
			
			ResultSet results = statement.executeQuery();
			
			while (results.next()) {
				Product p = new Product(results.getInt(1),
										results.getString(2),
										results.getDouble(3),
										results.getInt(4),
										results.getString(5),
										results.getInt(6));
				
				products.add(p);
			}
		}
		catch (SQLException e) {
			System.out.println("SQLException occurred: " + e.getMessage());
		}
		
		return products;
	}
	
	/**
	 * Adds product to user's cart. Returns 0 if success, -1 if error
	 * */
	public int addProductToCart(int user_id, int product_id, int quantity) {
		int status = 0;
		
		try {
			String sql = "INSERT INTO Carts (user_id, product_id, quantity, purchased) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setInt(2, product_id);
			statement.setInt(3, quantity);
			statement.setBoolean(4, false);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			status = -1;
		}
		return status;
	}
	
	/**
	 * Removes product from user's cart. Returns 0 if success, -1 if error
	 */
	public int removeProductFromCart(int user_id, int product_id) {
		int status = 0;
		
		try {
			String sql = "DELETE FROM Carts WHERE user_id = ? AND product_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setInt(2, product_id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			status = -1;
		}
		
		return status;
	}
	
	public List<Product> getUserCart(int user_id) {
		List<Product> products = new ArrayList<Product>();
		
		try {
			String sql = "SELECT p.product_id, p.name, p.price, p.vendor_id, p.image_url, Carts.quantity "
					   + "FROM Products p JOIN Carts ON p.product_id = Carts.product_id "
					   + "WHERE Carts.user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			
			ResultSet results = statement.executeQuery();
			
			while (results.next()) {
				Product p = new Product(results.getInt(1),
										results.getString(2),
										results.getDouble(3),
										results.getInt(4),
										results.getString(5),
										results.getInt(6));
				
				products.add(p);
			}
		}
		catch (SQLException e) {
			System.out.println("SQLException occurred: " + e.getMessage());
		}
		
		return products;
	}
	
	public static void main(String[] args) {
		JDBCConnector db = new JDBCConnector();
		
//		System.out.println(db.insertNewUser(new User("nate", "password", 0)));
//		System.out.println(db.insertNewUser(new User("jerry", "fish", 0)));
//		System.out.println(db.insertNewUser(new User("farmer_jeff", "ilovecarrots", 1)));
		
//		int bob = db.insertNewUser(new User("farmer_bob", "cornisthebest", 1));
//		System.out.println(bob);
//		
//		Product carrots = new Product("Carrots", 1.50, 3, "img/carrots", 100);
//		Product lettuce = new Product("Lettuce", 2.50, 3, "img/lettuce", 300);
//		Product corn = new Product("Lettuce", 3.75, bob, "img/corn", 50);
//		
//		System.out.println(db.addProduct(carrots));
//		System.out.println(db.addProduct(lettuce));
//		System.out.println(db.addProduct(corn));
		
//		List<Product> jeffsProducts = db.getVendorProducts(3);
//		for (Product p : jeffsProducts) {
//			System.out.println(p.toString());
//		}
		
		
		//db.addProductToCart(2, 2, 40);
		//db.removeProductFromCart(2, 2);
		
		List<Product> natesCart = db.getUserCart(2);
		for (Product p : natesCart) {
			System.out.println(p.toString());
		}
		
	}
}
