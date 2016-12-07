package co.ke.technovation.ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaOutDAOI;
import co.ke.technovation.entity.MpesaOut;

@Stateless
public class MpesaOutEJBImpl implements MpesaOutEJBI {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private MpesaOutDAOI mpesaOutDAO;
	
	@Override
	public MpesaOut save(MpesaOut mpesaOut) throws Exception{
		return mpesaOutDAO.save(mpesaOut);
	}
	
	@Override
	public MpesaOut findByTransactionId(String transID){
		return mpesaOutDAO.findBy("transId", transID);
	}

}
