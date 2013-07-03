package com.imdevice.pipe2wp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MySAXHandler extends DefaultHandler {
	SimpleDateFormat sdf,df;
	StringBuffer sb;
	boolean inItem=false;
	boolean isNew=true;
	int count=0;
	Date lastFetch,tempLastFetch;  //record the "pubDate" of newest item in the last rss fetching
	Date lastPub;  //record the "date_created_gmt" of last publish post
	Post post;
	
	public void init(Object lastFetch,Object lastPub){
		System.out.println((Date)lastFetch);
		System.out.println((Date)lastPub);
		this.lastFetch=new Date();
		this.lastPub=new Date();
		if(null!=lastFetch){
			this.lastFetch=(Date)lastFetch;
		}else{
			this.lastFetch.setTime(this.lastFetch.getTime()-22*60*60*1000);
		}
		if(null!=lastPub&&this.lastPub.before((Date)lastPub)){
			this.lastPub=(Date)lastPub;
		}
		System.out.println("After init:");
		System.out.println(this.lastFetch);
		System.out.println(this.lastPub);
	}
	
	@Override
	public void startDocument() throws SAXException {
		sb=new StringBuffer();
		sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		df=new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss",Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		//lastFetch=lastPub=new Date();//if(lastPub.before(new Date()))lastPub.before=new Date();
		//lastFetch.setTime(lastFetch.getTime()-1*60*60*1000);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase("item")){
			inItem=true;
			if(isNew)post=new Post();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		sb.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equalsIgnoreCase("item")){
			inItem=false;
			if(!isNew)return;
			if(post.date_created_gmt.length()>0){//if "date_created_gmt" is set  return true;
				try {
					new XmlRPCHandler().callRpc(post);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			count++;
		}
		if(inItem&isNew){
			
			if(qName.equalsIgnoreCase("title")){
				post.setTitle(sb.toString().trim());
			}
			if(qName.equalsIgnoreCase("description")){
				post.setMt_excerpt(delWhoPost(sb.toString().trim()));
			}
			/*if(qName.matches("title|guid|pubDate"))
				System.out.println("["+qName+"]"+sb.toString().trim());*/
			if(qName.equalsIgnoreCase("pubDate")){
				try {
				String dateStr=sb.toString().trim();
				Date pubDate=sdf.parse(dateStr);
				if(pubDate.after(lastFetch)){
					if(count==0){tempLastFetch=pubDate;}//update lastFetch
					lastPub.setTime(lastPub.getTime()+2*60*1000);
					post.setDate_created_gmt(df.format(lastPub));
					System.out.println("new! "+pubDate+" date_created_gmt:"+df.format(lastPub));
					System.out.println(post.title);
				}else{
					isNew=false;
					System.out.println("old! "+pubDate);
				}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//if(count<10)getContent(sb.toString());				
			}
			if(qName.equalsIgnoreCase("guid")){
				System.out.println(count+"  "+sb.toString().trim());
				if(post.date_created_gmt.length()>0){//if "date_created_gmt" is set  return true;
					post.setDescription(delWhoPost(getContent(sb.toString().trim())));
				}
				//if(count<10)getContent(sb.toString());				
			}
			
		}
		sb.setLength(0);
	}

	@Override
	public void endDocument() throws SAXException {
		if(null!=tempLastFetch)lastFetch=tempLastFetch;
		System.out.println("lastFetch:"+lastFetch);
	}

	private String getContent(String guid) {
		
		 URL url;
		 StringBuffer sb=new StringBuffer();
		 boolean flag=false;
		try {
			url = new URL(guid);
	         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"GBK"));
	         String line;
	         while ((line = reader.readLine()) != null) {
	        	 if(line.trim().startsWith("<div class=\"digbox\">")){flag=false;break;}
	        	 if(line.trim().startsWith("<div id=\"news_content\">")){flag=true;continue;}
	        	 if(flag)sb.append(line);
	         }
			reader.close();
			//System.out.println(sb.toString().trim());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
		
	}
	
	private String delWhoPost(String src){
		//return src.replaceAll("<b>感谢.+的投递</b><br />", "");
		return src.replaceAll("<b>感谢.+的投递</b><br />", "");
	}

	public Date getLastFetch() {
		return lastFetch;
	}

	public void setLastFetch(Date lastFetch) {
		this.lastFetch = lastFetch;
	}

	public Date getLastPub() {
		return lastPub;
	}

	public void setLastPub(Date lastPub) {
		this.lastPub = lastPub;
	}
}
