package co.ke.technovation.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.ke.technovation.constants.AppPropertyHolder;
import co.ke.technovation.entity.RedCrossPayment;

public class RedCrossPaymentsDAOImpl extends GenericDAOImpl<RedCrossPayment, Long> implements RedCrossPaymentsDAOI {

	@PersistenceContext(unitName=AppPropertyHolder.REDCROSS_PERSISTENCE_UNIT_NAME)
	protected EntityManager redcrossem;

	@Override
	public RedCrossPayment save(RedCrossPayment entity) throws Exception {
		return redcrossem.merge(entity);
	}
	
}
