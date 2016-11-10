package co.ke.technovation.ejb;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.RedCrossPaymentsDAOI;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.RedCrossPayment;

@Stateless
public class RedCrossPaymentsEJBImpl implements RedCrossPaymentsEJBI {
	
	@Inject
	private RedCrossPaymentsDAOI redcrossPaymentDAO;
	
	public Logger logger = Logger.getLogger(getClass());
	
	@Override
	public RedCrossPayment savePayment(RedCrossPayment payment) throws Exception{
		return redcrossPaymentDAO.save(payment);
	}
	
	
	@Override
	public boolean redcrossPaymentExists(String transId){
		RedCrossPayment payment = redcrossPaymentDAO.getPaymentByTransId(transId);
		return payment!=null;
	}
	
	
	@Override
	public RedCrossPayment savePayment(MpesaIn mpesaIn) throws Exception{
		RedCrossPayment payment = new RedCrossPayment();
		payment.setAmount(  mpesaIn.getTransAmount() );
		payment.setIs_processed(Boolean.FALSE);
		payment.setPhone_number( mpesaIn.getMsisdn() );
		payment.setTelco_transaction_id( mpesaIn.getTransId() );
		payment.setFirst_name( mpesaIn.getFirst_name() );
		payment.setLast_name( mpesaIn.getLast_name() );
		payment.setAccount_number( mpesaIn.getBillRefNumber() );
		payment.setDate_paid( mpesaIn.getTransTime() );
		payment.setDate_received( mpesaIn.getTimeStamp() );
		payment.setTelco_name("Safaricom");
		return redcrossPaymentDAO.save(payment);
	}
	
	public void mimicPayment(){
		RedCrossPayment payment = new RedCrossPayment();
		payment.setAmount(BigDecimal.TEN);
		payment.setFirst_name("Test Name");
		payment.setIs_processed(Boolean.FALSE);
		payment.setLast_name("Last Name");
		payment.setPhone_number("0720988636");
		payment.setTelco_transaction_id("KMXSERE");
		try {
			redcrossPaymentDAO.save(payment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
