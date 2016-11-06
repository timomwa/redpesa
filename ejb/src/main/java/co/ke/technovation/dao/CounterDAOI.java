package co.ke.technovation.dao;

import co.ke.technovation.entity.Counter;

public interface CounterDAOI  extends GenericDAOI<Counter, Long>{
	
	
	public boolean doesCounterExist(String counter_name) ;

	public Counter findCounter(String counter_name);

	public void incrementCounter(String counter_name);
	

}
