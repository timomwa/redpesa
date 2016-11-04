package co.ke.technovation.helloworld;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.ke.technovation.ejb.MsisdnEJBI;

@WebServlet("/index.jsp")
public class HelloWorldServlet extends HttpServlet {
	
	@EJB
	private MsisdnEJBI msisdnEJB;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		msisdnEJB.testWrite("254720988636");
		resp.getWriter().println(".. Tres Bien ..");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//msisdnEJB.testWrite("254720988636");
		resp.getWriter().println(".. Tres Bien ..");
	}
	
	

}
