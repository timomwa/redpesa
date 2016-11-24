package co.ke.technovation.entity;

import java.math.BigDecimal;
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
@Table(name = "mpesa_out", indexes = {
		@Index(columnList = "transId", name = "msaOuttransIdIdx", unique = true),
		@Index(columnList = "transactionReceipt", name = "msaOuttransRcptIdx", unique = true)
	}, 
schema = AppPropertyHolder.CMP_SCHEMA_NAME)
public class MpesaOut extends AbstractEntity {

	private static final long serialVersionUID = -8590242104546595829L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;
	
	@Column(name="raw_xml_id", nullable=false)
	private Long raw_xml_id;

	@Column(name="resultType")
	private Integer resultType;
	
	@Column(name="resultCode")
	private Integer resultCode;
	
	@Column(name="resultDesc", length=300, nullable=false)
	private String resultDesc;
	
	@Column(name="originatorConversationID", length=100, nullable=false)
	private String originatorConversationID;
	
	@Column(name="conversationID", length=100, nullable=false)
	private String conversationID;
	
	@Column(name="transId", length=20, nullable=false)
	private String transId;
	
	@Column(name="transactionReceipt", length=20, nullable=false)
	private String transactionReceipt;
	
	@Column(name = "transactionAmount", precision = 10, scale = 2, nullable = false)
	private BigDecimal transactionAmount;
	
	@Column(name = "b2CChargesPaidAccountAvailableFunds", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CChargesPaidAccountAvailableFunds;
	
	@Column(name="b2CRecipientIsRegisteredCustomer", nullable=false)
	private Boolean b2CRecipientIsRegisteredCustomer;
	
	@Column(name="transactionCompletedDateTime", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionCompletedDateTime;
	
	@Column(name="receiverPartyPublicName", length=200, nullable=false)
	private String receiverPartyPublicName;
	
	@Column(name = "b2CWorkingAccountAvailableFunds", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CWorkingAccountAvailableFunds;
	
	@Column(name = "b2CUtilityAccountAvailableFunds", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CUtilityAccountAvailableFunds;
	
	@Column(name="sourceip", length=45, nullable=false)
	private String sourceip;
	
	@Column(name="status", nullable=false)
	private Integer status;

	public Long getRaw_xml_id() {
		return raw_xml_id;
	}

	public void setRaw_xml_id(Long raw_xml_id) {
		this.raw_xml_id = raw_xml_id;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getOriginatorConversationID() {
		return originatorConversationID;
	}

	public void setOriginatorConversationID(String originatorConversationID) {
		this.originatorConversationID = originatorConversationID;
	}

	public String getConversationID() {
		return conversationID;
	}

	public void setConversationID(String conversationID) {
		this.conversationID = conversationID;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransactionReceipt() {
		return transactionReceipt;
	}

	public void setTransactionReceipt(String transactionReceipt) {
		this.transactionReceipt = transactionReceipt;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getB2CChargesPaidAccountAvailableFunds() {
		return b2CChargesPaidAccountAvailableFunds;
	}

	public void setB2CChargesPaidAccountAvailableFunds(BigDecimal b2cChargesPaidAccountAvailableFunds) {
		b2CChargesPaidAccountAvailableFunds = b2cChargesPaidAccountAvailableFunds;
	}

	public Boolean getB2CRecipientIsRegisteredCustomer() {
		return b2CRecipientIsRegisteredCustomer;
	}

	public void setB2CRecipientIsRegisteredCustomer(Boolean b2cRecipientIsRegisteredCustomer) {
		b2CRecipientIsRegisteredCustomer = b2cRecipientIsRegisteredCustomer;
	}

	public Date getTransactionCompletedDateTime() {
		return transactionCompletedDateTime;
	}

	public void setTransactionCompletedDateTime(Date transactionCompletedDateTime) {
		this.transactionCompletedDateTime = transactionCompletedDateTime;
	}

	public String getReceiverPartyPublicName() {
		return receiverPartyPublicName;
	}

	public void setReceiverPartyPublicName(String receiverPartyPublicName) {
		this.receiverPartyPublicName = receiverPartyPublicName;
	}

	public BigDecimal getB2CWorkingAccountAvailableFunds() {
		return b2CWorkingAccountAvailableFunds;
	}

	public void setB2CWorkingAccountAvailableFunds(BigDecimal b2cWorkingAccountAvailableFunds) {
		b2CWorkingAccountAvailableFunds = b2cWorkingAccountAvailableFunds;
	}

	public BigDecimal getB2CUtilityAccountAvailableFunds() {
		return b2CUtilityAccountAvailableFunds;
	}

	public void setB2CUtilityAccountAvailableFunds(BigDecimal b2cUtilityAccountAvailableFunds) {
		b2CUtilityAccountAvailableFunds = b2cUtilityAccountAvailableFunds;
	}

	public String getSourceip() {
		return sourceip;
	}

	public void setSourceip(String sourceip) {
		this.sourceip = sourceip;
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

	@PreUpdate
	@PrePersist
	public void init(){
		if(status==null)
			status = ProcessingStatus.JUST_IN.getCode();
		if(resultType==null)
			resultType = -1;
		if(resultCode==null)
			resultCode = -1;
		if(resultDesc==null)
			resultDesc = "";
		if(originatorConversationID==null)
			originatorConversationID = "";
		if(conversationID==null)
			conversationID = "";
		if(transactionReceipt==null)
			transactionReceipt = "";
		if(transactionAmount==null)
			transactionAmount = BigDecimal.ZERO;
		if(b2CChargesPaidAccountAvailableFunds==null)
			b2CChargesPaidAccountAvailableFunds = BigDecimal.ZERO;
		if(b2CRecipientIsRegisteredCustomer==null)
			b2CRecipientIsRegisteredCustomer = Boolean.TRUE;
		if(transactionCompletedDateTime==null)
			transactionCompletedDateTime = new Date();
		if(receiverPartyPublicName==null)
			receiverPartyPublicName = "";
		if(b2CWorkingAccountAvailableFunds==null)
			b2CWorkingAccountAvailableFunds = BigDecimal.ZERO;
		if(b2CUtilityAccountAvailableFunds==null)
			b2CUtilityAccountAvailableFunds = BigDecimal.ZERO;
		if(timeStamp==null)
			timeStamp = new Date();
		if(transactionCompletedDateTime==null)
			transactionCompletedDateTime = new Date();
		if(sourceip==null)
			sourceip = AppPropertyHolder.DEF_SOURCE_IP;	
		
	}

}
