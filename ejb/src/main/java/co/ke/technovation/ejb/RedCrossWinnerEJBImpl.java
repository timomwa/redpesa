package co.ke.technovation.ejb;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.RedCrossWinnerDAOI;
import co.ke.technovation.entity.RedCrossWinner;

@Stateless
@Remote(RedCrossWinnerEJBI.class)
public class RedCrossWinnerEJBImpl implements RedCrossWinnerEJBI {
	
	@Inject
	private RedCrossWinnerDAOI redcrossWinnerDAO;
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public RedCrossWinner save(RedCrossWinner winner) throws Exception {
		try{
			return  redcrossWinnerDAO.save(winner);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	@Override
	public List<RedCrossWinner> getWinners(int limit){
		return redcrossWinnerDAO.getUnpaidWinners(limit);
	}
}
