package co.ke.technovation.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import co.ke.technovation.dao.MpesaOutRawXMLDAOI;
import co.ke.technovation.entity.MpesaOutRawXML;
import co.ke.technovation.entity.ProcessingStatus;

@Stateless
public class MpesaOutRawXMLEJBImpl implements MpesaOutRawXMLEJBI {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private MpesaOutRawXMLDAOI mpesaOutRawDAO;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	public MpesaOutRawXML save(MpesaOutRawXML mpesaOutRawXML) throws Exception{
		return mpesaOutRawDAO.save(mpesaOutRawXML);
	}
	
	@Override
	public MpesaOutRawXML logRequest(String xml) throws Exception{
		
		try{
			String transId = xmlUtils.getValue(xml, "TransactionID");
			MpesaOutRawXML mpesaOutRawXML = new MpesaOutRawXML();
			mpesaOutRawXML.setRaw_xml(xml);
			mpesaOutRawXML.setStatus(ProcessingStatus.JUST_IN.getCode());
			mpesaOutRawXML.setTransId( transId );
			return save(mpesaOutRawXML);
			
		}catch( Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		
	}

}
