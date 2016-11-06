package co.ke.technovation.safaricom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import co.ke.technovation.ejb.MpesaInEJBI;
import co.ke.technovation.ejb.MpesaRawEJBI;
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
	private RedCrossPaymentsEJBI paymentsEJBI;
	
	@EJB
	private MpesaInEJBI mpesaInEJB;
	
	
	@EJB
	private XMLUtilsI xmlUtils;
	
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
			String xml = getBody(req);
			
			String transType = xmlUtils.getValue(xml, "TransType");
			String transID = xmlUtils.getValue(xml, "TransID");
			String transTime = xmlUtils.getValue(xml, "TransTime");
			String transAmount = xmlUtils.getValue(xml, "TransAmount");
			String businessShortCode = xmlUtils.getValue(xml, "BusinessShortCode");
			String billRefNumber = xmlUtils.getValue(xml, "BillRefNumber");
			String orgAccountBalance = xmlUtils.getValue(xml, "OrgAccountBalance");
			String msisdn = xmlUtils.getValue(xml, "MSISDN");
			String ip_addr = req.getRemoteAddr();
			
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("\t\t TransType :").append(transType).append("\n");
			sb.append("\t\t TransType :").append(transType).append("\n");
			sb.append("\t\t TransID :").append(transID).append("\n");
			sb.append("\t\t TransTime :").append(transTime).append("\n");
			sb.append("\t\t TransAmount :").append(transAmount).append("\n");
			sb.append("\t\t BusinessShortCode :").append(businessShortCode).append("\n");
			sb.append("\t\t BillRefNumber :").append(billRefNumber).append("\n");
			sb.append("\t\t InvoiceNumber :").append(orgAccountBalance).append("\n");
			sb.append("\t\t MSISDN :").append(msisdn).append("\n");
			
			
			MpesaInRawXML mpesa_raw_xml = mpesaRawEJB.logRequest(xml, CallType.CONFIRMATION);
			
			
			MpesaIn mpesaIn = new MpesaIn();
			mpesaIn.setBillRefNumber(billRefNumber);
			mpesaIn.setBusinessShortcode(businessShortCode);
			mpesaIn.setCallType(CallType.CONFIRMATION);
			mpesaIn.setOrgAccountBalance( xmlUtils.toBigDecimal( orgAccountBalance ) );
			mpesaIn.setRaw_xml_id(mpesa_raw_xml.getId());
			mpesaIn.setStatus(ProcessingStatus.JUST_IN.getCode());
			mpesaIn.setTransAmount( xmlUtils.toBigDecimal( transAmount) );
			mpesaIn.setTransId(transID);
			mpesaIn.setTransType(transType);
			mpesaIn.setSourceip(ip_addr);
			
			mpesaIn = xmlUtils.populateValues(xml, mpesaIn);
			
			RedCrossPayment payment = new RedCrossPayment();
			payment.setAmount(  xmlUtils.toBigDecimal( transAmount ) );
			payment.setIs_processed(Boolean.FALSE);
			payment.setPhone_number(msisdn);
			payment.setTelco_transaction_id(transID);
			payment.setFirst_name(mpesaIn.getFirst_name());
			payment.setLast_name(mpesaIn.getLast_name());
			
			

			sb.append("\t\t First name :").append(mpesaIn.getFirst_name()).append("\n");
			sb.append("\t\t Middle name :").append(mpesaIn.getMiddle_name() ).append("\n");
			sb.append("\t\t Last name :").append(mpesaIn.getLast_name()).append("\n");
			
			logger.debug( "\n\n Extracted Values --> "+ sb.toString() +"\n\n");
			
			mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
			payment = paymentsEJBI.savePayment(payment);
			
			mpesa_raw_xml.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
			mpesaIn.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
			
			mpesa_raw_xml = mpesaRawEJB.save(mpesa_raw_xml);
			mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
			
			resp.setContentType("text/xml");
			String resp_const = response.replaceAll("\\$\\{TRANSACTION_ID\\}", transID);
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
