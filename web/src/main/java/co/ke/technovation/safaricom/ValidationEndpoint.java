package co.ke.technovation.safaricom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

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

@WebServlet("/mpesa/validation")
public class ValidationEndpoint extends HttpServlet {
	
	@EJB
	private MpesaRawEJBI mpesaInflowEJB;
	
	private final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(50);
	
	private Logger logger = Logger.getLogger(getClass());
	private String response =  "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			 +"xmlns:c2b=\"http://cps.huawei.com/cpsinterface/c2bpayment\"> " 
			 +"<soapenv:Header/> "
			 +"<soapenv:Body> "
			 +"<c2b:C2BPaymentValidationResult> "
			 +"<ResultCode>0</ResultCode> " //0 is success, any other error code refer to the appendix.
			 +"<ResultDesc>Service processing successful</ResultDesc> "
			+"<ThirdPartyTransID>${TRANSACTION_ID}</ThirdPartyTransID> "
			+"</c2b:C2BPaymentValidationResult> "
			+"</soapenv:Body> "
			+"</soapenv:Envelope>";
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter pw = resp.getWriter();
		
		try{
			
			String xml = getBody(req);
			
			String TransType  = xmlUtils.getValue(xml, "TransType");
			String TransID  = xmlUtils.getValue(xml, "TransID");
			String TransTime = xmlUtils.getValue(xml, "TransTime");
			String TransAmount = xmlUtils.getValue(xml, "TransAmount");
			String BusinessShortCode = xmlUtils.getValue(xml, "BusinessShortCode");
			String BillRefNumber = xmlUtils.getValue(xml, "BillRefNumber");
			String InvoiceNumber = xmlUtils.getValue(xml, "InvoiceNumber");
			String MSISDN = xmlUtils.getValue(xml, "MSISDN");
			
			
			StringBuffer sb = new StringBuffer();
			sb.append("\t\t TransType :").append(TransType).append("\n");
			sb.append("\t\t TransID :").append(TransID).append("\n");
			sb.append("\t\t TransTime :").append(TransTime).append("\n");
			sb.append("\t\t TransAmount :").append(TransAmount).append("\n");
			sb.append("\t\t BusinessShortCode :").append(BusinessShortCode).append("\n");
			sb.append("\t\t BillRefNumber :").append(BillRefNumber).append("\n");
			sb.append("\t\t InvoiceNumber :").append(InvoiceNumber).append("\n");
			sb.append("\t\t MSISDN :").append(MSISDN).append("\n");
			
			logger.info( "\n\n ConfirmationURL --> "+ xml +"\n\n");
			
			logger.info( "\n\n Extracted --> "+ sb.toString() +"\n\n");
			
			mpesaInflowEJB.logRequest(xml, CallType.VALIDATION);
			
			resp.setContentType("text/xml");
			
			String resp_const = response.replaceAll("\\$\\{TRANSACTION_ID\\}", TransID);
			
			logger.info( "\n\n Extracted --> "+ resp_const +"\n\n");
			
			if(xmlUtils.toBigDecimal(TransAmount).compareTo(MIN_AMOUNT)<0)
				throw new Exception("Amount is less than Kes. 50. Payment rejected.");
		
			pw.println(resp_const);
			
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
