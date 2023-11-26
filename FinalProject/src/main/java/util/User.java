package util;

public class User {

	public int user_id;
	public String username;
	public String password;
	public int account_type;
	public int current_balance;
	public int current_profit;
	public int products_purchased;

	public User(int user_id, String username, String password, int account_type, int current_balance,
			int current_profit, int products_purchased) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.account_type = account_type;
		this.current_balance = current_balance;
		this.current_profit = current_profit;
		this.products_purchased = products_purchased;
	}

	public User(String username, String password, int account_type) {
		this.username = username;
		this.password = password;
		this.account_type = account_type;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", account_type=" + account_type + "]";
	}

}
