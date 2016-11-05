package co.ke.technovation.ejb;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.RedCrossPaymentsDAOI;
import co.ke.technovation.entity.RedCrossPayment;

@Stateless
public class RedCrossPaymentsEJBImpl implements RedCrossPaymentsEJBI {
	
	@Inject
	private RedCrossPaymentsDAOI paymentdDAO;
	
	public Logger logger = Logger.getLogger(getClass());
	
	public void savePayment(RedCrossPayment payment) throws Exception{
		paymentdDAO.save(payment);
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
			paymentdDAO.save(payment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
