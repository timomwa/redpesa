package co.ke.technovation.dao;

import java.util.List;

import co.ke.technovation.entity.MpesaOutRawXML;

public interface MpesaOutRawXMLDAOI extends GenericDAOI<MpesaOutRawXML, Long> {

	public List<MpesaOutRawXML> listUnprocessed(int limit);
}
