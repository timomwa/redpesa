package co.ke.technovation.dao;

import co.ke.technovation.entity.SMSConfig;

public interface SMSConfigDAOI extends GenericDAOI<SMSConfig, Long> {

	public SMSConfig getFirst(); 

}
