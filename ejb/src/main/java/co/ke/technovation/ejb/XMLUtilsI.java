package co.ke.technovation.ejb;

import java.math.BigDecimal;

import co.ke.technovation.entity.RedCrossPayment;
import co.ke.technovation.entity.MpesaIn;

public interface XMLUtilsI {
	
	public String getValue(String xml,String tagname);

	public BigDecimal toBigDecimal(String value);

	public RedCrossPayment populateValues(String xml, RedCrossPayment payment) throws Exception;
	
	public MpesaIn populateValues(String xml, MpesaIn mpesaIn) throws Exception;

}
