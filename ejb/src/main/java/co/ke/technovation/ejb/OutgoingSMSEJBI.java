package co.ke.technovation.ejb;

import co.ke.technovation.entity.SMS;
import ke.co.technovation.httputils.GenericHttpResp;

public interface OutgoingSMSEJBI {
	
	public GenericHttpResp sendSMS(SMS sms);

}
