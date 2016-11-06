package co.ke.technovation.ejb;

import java.math.BigInteger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MsisdnListDAOI;
import co.ke.technovation.entity.MsisdnList;

@Stateless
public class MsisdnListEJBImpl implements MsisdnListEJBI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private MsisdnListDAOI msisdnListDAO;
	
	@Override
	public void incrementCounter(String msisdn){
		
		try{
			
			boolean success = msisdnListDAO.incrementCounter(msisdn);
			if(!success){
				MsisdnList msisdnList = new MsisdnList();
				msisdnList.setMsisdn(msisdn);
				msisdnList.setPlaycount(BigInteger.ONE);
				msisdnListDAO.save(msisdnList);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
	}

}
