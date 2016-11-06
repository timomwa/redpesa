package co.ke.technovation.ejb;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.CounterDAOI;
import co.ke.technovation.entity.Counter;

@Stateless
public class CounterEJBImpl implements CounterEJBI {
	
	public Logger logger = Logger.getLogger(getClass());
	private Boolean check_if_counter_exists_ = Boolean.TRUE;
	
	@Inject
	public CounterDAOI counterDAOI;
	
	public void incrementCounter(String counter_name){
		
		Counter counter = null;
		
		try{
			
			if(check_if_counter_exists_){
				counter = counterDAOI.findCounter(counter_name);
				if(counter==null){
					counter = createCounter(counter_name);
				}else{
					check_if_counter_exists_ = Boolean.FALSE;
				}
			}
			
			counterDAOI.incrementCounter(counter_name);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}

	
	private Counter createCounter(String counter_name) throws Exception {
		Counter counter = new Counter();
		counter.setName(counter_name);
		return counterDAOI.save(counter);
	}

	
	
}
