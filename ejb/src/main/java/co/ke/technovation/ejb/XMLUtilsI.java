package co.ke.technovation.ejb;

import java.math.BigDecimal;

public interface XMLUtilsI {
	
	public String getValue(String xml,String tagname);

	public BigDecimal toBigint(String value);

}
