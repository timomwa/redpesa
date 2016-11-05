package co.ke.technovation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import co.ke.technovation.constants.AppPropertyHolder;


@Entity
@Table(name="msisdn", schema=AppPropertyHolder.CMP_SCHEMA_NAME)
public class Msisdn extends AbstractEntity {
	
	@Column(name="msisdn")
	private String msisdn;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	
}
