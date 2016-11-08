package co.ke.technovation.ejb;

import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaInRawXML;
import java.util.List;

public interface MpesaRawEJBI {

	public MpesaInRawXML logRequest(String xml, CallType callType)  throws Exception;

	public MpesaInRawXML save(MpesaInRawXML mpesa_raw_xml) throws Exception;

	public List<MpesaInRawXML> listUnprocessed(int limit);

}
