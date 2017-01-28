package co.ke.technovation.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.SMSConfigDAOI;
import co.ke.technovation.entity.SMSConfig;


@Stateless
@Remote
public class SMSConfigEJBImpl implements SMSConfigEJBI {
	
	private Logger logger = Logger.getLogger(getClass());

	@Inject
	private SMSConfigDAOI smsconfigDAO;
	
	@Override
	public SMSConfig save(SMSConfig config) throws Exception{
		return smsconfigDAO.save(config);
	}
	
	
	@Override
	public SMSConfig getFirstConfig(){
		return smsconfigDAO.getFirst();
	}

}