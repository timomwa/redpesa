package co.ke.technovation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name="payments", schema=AppPropertyHolder.REDCROS_SCHEMA_NAME)
public class RedCrossPayment implements Serializable {
	
	@Id
	//@Column(name = "id", nullable = false)
	//@SequenceGenerator(name = "mobile_money_seq", sequenceName = "mobile_money_seq")
	//@GeneratedValue(strategy = GenerationType.AUTO, generator = "mobile_money_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@Column(name="telco_transaction_id", length=100)
	private String telco_transaction_id;
	
	@Column(name="amount", precision=10, scale=1)
	private BigDecimal amount;
	
	@Column(name="phone_number", length=100)
	private String phone_number;
	
	@Column(name="first_name", length=100)
	private String first_name;
	
	@Column(name="last_name", length=100)
	private String last_name;
	
	@Column(name="is_processed")
	private Boolean is_processed;
	
	@Column(name="account_number", length=100)
	private String account_number;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_received")
	private Date date_received;
	
	@Column(name="telco_name", length=100)
	private String telco_name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_paid")
	private Date date_paid;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getTelco_transaction_id() {
		return telco_transaction_id;
	}
	public void setTelco_transaction_id(String telco_transaction_id) {
		this.telco_transaction_id = telco_transaction_id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public Boolean getIs_processed() {
		return is_processed;
	}
	public void setIs_processed(Boolean is_processed) {
		this.is_processed = is_processed;
	}
	
	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public Date getDate_received() {
		return date_received;
	}

	public void setDate_received(Date date_received) {
		this.date_received = date_received;
	}

	public String getTelco_name() {
		return telco_name;
	}

	public void setTelco_name(String telco_name) {
		this.telco_name = telco_name;
	}

	public Date getDate_paid() {
		return date_paid;
	}

	public void setDate_paid(Date date_paid) {
		this.date_paid = date_paid;
	}

	@PrePersist
	@PreUpdate
	public void update(){
		if(is_processed==null)
			is_processed = Boolean.FALSE;
		if(date_received==null)
			date_received = new Date();
		if(date_paid==null)
			date_paid = new Date();
	}
	


}
