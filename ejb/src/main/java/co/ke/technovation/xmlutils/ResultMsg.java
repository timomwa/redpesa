
/**
 * ResponseMsg.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:09:26 BST)
 */

package co.ke.technovation.xmlutils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ResultMsg")
public class ResultMsg {
	
	protected String value;

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ResultMsg [value=" + value + "]";
	}
	
	
}
