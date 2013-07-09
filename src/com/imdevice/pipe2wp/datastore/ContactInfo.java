package com.imdevice.pipe2wp.datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
@PersistenceCapable
public class ContactInfo {
	@PrimaryKey 
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String city;
	@Persistent
	private String stateOrProvince;
	@Persistent
	private String zipCode;
	
	public ContactInfo(){}
	public ContactInfo(String firstName,String lastName,String zipCode){
		this.city=firstName;
		this.stateOrProvince=lastName;
		this.zipCode=zipCode;
	}
	
	public Key getKey() {
        return key;
    }
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateOrProvince() {
		return stateOrProvince;
	}
	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@Override
	public String toString(){
		return city+" : "+stateOrProvince+" : "+zipCode;
	}
}
