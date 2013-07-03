package com.imdevice.pipe2wp;

public class Post {
	/*  string post_status
	 	'new' - When there's no previous status
		'publish' - A published post or page
		'pending' - post in pending review
		'draft' - a post in draft status
		'auto-draft' - a newly created post, with no content
		'future' - a post to publish in the future
		'private' - not visible to users who are not logged in
		'inherit' - a revision. see get_children.
		'trash' - post is in trashbin. added with Version 2.9.
	 */
	String post_status;
	String title;
	String mt_excerpt;
	String description;
	String date_created_gmt;
	String categories;
	String mt_keywords;
	
	public Post(){
		post_status=null;
		title="";
		mt_excerpt="";
		description="";
		date_created_gmt="";
		categories="业界新闻";
		mt_keywords="";
	}
	
	public String toXML(){
		StringBuffer xml=new StringBuffer();
		if(null!=post_status&&post_status.length()>0){
		xml.append("<member>");
		xml.append("<name>post_status</name>");
		xml.append("<value><string>"+post_status+"</string></value>");
		xml.append("</member>");
		}
		xml.append("<member>");
		xml.append("<name>title</name>");
		xml.append("<value><string>"+title+"</string></value>");
		xml.append("</member>");
		xml.append("<member>");
		xml.append("<name>mt_excerpt</name>");
		xml.append("<value><string><![CDATA["+mt_excerpt+"]]></string></value>");
		xml.append("</member>");
		xml.append("<member>");
		xml.append("<name>description</name>");
		xml.append("<value><string><![CDATA["+description+"]]></string></value>");
		xml.append("</member>");
		if(date_created_gmt.length()>0){
		xml.append("<member>");
		xml.append("<name>date_created_gmt</name>");
		xml.append("<value><dateTime.iso8601>"+date_created_gmt+"</dateTime.iso8601></value>");
		xml.append("</member>");
		}
		return xml.toString();
	}
	
	public String getPost_status() {
		return post_status;
	}
	
	public void setPost_status(String post_status) {
		this.post_status = post_status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMt_excerpt(String mt_excerpt) {
		this.mt_excerpt = mt_excerpt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate_created_gmt(String date_created_gmt) {
		this.date_created_gmt = date_created_gmt;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public void setMt_keywords(String mt_keywords) {
		this.mt_keywords = mt_keywords;
	}

}
