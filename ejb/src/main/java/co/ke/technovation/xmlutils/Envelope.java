package co.ke.technovation.xmlutils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Envelope")
public class Envelope {
	
	protected Body body;

	@XmlElement(name="Body")
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	

}
