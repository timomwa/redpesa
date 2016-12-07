package co.ke.technovation.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.MpesaInRawXML;
import co.ke.technovation.entity.MpesaOut;
import co.ke.technovation.entity.MpesaOutRawXML;
import co.ke.technovation.entity.ProcessingStatus;

import java.util.List;

@Stateless
public class ReprocessorEJBImpl implements ReprocessorEJBI {
	
	public Logger logger = Logger.getLogger(getClass());
	
	@EJB
	private MpesaRawEJBI mpesaRawEJB;
	
	@EJB
	private MpesaOutRawXMLEJBI mpesaOutRawEJB;
	
	@EJB
	private RedCrossPaymentsEJBI redcrossPaymentsEJB;
	
	@EJB
	private MpesaInEJBI mpesaInEJB;
	
	@EJB
	private MpesaOutEJBI mpesaOutEJB;
	
	
	@EJB
	private CounterEJBI counterEJB;
	
	@EJB
	private MsisdnListEJBI msisdnListEJB;
	
	@EJB
	private XMLUtilsI xmlUtils;
	
	@EJB
	private RedCrossWinnerEJBI redcrossWinnerEJB;
	
	private final static String MPESA_INS_COUNTER = "MPESA_INFLOW";
	
	@Override
	public int process(String ip_addr, int limit){
		int processed  = 0;
		processed = processC2B(ip_addr, limit);
		processed = processB2C(ip_addr, limit) + processed;
		return processed;
	}
	
	
	@Override
	public int processC2B(String ip_addr, int limit){
		
		int processed_total = 0;
		
		List<MpesaInRawXML> mpesaRawlist = mpesaRawEJB.listUnprocessed(limit);
		
		if(mpesaRawlist!=null)
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
					
					boolean paymentexit = redcrossPaymentsEJB.redcrossPaymentExists( mpesaIn.getTransId() );
					
					if(!paymentexit)
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
	
	
	@Override
	public int processB2C(String ip_addr, int limit){
		
		int processed_total = 0;
		
		List<MpesaOutRawXML> mpesaRawlist =  mpesaOutRawEJB.listUnprocessed(limit);
		
		if(mpesaRawlist!=null)
			for(MpesaOutRawXML rawOutXML : mpesaRawlist){
				try{
					String xmlstr = rawOutXML.getRaw_xml();
					String transID = xmlUtils.getValue(xmlstr, "TransactionID");
					MpesaOut mpesaOut = mpesaOutEJB.findByTransactionId(transID);
					
					if(mpesaOut==null){
						mpesaOut = xmlUtils.parseB2CQueryTimeout(xmlstr, ip_addr);
					}
					
					mpesaOut.setSourceip(ip_addr);
					mpesaOut.setRaw_xml_id(rawOutXML.getId());
					
					boolean success = redcrossWinnerEJB.updatePaymentStatus(mpesaOut);
					
					if(success){
						
						rawOutXML.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
						mpesaOut.setStatus(ProcessingStatus.PROCESSED_SUCCESSFULLY.getCode());
						
						rawOutXML = mpesaOutRawEJB.save(rawOutXML);
						mpesaOut = mpesaOutEJB.save(mpesaOut);
						processed_total++;
					}
					
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
				
			}
		
		return processed_total;	
		
	}

}
