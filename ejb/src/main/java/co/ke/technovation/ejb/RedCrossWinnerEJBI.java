package co.ke.technovation.ejb;

import java.util.List;

import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.RedCrossWinner;

public interface RedCrossWinnerEJBI {
	
	public RedCrossWinner save(RedCrossWinner winner) throws Exception ;
	
	public List<RedCrossWinner> getWinners(int limit);
	
	public boolean updatePaymentStatus(MpesaOut mpesaOut);
	
	public boolean winnerSuccessfullyPaid(RedCrossWinner winner);

}
