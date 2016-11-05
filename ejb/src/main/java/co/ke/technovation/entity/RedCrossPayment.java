package co.ke.technovation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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

import co.ke.technovation.constants.AppPropertyHolder;

@Entity
@Table(name="payments", schema=AppPropertyHolder.REDCROS_SCHEMA_NAME)
public class RedCrossPayment implements Serializable {
	
	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "mobile_money_seq", sequenceName = "mobile_money_seq")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "mobile_money_seq")
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
	
	
	@PrePersist
	@PreUpdate
	public void update(){
		if(is_processed==null)
			is_processed = Boolean.FALSE;
	}
	


}
