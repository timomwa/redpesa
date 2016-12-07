package co.ke.technovation.ejb;

import co.ke.technovation.entity.MpesaOut;

public interface MpesaOutEJBI {

	public MpesaOut save(MpesaOut mpesaOut) throws Exception;

	public MpesaOut findByTransactionId(String transID);

}
