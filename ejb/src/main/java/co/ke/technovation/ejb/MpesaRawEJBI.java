package co.ke.technovation.ejb;

import co.ke.technovation.entity.CallType;

public interface MpesaRawEJBI {

	public void logRequest(String xml, CallType callType)  throws Exception;

}
