package co.ke.technovation.entity;

import java.math.BigInteger;
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
@Table(name = "gen_counter", schema = AppPropertyHolder.CMP_SCHEMA_NAME, indexes = {
		@Index(columnList = "name", name = "counternameIdx"),
		@Index(columnList = "timeStamp", name = "countrtimeStampIdx"),
		@Index(columnList = "name,timeStamp", name = "countrtStampNmIdx")})
public class Counter extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3047076426889613355L;

	@Column(name="name", length=10, nullable=false, unique=true)
	private String name;
	
	@Column(name="counts", nullable=false)
	private BigInteger counts;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getCounts() {
		return counts;
	}

	public void setCounts(BigInteger counts) {
		this.counts = counts;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@PreUpdate
	@PrePersist
	public void init(){
		if(timeStamp==null)
			timeStamp = new Date();
		if(counts==null)
			counts = BigInteger.ZERO;
	}
}
