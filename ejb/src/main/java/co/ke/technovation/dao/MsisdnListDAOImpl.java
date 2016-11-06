package co.ke.technovation.dao;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.MsisdnList;

public class MsisdnListDAOImpl  extends GenericDAOImpl<MsisdnList, Long>  implements MsisdnListDAOI {

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean incrementCounter(String msisdn){
		
		boolean success = false;
		
		try{
			
			Query qry = em.createQuery("UPDATE MsisdnList SET playcount = (playcount + 1) WHERE msisdn = :msisdn");
			qry.setParameter("msisdn", msisdn);
			int updated = qry.executeUpdate();
			success = ( updated>0 );
			
		}catch(javax.persistence.NoResultException nre){
			logger.error("Could not find msisdnlist record with the msisdn = \""+msisdn+"\"");
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return success;
	}
	
	
	
	@Override
	public MsisdnList findMsisdn(String msisdn){
		
		MsisdnList msisdnlist = null;
		
		try{
			
			Query qry = em.createQuery("from MsisdnList WHERE msisdn = :msisdn");
			qry.setParameter("msisdn", msisdn);
			msisdnlist = (MsisdnList) qry.getSingleResult();
			
		}catch(javax.persistence.NoResultException nre){
			logger.error("Could not find msisdnlist record with the msisdn = \""+msisdn+"\"");
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return msisdnlist;
	}
}
