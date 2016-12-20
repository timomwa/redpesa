package co.ke.technovation.dao;

import co.ke.technovation.entity.SMS;

public interface SMSDAOI extends GenericDAOI<SMS, Long>  {
	
	public SMS save(SMS entity) throws Exception;

}
