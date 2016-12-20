package co.ke.technovation.ejb;

import co.ke.technovation.entity.SMSConfig;

public interface SMSConfigEJBI {
	
	public SMSConfig save(SMSConfig config) throws Exception;
	
	public SMSConfig getFirstConfig();

}
