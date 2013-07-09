package com.imdevice.pipe2wp.datastore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
@Entity
public class Employee {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Key key;

	private String firstName;
	private String lastName;
	private Date hireDate;
	
	public Employee(){}
	public Employee(String firstName,String lastName,Date hireDate){
		this.firstName=firstName;
		this.lastName=lastName;
		this.hireDate=hireDate;
	}
	
	public Key getKey() {
        return key;
    }
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
	@Override
	public String toString(){
		return firstName+"."+lastName+" hired @ "+hireDate;
	}
}
