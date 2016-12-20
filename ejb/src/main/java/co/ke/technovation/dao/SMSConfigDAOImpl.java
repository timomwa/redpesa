package co.ke.technovation.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.constants.AppPropertyHolder;
import co.ke.technovation.entity.SMSConfig;


public class SMSConfigDAOImpl extends GenericDAOImpl<SMSConfig, Long>  implements SMSConfigDAOI {

	@PersistenceContext(unitName=AppPropertyHolder.REDCROSS_REPORTING_PERSISTENCE_UNIT_NAME)
	protected EntityManager cmp_reporting_em;
	
	private Logger logger = Logger.getLogger(getClass());
	
	public SMSConfig getFirst(){
		
		SMSConfig config = null;
		
		try{
			Query qry = cmp_reporting_em.createQuery("from SMSConfig ORDER BY id desc");
			qry.setFirstResult(0);
			qry.setMaxResults(1);
			config = (SMSConfig) qry.getSingleResult();
		}catch(javax.persistence.NoResultException nre){
			logger.warn(" Could not find any sms config.");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return config;
	}
}
