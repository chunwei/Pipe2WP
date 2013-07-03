package com.imdevice.pipe2wp;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Option {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String name;
	@Persistent
	private Date dateValue;
	
	public Option(String name){
		this.name=name;
	}
	
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
