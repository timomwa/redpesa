package co.ke.technovation.dao;

import java.util.List;

import co.ke.technovation.entity.RedCrossWinner;

public interface RedCrossWinnerDAOI extends GenericDAOI<RedCrossWinner, Long> {

	public List<RedCrossWinner> getUnpaidWinners(int limit);
	
	public RedCrossWinner findBy(String fieldName, Object value);

}
