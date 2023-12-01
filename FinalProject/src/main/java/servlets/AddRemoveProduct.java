package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import util.*;

/**
 * Servlet implementation class Login
 */
@WebServlet("/product")
public class AddRemoveProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddRemoveProduct() {
        super();
    }
    
    /**
     * Sends the client to farm_login.html
     * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("farm_login.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		String name = null;
		double price = 0;
		String image_url = null;
		int vendor_id = 0;
		int quantity = 0;
		int product_id = 0;
		
		if (action.equals("add")) {
			name = request.getParameter("name");
			price = Integer.valueOf(request.getParameter("price"));
			image_url = request.getParameter("image_url");
			vendor_id = Integer.valueOf(request.getParameter("vendor_id"));
			quantity = Integer.valueOf(request.getParameter("quantity"));
		} else {
			product_id = Integer.valueOf(request.getParameter("product_id"));
		}
		
		
		System.out.println("read action: " + action);
		System.out.println("read product_id: " + product_id);
		System.out.println("read quantity: " + quantity);
		System.out.println("read vendor_id: " + vendor_id);
		System.out.println("read product_id: " + product_id);
		System.out.println("read quantity: " + quantity);
		
		JDBCConnector db = new JDBCConnector();
		int status = 0;
		Product p = null;
		if (action.equals("add")) {
			p = new Product(name, price, vendor_id, image_url, quantity);
			status = db.addProduct(p);
		}
		else {
			p = db.getProductInfo(product_id);
			status = db.removeProduct(product_id);
			if(p == null) {
				status = -1;
			}
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
			// Just returning everything in a JSON, and letting front end choose what it wants to use
			Gson gson = new Gson();
			String productJSON = gson.toJson(p);
			out.print(productJSON);
			out.flush();
		}
	}
}