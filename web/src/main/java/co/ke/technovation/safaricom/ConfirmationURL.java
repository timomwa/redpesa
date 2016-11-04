package co.ke.technovation.safaricom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import co.ke.technovation.ejb.MpesaRawEJBI;
import co.ke.technovation.ejb.XMLUtilsI;
import co.ke.technovation.entity.CallType;

@WebServlet("/mpesa/confirmation")
public class ConfirmationURL extends HttpServlet {
	
	@EJB
	private MpesaRawEJBI mpesaInflowEJB;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	private Logger logger = Logger.getLogger(getClass());
	private String response = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			+" xmlns:c2b=\"http://cps.huawei.com/cpsinterface/c2bpayment\"> "
			+" <soapenv:Header/> "
			+" <soapenv:Body> "
			+" <c2b:C2BPaymentConfirmationResult>C2B Payment Transaction 1234560000007031 result"
			+" received.</c2b:C2BPaymentConfirmationResult> " //Result received indicates success, otherwise failed."
			+" </soapenv:Body>"
			+" </soapenv:Envelope>";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter pw = resp.getWriter();
		
		try{
			String xml = getBody(req);
			
			String transType = xmlUtils.getValue(xml, "TransType");
			String transID = xmlUtils.getValue(xml, "TransID");
			String transTime = xmlUtils.getValue(xml, "TransTime");
			String transAmount = xmlUtils.getValue(xml, "TransAmount");
			String businessShortCode = xmlUtils.getValue(xml, "BusinessShortCode");
			String billRefNumber = xmlUtils.getValue(xml, "BillRefNumber");
			String orgAccountBalance = xmlUtils.getValue(xml, "OrgAccountBalance");
			String msisdn = xmlUtils.getValue(xml, "MSISDN");
			
			
			StringBuffer sb = new StringBuffer();
			sb.append("\t\t TransType :").append(transType).append("\n");
			sb.append("\t\t TransID :").append(transID).append("\n");
			sb.append("\t\t TransTime :").append(transTime).append("\n");
			sb.append("\t\t TransAmount :").append(transAmount).append("\n");
			sb.append("\t\t BusinessShortCode :").append(businessShortCode).append("\n");
			sb.append("\t\t BillRefNumber :").append(billRefNumber).append("\n");
			sb.append("\t\t InvoiceNumber :").append(orgAccountBalance).append("\n");
			sb.append("\t\t MSISDN :").append(msisdn).append("\n");
			
			
			logger.info( "\n\n ConfirmationURL --> "+ xml +"\n\n");
			
			logger.info( "\n\n Extracted --> "+ sb.toString() +"\n\n");
			
			mpesaInflowEJB.logRequest(xml, CallType.CONFIRMATION);
			
			resp.setContentType("text/xml");
			pw.println(response);
			
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
