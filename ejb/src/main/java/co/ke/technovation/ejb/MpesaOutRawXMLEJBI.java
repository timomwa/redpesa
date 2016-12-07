package co.ke.technovation.ejb;

import java.util.List;

import co.ke.technovation.entity.MpesaOutRawXML;

public interface MpesaOutRawXMLEJBI {

	public MpesaOutRawXML logRequest(String xml) throws Exception;

	public MpesaOutRawXML save(MpesaOutRawXML mpesaOutRawXML) throws Exception;

	public List<MpesaOutRawXML> listUnprocessed(int limit);

}
