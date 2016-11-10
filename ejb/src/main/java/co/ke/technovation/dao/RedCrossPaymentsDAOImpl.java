package co.ke.technovation.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.constants.AppPropertyHolder;
import co.ke.technovation.entity.RedCrossPayment;

public class RedCrossPaymentsDAOImpl extends GenericDAOImpl<RedCrossPayment, Long> implements RedCrossPaymentsDAOI {

	private Logger logger = Logger.getLogger(getClass());
	
	@PersistenceContext(unitName=AppPropertyHolder.REDCROSS_PERSISTENCE_UNIT_NAME)
	protected EntityManager redcrossem;

	@Override
	public RedCrossPayment save(RedCrossPayment entity) throws Exception {
		return redcrossem.merge(entity);
	}
	
	@Override
	public RedCrossPayment getPaymentByTransId(String transId){
		RedCrossPayment rcp = null;
		try{
			Query qry = redcrossem.createQuery("from RedCrossPayment WHERE telco_transaction_id = :transid");
			qry.setParameter("transid", transId);
			rcp = (RedCrossPayment) qry.getSingleResult();
		}catch(NoResultException nre){
			logger.warn(" No RedCrossPayment with transId = "+transId);
		}
		return rcp;
	}
	
}
