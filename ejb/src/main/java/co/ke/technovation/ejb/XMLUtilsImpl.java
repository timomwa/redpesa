package co.ke.technovation.ejb;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
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
import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.ProcessingStatus;
import co.ke.technovation.entity.RedCrossPayment;
import co.ke.technovation.xmlutils.Envelope;

@Stateless
@Remote(XMLUtilsI.class)
public class XMLUtilsImpl implements XMLUtilsI {
	
	
	private DateFormat source_format = new SimpleDateFormat("yyyyMMddHHmmss");
	private DateFormat source_format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
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
	
	
	@Override
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
	public Integer toInteger(String value){
		if(value!=null && !value.trim().isEmpty())
		try{
			return new Integer(value);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return Integer.valueOf(0);
	}


	@Override
	public MpesaOut parseB2CQueryTimeout(String xmlc, String ip_addr) throws Exception{
		
		MpesaOut mpesaOut = null;
		
		try{
			
			mpesaOut = new MpesaOut();
			
			JAXBContext jc = JAXBContext.newInstance(Envelope.class);
		    Unmarshaller unmarshaller = jc.createUnmarshaller();
		    Envelope envelope = (Envelope)  unmarshaller.unmarshal( new StringReader(xmlc) );
	        String cdata_xml = envelope.getBody().getResultMsg().getValue();
	        
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(cdata_xml));
		    Document doc = builder.parse(is);
		    doc.getDocumentElement().normalize();
		    
		    String resultType = doc.getElementsByTagName("ResultType").item(0).getTextContent();
		    String resultCode = doc.getElementsByTagName("ResultCode").item(0).getTextContent();
		    String resultDesc = doc.getElementsByTagName("ResultDesc").item(0).getTextContent();
		    String originatorConversationID = doc.getElementsByTagName("OriginatorConversationID").item(0).getTextContent();
		    String conversationID = doc.getElementsByTagName("ConversationID").item(0).getTextContent();
		    String TransactionID = doc.getElementsByTagName("TransactionID").item(0).getTextContent();
		    
		    mpesaOut.setResultType( toInteger(resultType ) );
		    mpesaOut.setResultCode( toInteger(resultCode ) );
		    mpesaOut.setResultDesc( resultDesc );
		    mpesaOut.setOriginatorConversationID(originatorConversationID);
		    mpesaOut.setConversationID(conversationID);
		    mpesaOut.setTransId(TransactionID);
		   			   
		    NodeList nList = doc.getElementsByTagName(B2C_RESULTPARAMETER);
		    
		    for (int temp = 0; temp < nList.getLength(); temp++) {
		    	
		    	Node nNode = nList.item(temp);
		    	
		    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		    		Element eElement = (Element) nNode;
					
					String key  = eElement.getElementsByTagName("Key").item(0).getTextContent().trim();
					String val  = eElement.getElementsByTagName("Value").item(0).getTextContent().trim();
					
					if(key.equals(B2C_TRANSACTIONRECEIPT)){
						mpesaOut.setTransactionReceipt(val);
					}
					if(key.equals(B2C_TRANSACTIONAMOUNT)){
						mpesaOut.setTransactionAmount( toBigDecimal(val) );
					}
					if(key.equals(B2C_CHARGES_PAID_ACCOUNT_AVAILABLE_FUNDS)){
						mpesaOut.setB2CChargesPaidAccountAvailableFunds( toBigDecimal(val) );
					}
					if(key.equals(B2C_RECIPIENT_IS_REGISTERED_CUSTOMER)){
						mpesaOut.setB2CRecipientIsRegisteredCustomer( val.equalsIgnoreCase("Y") );
					}
					if(key.equals(B2C_TRANSACTION_COMPLETED_DATE_TIME)){
						Date transactionCompletedDateTime = null;
						try {
							transactionCompletedDateTime = source_format2.parse( val );
							mpesaOut.setTransactionCompletedDateTime( transactionCompletedDateTime );
						} catch (ParseException e) {
							logger.error(e.getMessage());
						}
					}
					if(key.equals(B2C_RECEIVER_PARTY_PUBLIC_NAME)){
						mpesaOut.setReceiverPartyPublicName( val );
					}
					if(key.equals(B2C_WORKING_ACCOUNT_AVAILABLE_FUNDS)){
						mpesaOut.setB2CWorkingAccountAvailableFunds( toBigDecimal( val ) );
					}
					if(key.equals(B2C_UTILITY_ACCOUNT_AVAILABLE_FUNDS)){
						mpesaOut.setB2CUtilityAccountAvailableFunds( toBigDecimal( val ) );
					}
		    	}
		    }
	    
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
	    
		return mpesaOut;
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
