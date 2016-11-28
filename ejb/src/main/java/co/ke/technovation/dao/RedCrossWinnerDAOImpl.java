package co.ke.technovation.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.constants.AppPropertyHolder;
import co.ke.technovation.entity.PaymentStatus;
import co.ke.technovation.entity.RedCrossWinner;

public class RedCrossWinnerDAOImpl extends GenericDAOImpl<RedCrossWinner, Long> implements RedCrossWinnerDAOI {

	private Logger logger  = Logger.getLogger(getClass());
	
	@PersistenceContext(unitName=AppPropertyHolder.REDCROSS_PERSISTENCE_UNIT_NAME)
	protected EntityManager redcrossem;
	
	@Override
	public RedCrossWinner findBy(String fieldName, Object value) {
		
		RedCrossWinner winner = null;
		
		try{
			Query qry = redcrossem.createQuery("from RedCrossWinner WHERE "+fieldName+" = :"+fieldName);
			qry.setParameter("fieldName", value);
			qry.setMaxResults(1);
			winner = (RedCrossWinner) qry.getSingleResult();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return winner;
	}
	
	@Override
	public RedCrossWinner save(RedCrossWinner entity) throws Exception {
		return redcrossem.merge(entity);
	}	
	
	@SuppressWarnings("unchecked")
	public List<RedCrossWinner> getUnpaidWinners(int limit){
		
		List<RedCrossWinner> winners = new ArrayList<RedCrossWinner>();
		
		try{
			Query qry = redcrossem.createQuery("from RedCrossWinner WHERE is_verified=:is_verified AND is_processed=:is_processed AND payment_status=:payment_status");
			qry.setParameter("is_verified", Boolean.TRUE);
			qry.setParameter("is_processed", Boolean.TRUE);
			qry.setParameter("payment_status", PaymentStatus.JUST_IN.getCode());
			qry.setMaxResults(limit);
			winners = qry.getResultList();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return winners;
	}
}
