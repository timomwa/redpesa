package co.ke.technovation.ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaInDAOI;
import co.ke.technovation.entity.MpesaIn;

@Stateless
public class MpesaInEJBImpl implements MpesaInEJBI {
	
	@Inject
	private MpesaInDAOI mpesaInDao;
	
	public Logger logger = Logger.getLogger(getClass());
	
	@Override
	public MpesaIn saveMpesaInFlow(MpesaIn mpesaIn) throws Exception{
		
		MpesaIn originalRec = findByTransactionId(mpesaIn.getTransId());
		
		if(originalRec==null)
			return mpesaInDao.save(mpesaIn);
		
		originalRec.updateFromInstance(mpesaIn);
		
		return mpesaInDao.save(originalRec);
	}
	
	
	@Override
	public MpesaIn findByTransactionId(String transId) throws Exception{
		return mpesaInDao.findBy("transId", transId);
	}

}
