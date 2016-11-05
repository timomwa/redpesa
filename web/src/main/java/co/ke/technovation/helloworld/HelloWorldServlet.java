package co.ke.technovation.helloworld;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.ke.technovation.ejb.MsisdnEJBI;
import co.ke.technovation.ejb.RedCrossPaymentsEJBI;

@WebServlet("/index.jsp")
public class HelloWorldServlet extends HttpServlet {
	
	@EJB
	private RedCrossPaymentsEJBI paymentsEJBI;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		paymentsEJBI.mimicPayment();
		resp.getWriter().println(".. Tres Bien ..");
	}
	
	

}
