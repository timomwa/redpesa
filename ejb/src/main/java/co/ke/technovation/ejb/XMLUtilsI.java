package co.ke.technovation.ejb;

import java.math.BigDecimal;

import co.ke.technovation.entity.RedCrossPayment;

public interface XMLUtilsI {
	
	public String getValue(String xml,String tagname);

	public BigDecimal toBigDecimal(String value);

	public RedCrossPayment populateValues(String xml, RedCrossPayment payment) throws Exception;

}
