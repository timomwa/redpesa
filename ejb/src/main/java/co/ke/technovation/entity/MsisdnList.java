package co.ke.technovation.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name="mpesa_in_msisdn",  indexes = {
		@Index(columnList="msisdn", name="msisdncountIdx")
}, schema=AppPropertyHolder.CMP_SCHEMA_NAME)
public class MsisdnList extends AbstractEntity {
	
	@Column(name="msisdn", length=15, unique=true, nullable=false)
	private String msisdn;
	
	@Column(name="playcount", nullable=false)
	private BigInteger playcount;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public BigInteger getPlaycount() {
		return playcount;
	}

	public void setPlaycount(BigInteger playcount) {
		this.playcount = playcount;
	}

	

}
