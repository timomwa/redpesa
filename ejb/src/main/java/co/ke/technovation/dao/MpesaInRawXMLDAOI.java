package co.ke.technovation.dao;

import java.util.List;

import co.ke.technovation.entity.MpesaInRawXML;

public interface MpesaInRawXMLDAOI   extends GenericDAOI<MpesaInRawXML, Long> {

	public List<MpesaInRawXML> listUnprocessed(int limit);

}
