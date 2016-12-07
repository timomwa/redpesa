package co.ke.technovation.ejb;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.RedCrossWinnerDAOI;
import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.PaymentStatus;
import co.ke.technovation.entity.RedCrossWinner;

@Stateless
@Remote(RedCrossWinnerEJBI.class)
public class RedCrossWinnerEJBImpl implements RedCrossWinnerEJBI {
	
	private static final String INSUF_BAL = "The balance is insufficient for the transaction.";

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
	
	@Override
	public boolean updatePaymentStatus(MpesaOut mpesaOut){
		try{
			
			RedCrossWinner winnerRec = redcrossWinnerDAO.findBy("ticket_number", mpesaOut.getConversationID());
			if(winnerRec!=null){
				
				if(mpesaOut.getResultCode().compareTo(0)==0){
					winnerRec.setPayment_status(PaymentStatus.PROCESSED_SUCCESSFULLY.getCode());
				}else if( (mpesaOut.getResultCode().compareTo(1)==0) & mpesaOut.getResultDesc().equalsIgnoreCase(INSUF_BAL)){  
					winnerRec.setPayment_status(PaymentStatus.INSUFFICIENT_BALANCE.getCode());
				}else{
					winnerRec.setPayment_status( PaymentStatus.PROCESSED_IN_ERROR.getCode() ) ;
				}
				
				winnerRec = redcrossWinnerDAO.save(winnerRec);
				
			}else{
				logger.warn(" Could not find winner record with Ticket number: "+mpesaOut.getConversationID());
			}
						
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return false;
	}
}
