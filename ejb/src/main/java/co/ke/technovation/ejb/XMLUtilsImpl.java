package co.ke.technovation.ejb;

import java.math.BigDecimal;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

@Stateless
public class XMLUtilsImpl implements XMLUtilsI {
	
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
	
	
	public BigDecimal toBigint(String value){
		if(value!=null && !value.trim().isEmpty())
		try{
			return new BigDecimal(value);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return BigDecimal.ZERO;
	}

}
