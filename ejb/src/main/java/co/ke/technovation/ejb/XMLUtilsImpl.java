package co.ke.technovation.ejb;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import co.ke.technovation.entity.RedCrossPayment;

@Stateless
public class XMLUtilsImpl implements XMLUtilsI {
	
	private static final String FIRST_NAME_KEY = "[Personal Details][First Name]";
	private static final String MIDDLE_NAME_KEY = "[Personal Details][Middle Name]";
	private static final String LAST_NAME_KEY = "[Personal Details][Last Name]";
	
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
