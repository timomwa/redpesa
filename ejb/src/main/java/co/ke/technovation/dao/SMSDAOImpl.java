package co.ke.technovation.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.ke.technovation.constants.AppPropertyHolder;
import co.ke.technovation.entity.SMS;

public class SMSDAOImpl extends GenericDAOImpl<SMS, Long>  implements SMSDAOI {
	
	@PersistenceContext(unitName=AppPropertyHolder.REDCROSS_REPORTING_PERSISTENCE_UNIT_NAME)
	protected EntityManager cmp_reporting_em;
	
	
	@Override
	public SMS save(SMS entity) throws Exception{
		return cmp_reporting_em.merge(entity);
	}
	

}
