package co.ke.technovation.ejb;

import co.ke.technovation.entity.MpesaIn;

public interface MpesaInEJBI {

	public MpesaIn saveMpesaInFlow(MpesaIn mpesaIn) throws Exception;
	
	public MpesaIn findByTransactionId(String transId) throws Exception;

}
