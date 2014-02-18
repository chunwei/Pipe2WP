package com.imdevice.pipe2wp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

/**
 * @author luchunwei
 *
 */
@Entity
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	private String link;//the feed url
	private String uid;//user_id in wordpress
//	private String title;
//	private String category;//tech,news,hotpic
//	private String updatePeriod;//hourly
//	private String updateFrequency;//1
	private Date lastPubDate;
	private Date lastFetchDate;
	private String lastItemLink;
	
	public String getLastItemLink() {
		return lastItemLink;
	}
	public void setLastItemLink(String lastItemLink) {
		this.lastItemLink = lastItemLink;
	}
	public Subscribe(){
		init();
	}
	public Subscribe(String link){
		init();
		this.link=link;
	}
	
	private void init(){
		link="";
		uid="1";
		lastItemLink="newlink";
		Date init=new Date();
		init.setTime(init.getTime()-10*24*60*60*1000);
		lastPubDate=init;
		lastFetchDate=init;
	}
	public Key getKey() {
        return key;
    }

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
/*	public Subscribe(String link,String title){
		this.link=link;
		this.title=title;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUpdatePeriod() {
		return updatePeriod;
	}
	public void setUpdatePeriod(String updatePeriod) {
		this.updatePeriod = updatePeriod;
	}
	public String getUpdateFrequency() {
		return updateFrequency;
	}
	public void setUpdateFrequency(String updateFrequency) {
		this.updateFrequency = updateFrequency;
	}*/
	public Date getLastPubDate() {
		return lastPubDate;
	}
	public void setLastPubDate(Date lastPubDate) {
		this.lastPubDate = lastPubDate;
	}
	public Date getLastFetchDate() {
		return lastFetchDate;
	}
	public void setLastFetchDate(Date lastFetchDate) {
		this.lastFetchDate = lastFetchDate;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

}
