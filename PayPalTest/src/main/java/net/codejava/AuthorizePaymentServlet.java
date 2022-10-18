package net.codejava;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.paypal.base.rest.PayPalRESTException;

@WebServlet("/authorize_payment")
public class AuthorizePaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AuthorizePaymentServlet() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String product =request.getParameter("product");
		String subtotal =request.getParameter("subtotal");
		String shipping =request.getParameter("shipping");
		String tax =request.getParameter("tax");
		String total =request.getParameter("total");
		
		OrderDetail orderdetail=new OrderDetail(product,subtotal,shipping,tax,total);
		try {
		PaymentServices paymentServices=new PaymentServices();
		String approvalLink=paymentServices.authorizePayment(orderdetail);
		response.sendRedirect(approvalLink);
		}
		catch(PayPalRESTException ex){
			ex.printStackTrace();
			request.setAttribute("errorMessage", ex.fillInStackTrace().getMessage());//we can write invalid payment after error message
			request.getRequestDispatcher("error.jsp").forward(request, response);
			
		}
		
	}

}
