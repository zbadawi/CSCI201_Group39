package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import util.JDBCConnector;
import util.Product;

/**
 * Servlet implementation class Home
 */
@WebServlet("/buyer")
public class ProfileBuyer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProfileBuyer() {
        super();
    }

    /**
     * Returns list of user's previously purchased products
     * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		JDBCConnector db = new JDBCConnector();
		List<Product> purchasedProducts = db.getUserPurchasedProducts(user_id);
		String purchasedProductsString = new Gson().toJson(purchasedProducts);
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		response.getWriter().write(purchasedProductsString);
		response.getWriter().flush();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		double amount_added = Integer.parseInt(request.getParameter("amount_added"));
		
		System.out.println("read user_id: " + user_id);
		System.out.println("read amount_added: " + amount_added);
		
		JDBCConnector db = new JDBCConnector();
		int status = 0;
		
		double status_og = db.getUserBalance(user_id);
		if (status_og < 0) {
			status = -1;
		}
		else {
			status = db.setUserBalance(user_id, amount_added + status_og);
		}
	

		PrintWriter out = response.getWriter();
		if (status < 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Bad Add/Remove";
			out.print(error);
			out.flush();
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			out.print("Succesful Add/Remove");
			out.flush();
		}
	}
}
