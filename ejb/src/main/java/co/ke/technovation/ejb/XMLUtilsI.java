package co.ke.technovation.ejb;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import co.ke.technovation.entity.RedCrossPayment;
import co.ke.technovation.entity.CallType;
import co.ke.technovation.entity.MpesaIn;
import co.ke.technovation.entity.MpesaOut;

public interface XMLUtilsI {
	
	public static final String FIRST_NAME_KEY = "[Personal Details][First Name]";
	public static final String MIDDLE_NAME_KEY = "[Personal Details][Middle Name]";
	public static final String LAST_NAME_KEY = "[Personal Details][Last Name]";
	
	public static final String B2C_RESULTPARAMETER = "ResultParameter";
	public static final String B2C_TRANSACTIONRECEIPT = "TransactionReceipt";
	public static final String B2C_TRANSACTIONAMOUNT = "TransactionAmount";
	public static final String B2C_CHARGES_PAID_ACCOUNT_AVAILABLE_FUNDS = "B2CChargesPaidAccountAvailableFunds";
	public static final String B2C_RECIPIENT_IS_REGISTERED_CUSTOMER = "B2CRecipientIsRegisteredCustomer";
	public static final String B2C_TRANSACTION_COMPLETED_DATE_TIME = "TransactionCompletedDateTime";
	public static final String B2C_RECEIVER_PARTY_PUBLIC_NAME = "ReceiverPartyPublicName";
	public static final String B2C_WORKING_ACCOUNT_AVAILABLE_FUNDS = "B2CWorkingAccountAvailableFunds";
	public static final String B2C_UTILITY_ACCOUNT_AVAILABLE_FUNDS = "B2CUtilityAccountAvailableFunds";
	
	public String getValue(String xml,String tagname);

	public BigDecimal toBigDecimal(String value);
	
	public Integer toInteger(String value);

	public RedCrossPayment populateValues(String xml, RedCrossPayment payment) throws Exception;
	
	public MpesaIn populateValues(String xml, MpesaIn mpesaIn) throws Exception;

	public MpesaIn parseXML(String xmlstr, CallType callType);

	public MpesaOut parseB2CQueryTimeout(String xml, String ip_addr) throws Exception;

}
