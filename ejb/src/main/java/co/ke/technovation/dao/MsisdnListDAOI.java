package co.ke.technovation.dao;

import co.ke.technovation.entity.MsisdnList;

public interface MsisdnListDAOI extends GenericDAOI<MsisdnList, Long> {

	public MsisdnList findMsisdn(String msisdn);

	public boolean incrementCounter(String msisdn);

}
