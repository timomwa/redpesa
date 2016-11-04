package co.ke.technovation.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="mpesa_raw",  indexes = {
		@Index(columnList="msisdn", name="accodeidx")
})
public class MpesaRaw extends AbstractEntity {
	
	@Column(name="raw_confirmation_xml", length=6000)
	private String raw_confirmation_xml;
	
	@Column(name="msisdn", length=20)
	private String msisdn;
	
	@Column(name="transId", length=20, unique=true)
	private String transId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timeStamp")
	private Date timeStamp;
	
	@Enumerated(EnumType.STRING)
	@Column(name="callType", nullable=false)
	private CallType callType;
	
	@Column(name="transAmount", scale=5, precision=2)
	private BigDecimal transAmount;
	
	@PreUpdate
	@PrePersist
	public void init(){
		if(timeStamp==null)
			timeStamp = new Date();
	}

	public String getRaw_confirmation_xml() {
		return raw_confirmation_xml;
	}

	public void setRaw_confirmation_xml(String raw_confirmation_xml) {
		this.raw_confirmation_xml = raw_confirmation_xml;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public CallType getCallType() {
		return callType;
	}

	public void setCallType(CallType callType) {
		this.callType = callType;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	
	
	
}
