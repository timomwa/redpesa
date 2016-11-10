package co.ke.technovation.ejb;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.ProcessingStatus;
import co.ke.technovation.entity.RedCrossPayment;

@Stateless
@Remote(XMLUtilsI.class)
public class XMLUtilsImpl implements XMLUtilsI {
	
	private static final String FIRST_NAME_KEY = "[Personal Details][First Name]";
	private static final String MIDDLE_NAME_KEY = "[Personal Details][Middle Name]";
	private static final String LAST_NAME_KEY = "[Personal Details][Last Name]";
	private DateFormat source_format = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private Logger logger = Logger.getLogger(getClass());
	
	public String getValue(String xml,String tagname) {
		if(xml==null || xml.isEmpty() || tagname==null || tagname.isEmpty())
			return "";
		xml = xml.toLowerCase();
		tagname = tagname.toLowerCase();
		String startTag = "<"+tagname+">";
		String endTag = "</"+tagname+">";
		int start = xml.indexOf(startTag)+startTag.length();
		int end  = xml.indexOf(endTag);
		if(start<0 || end<0)
			return "";
		return xml.substring(start, end);
	}
	
	
	public BigDecimal toBigDecimal(String value){
		if(value!=null && !value.trim().isEmpty())
		try{
			return new BigDecimal(value);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return BigDecimal.ZERO;
	}

	
	@Override
	public MpesaIn parseXML(String xml, CallType callType){ 
		
		String transType = getValue(xml, "TransType");
		String transID = getValue(xml, "TransID");
		String transTime = getValue(xml, "TransTime");
		String transAmount = getValue(xml, "TransAmount");
		String businessShortCode = getValue(xml, "BusinessShortCode");
		String billRefNumber = getValue(xml, "BillRefNumber");
		String orgAccountBalance = getValue(xml, "OrgAccountBalance");
		String msisdn = getValue(xml, "MSISDN");
		Date transactionTime = null;
		try {
			transactionTime = source_format.parse(transTime);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		MpesaIn mpesaIn = new MpesaIn();
		mpesaIn.setBillRefNumber(billRefNumber);
		mpesaIn.setBusinessShortcode(businessShortCode);
		mpesaIn.setCallType(callType);
		mpesaIn.setOrgAccountBalance( toBigDecimal( orgAccountBalance ) );
		mpesaIn.setRaw_xml_id( null );//To be set once we save the xml id
		mpesaIn.setStatus(ProcessingStatus.JUST_IN.getCode());
		mpesaIn.setTransAmount( toBigDecimal( transAmount) );
		mpesaIn.setTransId(transID);
		mpesaIn.setTransType(transType);
		mpesaIn.setSourceip( null );//To be set elsewhere
		mpesaIn.setMsisdn(msisdn);
		mpesaIn.setTransTime( transactionTime );
		mpesaIn.setTimeStamp( new Date() );
		try {
			mpesaIn = populateValues(xml, mpesaIn);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return mpesaIn;
	}
	
	
	@Override
	public MpesaIn populateValues(String xml, MpesaIn payment) throws Exception{
		
		try{
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(xml));
		    Document doc = builder.parse(is);
		    doc.getDocumentElement().normalize();
		    
		    NodeList nList = doc.getElementsByTagName("KYCInfo");
		    for (int temp = 0; temp < nList.getLength(); temp++) {
		    	Node nNode = nList.item(temp);
		    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
					Element eElement = (Element) nNode;
					
					String key  = eElement.getElementsByTagName("KYCName").item(0).getTextContent();
					String val  = eElement.getElementsByTagName("KYCValue").item(0).getTextContent();
					
					if(key.trim().equalsIgnoreCase(FIRST_NAME_KEY)){
						payment.setFirst_name(val);
					}else if(key.trim().equalsIgnoreCase(MIDDLE_NAME_KEY)){
						payment.setMiddle_name(val);
					}else if (key.trim().equalsIgnoreCase(LAST_NAME_KEY)){
						payment.setLast_name(val);
					}
		    	}
	
		    }
		    
		}catch(org.xml.sax.SAXParseException spe){
			logger.warn(spe.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	    

    	
    	return payment;
	}

	@Override
	public RedCrossPayment populateValues(String xml, RedCrossPayment payment) throws Exception {
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    Document doc = builder.parse(is);
	    doc.getDocumentElement().normalize();
	    
	    NodeList nList = doc.getElementsByTagName("KYCInfo");
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	    	Node nNode = nList.item(temp);
	    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				String key  = eElement.getElementsByTagName("KYCName").item(0).getTextContent();
				String val  = eElement.getElementsByTagName("KYCValue").item(0).getTextContent();
				
				if(key.trim().equalsIgnoreCase(FIRST_NAME_KEY)){
					payment.setFirst_name(val);
				}else if(key.trim().equalsIgnoreCase(MIDDLE_NAME_KEY)){
					
				}else if (key.trim().equalsIgnoreCase(LAST_NAME_KEY)){
					payment.setLast_name(val);
				}
	    	}

	    }
	    

    	
    	return payment;
	}

}
