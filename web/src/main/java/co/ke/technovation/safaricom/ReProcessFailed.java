package co.ke.technovation.safaricom;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import co.ke.technovation.ejb.ReprocessorEJBI;

@WebServlet("/mpesa/reprocessfailed")
public class ReProcessFailed extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -252229768212233968L;
	@EJB
	private ReprocessorEJBI reprocessorEJB;
	private Logger logger = Logger.getLogger(getClass());
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter pw = resp.getWriter();
		
		try{
			
			String ip_addr = req.getRemoteAddr();
			int processed = reprocessorEJB.process(ip_addr);
			pw.println("<processed>"+processed+"</processed>");
			
		}catch(Exception e){
			
			logger.error(e.getMessage() , e);
			
		}finally{
			
			try{
				pw.close();
			}catch(Exception e){
				logger.error(e.getMessage() , e);
			}
			
		}
	}
	
	
	

}
