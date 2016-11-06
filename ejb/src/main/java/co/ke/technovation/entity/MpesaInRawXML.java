package co.ke.technovation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name="mpesa_in_xml",  indexes = {
		@Index(columnList="transId", name="transIdIdx")
}, schema=AppPropertyHolder.CMP_SCHEMA_NAME)
public class MpesaInRawXML extends AbstractEntity {
	
	@Column(name="raw_confirmation_xml", length=6000, nullable=false)
	private String raw_confirmation_xml;
	
	@Column(name="transId", length=20, nullable=false)
	private String transId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;
	
	@Column(name="status", nullable=false)
	private Integer status;

	public String getRaw_confirmation_xml() {
		return raw_confirmation_xml;
	}

	public void setRaw_confirmation_xml(String raw_confirmation_xml) {
		this.raw_confirmation_xml = raw_confirmation_xml;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
	
	

	@PreUpdate
	@PrePersist
	public void init(){
		if(timeStamp==null)
			timeStamp = new Date();
		if(status==null)
			status = ProcessingStatus.JUST_IN.getCode();
	}

}
