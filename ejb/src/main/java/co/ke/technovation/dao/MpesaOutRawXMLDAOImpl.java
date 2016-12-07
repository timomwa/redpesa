package co.ke.technovation.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.MpesaOutRawXML;
import co.ke.technovation.entity.ProcessingStatus;

public class MpesaOutRawXMLDAOImpl extends GenericDAOImpl<MpesaOutRawXML, Long> implements MpesaOutRawXMLDAOI {

	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MpesaOutRawXML> listUnprocessed(int limit) {
		
		List<MpesaOutRawXML> mpesaOutRawXML = new ArrayList<MpesaOutRawXML>();
    	
		try{
			
			Query qry = em.createQuery("from MpesaOutRawXML WHERE status <> :status");
			qry.setParameter("status", ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
			qry.setMaxResults(limit);
			mpesaOutRawXML = qry.getResultList();
			
		}catch(NoResultException nre){
			logger.warn(" Could not find any unprocessed raw xml files");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
    	
		return mpesaOutRawXML;
	}
}
