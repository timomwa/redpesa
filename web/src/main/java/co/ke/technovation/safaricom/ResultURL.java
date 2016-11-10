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

import co.ke.technovation.ejb.XMLUtilsI;

@WebServlet("/mpesa/b2c/resulturl")
public class ResultURL extends HttpServlet {
	private static final String resp_xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:res=\"http://api-v1.gen.mm.vodafone.com/mminterface/request\">"
   +"<soapenv:Header/>"
   +"<soapenv:Body>"
      +"<ResponseMsg><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><response xmlns=\"http://api-v1.gen.mm.vodafone.com/mminterface/response\"><ResponseCode>00000000</ResponseCode><ResponseDesc>success</ResponseDesc></response>]]></ResponseMsg>"
   +"</soapenv:Body>"
+"</soapenv:Envelope>";
	private static final String resp_xml2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:req=\"http://api-v1.gen.mm.vodafone.com/mminterface/result\">"
   +"<soapenv:Header/>"
   +"<soapenv:Body>"
   +"<req:ResponseMsg>Success</req:ResponseMsg>"
   +"</soapenv:Body>"
   +"</soapenv:Envelope>";
	
	private static final String resp_xml3 = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
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
	
	
	@EJB
	private XMLUtilsI xmlUtils;
	
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
			String conversationid = xmlUtils.getValue(xml, "ConversationID");
			
			logger.info("\n\n ip_addr-> "+ ip_addr +", \n\n XML REQ <<<<<<< "+xml+"\n\n");
			//resp.setContentType("text/xml");
			resp.setContentType("text/xml");
			resp.setCharacterEncoding("UTF-8");
			if(conversationid==null || conversationid.isEmpty())
				conversationid = "0";
			String resp_xml_ = resp_xml3.replaceAll("\\$\\{CONVERSATION_ID\\}", conversationid);
			
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
