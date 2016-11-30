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

import co.ke.technovation.ejb.CounterEJBI;
import co.ke.technovation.ejb.MpesaInEJBI;
import co.ke.technovation.ejb.MpesaRawEJBI;
import co.ke.technovation.ejb.MsisdnListEJBI;
import co.ke.technovation.ejb.RedCrossPaymentsEJBI;
import co.ke.technovation.ejb.XMLUtilsI;
import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.MpesaInRawXML;
import co.ke.technovation.entity.ProcessingStatus;
import co.ke.technovation.entity.RedCrossPayment;

@WebServlet("/mpesa/confirmation")
public class ConfirmationURL extends HttpServlet {
	
	@EJB
	private MpesaRawEJBI mpesaRawEJB;
	
	@EJB
	private RedCrossPaymentsEJBI redcrossPaymentsEJB;
	
	@EJB
	private MpesaInEJBI mpesaInEJB;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@EJB
	private CounterEJBI counterEJB;
	
	@EJB
	private MsisdnListEJBI msisdnListEJB;
	
	
	private final static String MPESA_INS_COUNTER = "MPESA_INFLOW";
	
	private Logger logger = Logger.getLogger(getClass());
	private String response = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			+" xmlns:c2b=\"http://cps.huawei.com/cpsinterface/c2bpayment\"> "
			+" <soapenv:Header/> "
			+" <soapenv:Body> "
			+" <c2b:C2BPaymentConfirmationResult>C2B Payment Transaction ${TRANSACTION_ID} result"
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
			String resp_const  = "";
			String xml = getBody(req);
			
			logger.info("\n-----xml -- > "+xml);
			
			if(xml!=null && !xml.trim().isEmpty()){
				
				String ip_addr = req.getRemoteAddr();
				
				MpesaInRawXML mpesa_raw_xml = mpesaRawEJB.logRequest(xml, CallType.CONFIRMATION);
				
				MpesaIn mpesaIn = xmlUtils.parseXML(xml, CallType.CONFIRMATION);
				
				mpesaIn.setSourceip( ip_addr );
				mpesaIn.setRaw_xml_id( mpesa_raw_xml.getId() );
				
				redcrossPaymentsEJB.savePayment( mpesaIn );
				
				mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
				
				mpesa_raw_xml.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				mpesaIn.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				
				mpesa_raw_xml = mpesaRawEJB.save(mpesa_raw_xml);
				mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
				
				resp_const = response.replaceAll("\\$\\{TRANSACTION_ID\\}", mpesaIn.getTransId());
				
				counterEJB.incrementCounter(MPESA_INS_COUNTER);
				
				msisdnListEJB.incrementCounter( mpesaIn.getMsisdn() );
				
			}else{
				
				resp_const = response.replaceAll("\\$\\{TRANSACTION_ID\\}", "0");
				
			}
			
			resp.setContentType("text/xml");
			
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
