package com.imdevice.pipe2wp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * @author luchunwei
 *
 */
@Entity
public class UserDefinedDict {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	private String name;
	private Text words;

	
	public UserDefinedDict(){
	}
	public UserDefinedDict(String name){
		this.name=name;
	}
	public Key getKey() {
        return key;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Text getWords() {
		return words;
	}
	public void setWords(Text words) {
		this.words = words;
	}
}
