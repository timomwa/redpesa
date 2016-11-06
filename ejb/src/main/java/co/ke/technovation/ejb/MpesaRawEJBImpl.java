package co.ke.technovation.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaInRawXMLDAOI;
import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaInRawXML;
import co.ke.technovation.entity.ProcessingStatus;

@Stateless
public class MpesaRawEJBImpl implements MpesaRawEJBI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@Inject
	private MpesaInRawXMLDAOI mpesaInXMLDao;
	
	
	@Override
	public MpesaInRawXML save(MpesaInRawXML rawlog) throws Exception{
		return mpesaInXMLDao.save(rawlog);
	}
	
	@Override
	public MpesaInRawXML logRequest(String xml, CallType callType) throws Exception{
		
		MpesaInRawXML rawlog = new MpesaInRawXML();
		
		try{
			
			rawlog.setRaw_confirmation_xml(xml);
			rawlog.setStatus(ProcessingStatus.JUST_IN.getCode());
			rawlog.setTransId(  xmlUtils.getValue(xml, "TransID")  );
			rawlog = mpesaInXMLDao.save(rawlog);
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		return rawlog;
	}

}
