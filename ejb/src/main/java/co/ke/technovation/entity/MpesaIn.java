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

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name="mpesa_in",  indexes = {
		@Index(columnList="msisdn", name="accodeidx"),
		@Index(columnList="billRefNumber", name="billrefNumberIdx"),
		@Index(columnList="businessShortcode", name="bizshortcodeIdx"),
		@Index(columnList="transId", name="transIdIdx", unique=true),
		@Index(columnList= "transId,timeStamp", name="UK_jmf0p32js61y0y9icngqkq6dr", unique=true)
}, schema=AppPropertyHolder.CMP_SCHEMA_NAME)
public class MpesaIn extends AbstractEntity {
	
	@Column(name="raw_xml_id", nullable=false)
	private Long raw_xml_id;
	
	@Column(name="msisdn", length=20, nullable=false)
	private String msisdn;
	
	@Column(name="transId", length=20, nullable=false)
	private String transId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;
	
	@Enumerated(EnumType.STRING)
	@Column(name="callType", nullable=false)
	private CallType callType;
	
	@Column(name="transType", nullable=false)
	private String transType;
	
	@Column(name="businessShortcode", nullable=false)
	private String businessShortcode;
	
	@Column(name="billRefNumber")
	private String billRefNumber;
	
	@Column(name="transAmount", scale=2, precision=5, nullable=false)
	private BigDecimal transAmount;
	
	@Column(name="orgAccountBalance", scale=2, precision=5, nullable=false)
	private BigDecimal orgAccountBalance;
	
	@Column(name="status", nullable=false)
	private Integer status;
	
	@Column(name="first_name", length=100, nullable=false)
	private String first_name;
	
	@Column(name="middle_name", length=100, nullable=false)
	private String middle_name;
	
	@Column(name="last_name", length=100, nullable=false)
	private String last_name;
	
	@Column(name="sourceip", length=45, nullable=false)
	private String sourceip;
	
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getRaw_xml_id() {
		return raw_xml_id;
	}

	public void setRaw_xml_id(Long raw_xml_id) {
		this.raw_xml_id = raw_xml_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBusinessShortcode() {
		return businessShortcode;
	}

	public void setBusinessShortcode(String businessShortcode) {
		this.businessShortcode = businessShortcode;
	}

	public String getBillRefNumber() {
		return billRefNumber;
	}

	public void setBillRefNumber(String billRefNumber) {
		this.billRefNumber = billRefNumber;
	}

	public BigDecimal getOrgAccountBalance() {
		return orgAccountBalance;
	}

	public void setOrgAccountBalance(BigDecimal orgAccountBalance) {
		this.orgAccountBalance = orgAccountBalance;
	}

	public String getSourceip() {
		return sourceip;
	}

	public void setSourceip(String sourceip) {
		this.sourceip = sourceip;
	}

	@PreUpdate
	@PrePersist
	public void init(){
		if(timeStamp==null)
			timeStamp = new Date();
		if(status==null)
			status = ProcessingStatus.JUST_IN.getCode();
		if(first_name==null)
			first_name = "";
		if(middle_name==null)
			middle_name = "";
		if(last_name==null)
			last_name = "";
		if(orgAccountBalance==null)
			orgAccountBalance = BigDecimal.ZERO;
		
		if(sourceip==null)
			sourceip = AppPropertyHolder.DEF_SOURCE_IP;
	}
}
