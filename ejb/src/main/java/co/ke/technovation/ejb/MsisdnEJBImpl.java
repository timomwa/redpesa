package co.ke.technovation.ejb;


import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MsisdnDAOI;
import co.ke.technovation.entity.Msisdn;

@Stateless
public class MsisdnEJBImpl implements MsisdnEJBI {
	
	@Inject
	private MsisdnDAOI msisdnDAO;
	
	private Logger logger = Logger.getLogger(getClass());
	
	public void testWrite(String string){
		//org.dom4j.DocumentFactory d;
		logger.info("\n\t msisdn ->"+string+"\n");
		
		Msisdn msisdn = new Msisdn();
		msisdn.setMsisdn(string);
		try {
			msisdnDAO.save(msisdn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
