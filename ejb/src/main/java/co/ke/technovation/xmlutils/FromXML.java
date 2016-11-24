package co.ke.technovation.xmlutils;

import java.io.FileInputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FromXML {
	
	public static void main(String[] args) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Envelope.class);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    Envelope envelope = (Envelope)  unmarshaller.unmarshal( new FileInputStream("success_b2c_callback.xml") );
        String xml = envelope.getBody().getResultMsg().getValue();
        System.out.println(xml);
        
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    Document doc = builder.parse(is);
	    doc.getDocumentElement().normalize();
	    
	    String resultType = doc.getElementsByTagName("ResultType").item(0).getTextContent();
	    String resultCode = doc.getElementsByTagName("ResultCode").item(0).getTextContent();
	    String resultDesc = doc.getElementsByTagName("ResultDesc").item(0).getTextContent();
	    String originatorConversationID = doc.getElementsByTagName("OriginatorConversationID").item(0).getTextContent();
	    String conversationID = doc.getElementsByTagName("ConversationID").item(0).getTextContent();
	    String TransactionID = doc.getElementsByTagName("TransactionID").item(0).getTextContent();
	    
	    StringBuffer sb = new StringBuffer();
	    sb.append(" resultType = ").append(resultType).append("\n");
	    sb.append(" resultCode = ").append(resultCode).append("\n");
	    sb.append(" resultDesc = ").append(resultDesc).append("\n");
	    sb.append(" originatorConversationID = ").append(originatorConversationID).append("\n");
	    sb.append(" conversationID = ").append(conversationID).append("\n");
	    sb.append(" TransactionID = ").append(TransactionID).append("\n");
		   
	    NodeList nList = doc.getElementsByTagName("ResultParameter");
	    sb.append(" ResultParameters Count = ").append(nList.getLength()).append("\n");
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	    	Node nNode = nList.item(temp);
	    	
	    	
	    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	    		Element eElement = (Element) nNode;
				
				String key  = eElement.getElementsByTagName("Key").item(0).getTextContent();
				String val  = eElement.getElementsByTagName("Value").item(0).getTextContent();
				sb.append(temp).append(". ").append(key).append(" = ").append(val).append("\n");
	    	}
	    }
	    
	    System.out.println(sb.toString());
	}
	
}
