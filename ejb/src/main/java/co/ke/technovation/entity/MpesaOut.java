package co.ke.technovation.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name = "mpesa_out", indexes = {
		@Index(columnList = "transId", name = "msaOuttransIdIdx", unique = true) }, schema = AppPropertyHolder.CMP_SCHEMA_NAME)
public class MpesaOut extends AbstractEntity {

	private static final long serialVersionUID = -8590242104546595829L;
	
	@Column(name="raw_xml_id", nullable=false)
	private Long raw_xml_id;

	@Column(name="resultType")
	private Integer resultType;
	
	@Column(name="resultCode")
	private Integer resultCode;
	
	@Column(name="resultDesc", length=500, nullable=false)
	private String resultDesc;
	
	@Column(name="transType", length=200, nullable=false)
	private String originatorConversationID;
	
	@Column(name="transType", length=200, nullable=false)
	private String conversationID;
	
	private String transId;// = KKN8BSNYLG
	private String transactionReceipt;// = KKN8BSNYLG
	@Column(name = "transAmount", precision = 10, scale = 2, nullable = false)
	private BigDecimal transactionAmount;// = 50.0
	@Column(name = "transAmount", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CChargesPaidAccountAvailableFunds;// = 0.00
	private String b2CRecipientIsRegisteredCustomer;// = Y
	private Date transactionCompletedDateTime;// = 23.11.2016 23:49:09
	private String receiverPartyPublicName;// = 254720988636 - TIMOTHY GIKONYO
	@Column(name = "transAmount", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CWorkingAccountAvailableFunds;// = 16971.00
	@Column(name = "transAmount", precision = 10, scale = 2, nullable = false)
	private BigDecimal b2CUtilityAccountAvailableFunds;// = 14850.00

}
