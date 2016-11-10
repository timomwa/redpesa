package co.ke.technovation.ejb;

import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.RedCrossPayment;

public interface RedCrossPaymentsEJBI {

	public void mimicPayment();

	public RedCrossPayment savePayment(RedCrossPayment payment)  throws Exception;

	public RedCrossPayment savePayment(MpesaIn mpesaIn) throws Exception;

	public boolean redcrossPaymentExists(String transId);

}
