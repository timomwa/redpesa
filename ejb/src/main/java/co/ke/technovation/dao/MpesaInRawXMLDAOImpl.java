package co.ke.technovation.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.MpesaInRawXML;
import co.ke.technovation.entity.ProcessingStatus;

public class MpesaInRawXMLDAOImpl extends GenericDAOImpl<MpesaInRawXML, Long> implements MpesaInRawXMLDAOI {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MpesaInRawXML> listUnprocessed(int limit) {
		
		List<MpesaInRawXML> mpesaRawXML = new ArrayList<MpesaInRawXML>();
    	
		try{
			
			Query qry = em.createQuery("from MpesaInRawXML WHERE status <> :status");
			qry.setParameter("status", ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
			qry.setMaxResults(limit);
			mpesaRawXML = qry.getResultList();
			
		}catch(NoResultException nre){
			logger.warn(" Could not find any unprocessed raw xml files");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
    	
		return mpesaRawXML;
	}

}
