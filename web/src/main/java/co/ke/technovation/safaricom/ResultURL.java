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
import co.ke.technovation.ejb.MpesaOutEJBI;
import co.ke.technovation.ejb.MpesaOutRawXMLEJBI;
import co.ke.technovation.ejb.RedCrossWinnerEJBI;
import co.ke.technovation.ejb.XMLUtilsI;
import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.MpesaOutRawXML;
import co.ke.technovation.entity.ProcessingStatus;

@WebServlet("/mpesa/b2c/resulturl")
public class ResultURL extends HttpServlet {
	
	@EJB
	private MpesaOutRawXMLEJBI mpesaOutRawXMLEJB;
	
	@EJB
	private RedCrossWinnerEJBI redcrossWinnerEJB;
	
	@EJB
	private MpesaOutEJBI mpesaOutEJB;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@EJB
	private CounterEJBI counterEJB;
	
	private final static String MPESA_OUTS_COUNTER = "MPESA_OUTFLOW";
		
	private static final String B2C_RESPONSE = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
												+"<soap:Body>"
												+"<ns2:ResponseMsg xmlns:ns2=\"http://api-v1.gen.mm.vodafone.com/mminterface/result\">"
												+"<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
												+"<response xmlns=\"http://api-v1.gen.mm.vodafone.com/mminterface/response\">"
												+"<ResponseCode>00000000</ResponseCode>"
												+"<ResponseDesc>success</ResponseDesc>"
												+"<ConversationID></ConversationID>"
												+"<OriginatorConversationID>${CONVERSATION_ID}</OriginatorConversationID>"
									            +"<ServiceStatus></ServiceStatus>"
									            +"</response>]]>"
									            +"</ns2:ResponseMsg>"
									    		+"</soap:Body>"
									    		+"</soap:Envelope>";
	
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
			
			logger.info("\n\n ip_addr-> "+ ip_addr +", \n\n XML REQ <<<<<<< "+xml+"\n\n");
			
			String conversationid = "0";
			
			if(xml!=null && !xml.trim().isEmpty()){
				
				MpesaOutRawXML mpesaOutRawXML = mpesaOutRawXMLEJB.logRequest(xml);
				
				MpesaOut mpesaOut = xmlUtils.parseB2CQueryTimeout(xml, ip_addr);
				mpesaOut.setRaw_xml_id(mpesaOutRawXML.getId());
				mpesaOut.setSourceip(ip_addr);
				
				mpesaOut = mpesaOutEJB.save(mpesaOut);
				
				mpesaOutRawXML.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				mpesaOut.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				
				mpesaOutRawXML = mpesaOutRawXMLEJB.save(mpesaOutRawXML);
				mpesaOut = mpesaOutEJB.save(mpesaOut);
				
				conversationid = mpesaOut.getConversationID();
				
				redcrossWinnerEJB.updatePaymentStatus(mpesaOut);
				
			}
			
			resp.setContentType("text/xml");
			resp.setCharacterEncoding("UTF-8");
			
			String resp_xml_ = B2C_RESPONSE.replaceAll("\\$\\{CONVERSATION_ID\\}", conversationid);
			
			logger.info("\n\n ip_addr-> "+ ip_addr +", \n\n XML RESP  >>>>>> "+resp_xml_+"\n\n");
			
			pw.print(resp_xml_);
			
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
