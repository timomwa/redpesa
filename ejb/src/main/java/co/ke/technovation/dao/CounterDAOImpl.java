package co.ke.technovation.dao;


import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.Counter;

public class CounterDAOImpl extends GenericDAOImpl<Counter, Long>  implements CounterDAOI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean doesCounterExist(String counter_name) {
		
		try{
			
			Query query = em.createQuery("from Counter WHERE name = :counter_name");
			query.setParameter("counter_name", counter_name);
			Counter counter = (Counter) query.getSingleResult();
			
			if(counter!=null)
				return true;
			
		}catch(NoResultException nre){
			logger.error(" Couldn't find counter by the name \""+counter_name+"\"");
		}catch(Exception nre){
			logger.error(nre.getMessage(), nre);
		}
		
		return false;
	}
	
	@Override
	public Counter findCounter(String counter_name){
		
		Counter counter = null;
		
		try{
			
			Query query = em.createQuery("from Counter WHERE name = :counter_name");
			query.setParameter("counter_name", counter_name);
			counter = (Counter) query.getSingleResult();
			
		}catch(NoResultException nre){
			logger.error(" Couldn't find counter by the name \""+counter_name+"\"");
		}catch(Exception nre){
			logger.error(nre.getMessage(), nre);
		}
		
		return counter;
	}
	
	
	@Override
	public void incrementCounter(String counter_name){

		try{
			
			Query query = em.createQuery("UPDATE Counter SET  counts = (counts + 1), timeStamp = current_date WHERE name = :counter_name");
			query.setParameter("counter_name", counter_name);
			query.executeUpdate();
			
		}catch(NoResultException nre){
			logger.error(" Couldn't find counter by the name \""+counter_name+"\"");
		}catch(Exception nre){
			logger.error(nre.getMessage(), nre);
		}
		
	}

}
