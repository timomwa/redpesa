package co.ke.technovation.dao;

import co.ke.technovation.entity.RedCrossPayment;

public interface RedCrossPaymentsDAOI extends GenericDAOI<RedCrossPayment, Long> {

	public RedCrossPayment getPaymentByTransId(String transId);
	
	@Override
	public RedCrossPayment save(RedCrossPayment entity) throws Exception;
		

}
