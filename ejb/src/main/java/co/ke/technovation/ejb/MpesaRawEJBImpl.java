package co.ke.technovation.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	
	DateFormat source_format = new SimpleDateFormat("yyyyMMddHHmmss");
    DateFormat dest_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    @Override
    public List<MpesaInRawXML> listUnprocessed(int limit){
    	return mpesaInXMLDao.listUnprocessed(limit);
    }
    
    
	@Override
	public MpesaInRawXML logRequest(String xml, CallType callType) throws Exception{
		
		MpesaInRawXML rawlog = null;
		
		try{
			
			String transId = xmlUtils.getValue(xml, "TransID");
			
			if(transId!=null && !transId.trim().isEmpty()){
				
				String transTime = xmlUtils.getValue(xml, "TransTime");
				Date transactionTime = ( transTime!=null && !transTime.trim().isEmpty() ) ? source_format.parse(transTime) : null;
				
				rawlog = mpesaInXMLDao.findBy("transId", transId);
				
				if(rawlog==null){
					rawlog = new MpesaInRawXML();
					rawlog.setRaw_confirmation_xml(xml);
					rawlog.setStatus(ProcessingStatus.JUST_IN.getCode());
					rawlog.setTransId(  transId  );
					rawlog.setTransTime(transactionTime);
					rawlog = mpesaInXMLDao.save(rawlog);
				}
				
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		return rawlog;
	}

}
