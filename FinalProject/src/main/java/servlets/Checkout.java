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
@WebServlet("/checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Checkout() {
        super();
    }

    /**
     * Sends the client to farm_homepage.html
     * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("farm_homepage.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		
		System.out.println("read user_id: " + user_id);

		
		JDBCConnector db = new JDBCConnector();
		int status = 0;
		List<Product> cart = db.getUserCart(user_id);
		
		status = db.buyCart(user_id);;
			
		PrintWriter out = response.getWriter();
		if (status < 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Bad Add/Remove";
			out.print(error);
			out.flush();
			
		}
		else {
			Gson gson = new Gson();
	        String json = new Gson().toJson(cart);
	        json = json.substring(0, json.length() - 1);
	        json += ", currentBalance: " + db.getUserBalance(user_id) + "}";
			response.setStatus(HttpServletResponse.SC_OK);
			out.print(json);
			out.flush();
		}
		
	}
}
