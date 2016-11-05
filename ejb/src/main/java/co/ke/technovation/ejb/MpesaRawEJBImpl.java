package co.ke.technovation.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaRawDAOI;
import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaRaw;

@Stateless
public class MpesaRawEJBImpl implements MpesaRawEJBI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@Inject
	private MpesaRawDAOI mpesarawDAO;
	
	public void logRequest(String xml, CallType callType) throws Exception{
		
		try{
			
			MpesaRaw rawlog = new MpesaRaw();
			rawlog.setRaw_confirmation_xml(xml);
			rawlog.setMsisdn( xmlUtils.getValue(xml, "MSISDN") );
			rawlog.setTransId(  xmlUtils.getValue(xml, "TransID")  );
			rawlog.setCallType(callType);
			rawlog.setTransAmount( xmlUtils.toBigDecimal( xmlUtils.getValue(xml, "TransAmount") )  );
			mpesarawDAO.save(rawlog);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

}
