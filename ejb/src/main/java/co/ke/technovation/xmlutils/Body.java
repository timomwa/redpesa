package co.ke.technovation.xmlutils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Body")
public class Body {
	
	
	private ResultMsg resultMsg;

	@XmlElement(name="ResultMsg")
	public ResultMsg getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(ResultMsg resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	

}
