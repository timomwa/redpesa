package co.ke.technovation.ejb;

import co.ke.technovation.entity.RedCrossPayment;

public interface RedCrossPaymentsEJBI {

	public void mimicPayment();

	public void savePayment(RedCrossPayment payment)  throws Exception;

}
