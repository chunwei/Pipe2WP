package com.imdevice.pipe2wp;

import java.util.Date;

/**
 * @author luchunwei
 *
 */
public class Feed {
	private String title;
	private String link;//the feed url
	private String icon;
	private String category;//tech,news,hotpic
	private String updatePeriod;//hourly
	private String updateFrequency;//1
	private Date lastItemPubDate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
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
	}
	public Date getLastItemPubDate() {
		return lastItemPubDate;
	}
	public void setLastItemPubDate(Date lastItemPubDate) {
		this.lastItemPubDate = lastItemPubDate;
	}

}
