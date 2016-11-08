package co.ke.technovation.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.MpesaInRawXML;
import co.ke.technovation.entity.ProcessingStatus;

import java.util.List;

@Stateless
public class ReprocessorEJBImpl implements ReprocessorEJBI {
	
	public Logger logger = Logger.getLogger(getClass());
	
	@EJB
	private MpesaRawEJBI mpesaRawEJB;
	
	@EJB
	private RedCrossPaymentsEJBI redcrossPaymentsEJB;
	
	@EJB
	private MpesaInEJBI mpesaInEJB;
	
	@EJB
	private CounterEJBI counterEJB;
	
	@EJB
	private MsisdnListEJBI msisdnListEJB;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	private final static String MPESA_INS_COUNTER = "MPESA_INFLOW";
	
	public int process(String ip_addr){
		
		int processed_total = 0;
		
		List<MpesaInRawXML> mpesaRawlist = mpesaRawEJB.listUnprocessed(100);
		
		for(MpesaInRawXML rawXML : mpesaRawlist){
			
			try{
				
				String xmlstr = rawXML.getRaw_confirmation_xml();
				String transID = xmlUtils.getValue(xmlstr, "TransID");
				MpesaIn mpesaIn = mpesaInEJB.findByTransactionId(transID);
				
				if(mpesaIn==null){
					mpesaIn = xmlUtils.parseXML(xmlstr, CallType.CONFIRMATION);
				}
				
				mpesaIn.setRaw_xml_id( rawXML.getId() );
				mpesaIn.setSourceip( ip_addr );
				
				redcrossPaymentsEJB.savePayment( mpesaIn );
				mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
				
				rawXML.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				mpesaIn.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
				
				rawXML = mpesaRawEJB.save(rawXML);
				mpesaIn = mpesaInEJB.saveMpesaInFlow(mpesaIn);
				
				counterEJB.incrementCounter(MPESA_INS_COUNTER);
				
				msisdnListEJB.incrementCounter( mpesaIn.getMsisdn() );
				
				processed_total++;
				
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			
		}
		
		return processed_total;
	}

}
