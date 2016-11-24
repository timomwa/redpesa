package co.ke.technovation.safaricom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/mpesa/b2c/queuetimeout")
public class QueueTimeoutURL extends HttpServlet  {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6799840305066155862L;
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
			String xml = getBody(req);
			logger.info("\n\n ip_addr-> "+ ip_addr +", \n\n XML ----> "+xml+"\n\n");
			pw.print("OK");
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

	
	public String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    InputStream inputStream = null;
	    
	    try {
	        inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        logger.error(ex.getMessage(),ex);
	    } finally {
	    	
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	            	 logger.error(ex.getMessage(),ex);
	            }
	        }
	        
	        try {
	        	inputStream.close();
            } catch (IOException ex) {
            }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
}
