package co.ke.technovation.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaOutDAOI;
import co.ke.technovation.dao.RedCrossWinnerDAOI;
import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.PaymentStatus;
import co.ke.technovation.entity.RedCrossWinner;
import co.ke.technovation.entity.SMS;

@Stateless
@Remote(RedCrossWinnerEJBI.class)
public class RedCrossWinnerEJBImpl implements RedCrossWinnerEJBI {
	
	private static final String INSUF_BAL = "The balance is insufficient for the transaction.";
	private static final String SMS_TEMPLATE = "Hongera! You have won KES: ${PRIZE} for Ticket No: ${TICKET_NUMBER} in the ${DRAW_NUMBER} Draw. Keep playing Shinda Washinde to win again!";

	@Inject
	private RedCrossWinnerDAOI redcrossWinnerDAO;
	
	@EJB
	private MpesaOutEJBI mpesaOutEJB;
	
	@EJB
	private OutgoingSMSEJBI outgoingSMSEJB;
	
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
					try{
						SMS sms = new SMS();
						sms.setMsisdn(winnerRec.getPhone_number());
						sms.setSms(  generateSMS(winnerRec)  );
						outgoingSMSEJB.sendSMS(sms);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
					}
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
	
	private String generateSMS(RedCrossWinner winnerRec) {
		String sms  = SMS_TEMPLATE;
		sms = sms.replaceAll("\\$\\{PRIZE\\}", String.valueOf( winnerRec.getPrice_won() ) );
		sms = sms.replaceAll("\\$\\{TICKET_NUMBER\\}", String.valueOf( winnerRec.getTicket_number() ) );
		sms = sms.replaceAll("\\$\\{DRAW_NUMBER\\}", String.valueOf( winnerRec.getDraw_number() ) );
		return sms;
	}


	@Override
	public boolean winnerSuccessfullyPaid(RedCrossWinner winner){
		MpesaOut payment = mpesaOutEJB.getPaymentFromTicketNumber(winner);//conversationID in real sense.
		if(payment==null){
			return false;
		}else{
			if(payment.getResultCode().compareTo(0)==0)
				return true;
			else
				return false;
		}
	}
	
	
}
