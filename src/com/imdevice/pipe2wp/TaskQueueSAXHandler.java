package com.imdevice.pipe2wp;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;


public class TaskQueueSAXHandler extends DefaultHandler {
	SimpleDateFormat sdf,df;
	StringBuffer sb;
	boolean inItem=false;
	boolean isNew=true;
	int count=0;
	Date lastFetch,tempLastFetch;  //record the "pubDate" of newest item in the last rss fetching
	Date lastPub;  //record the "date_created_gmt" of last publish post
	Post post;
	Queue queue;
	
	public void init(Object lastFetch,Object lastPub){
		queue = QueueFactory.getDefaultQueue();
		System.out.println("Before init:");
		System.out.println((Date)lastFetch);
		System.out.println((Date)lastPub);
		this.lastFetch=new Date();
		this.lastPub=new Date();
		if(null!=lastFetch){
			this.lastFetch=(Date)lastFetch;
		}else{
			this.lastFetch.setTime(this.lastFetch.getTime()-10*60*1000);
		}
		if(null!=lastPub&&this.lastPub.before((Date)lastPub)){
			this.lastPub=(Date)lastPub;
		}
		System.out.println("After init:");
		System.out.println("lastFetch:"+this.lastFetch);
		System.out.println("lastPub:"+this.lastPub);
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
			/*if(post.date_created_gmt.length()>0){//if "date_created_gmt" is set  return true;
				try {
					new XmlRPCHandler().callRpc(post);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			count++;
		}
		if(inItem&isNew){
			
			if(qName.equalsIgnoreCase("title")){
				post.setTitle(sb.toString().trim());
				
			}
			/* too much ad links in expert, drop it.
			if(qName.equalsIgnoreCase("description")){
				post.setMt_excerpt(delWhoPost(sb.toString().trim()));
			}
			*/
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
					//post.setDescription(delWhoPost(getContent(sb.toString().trim())));
					//System.out.println(post.toXML());
					queue.add(withUrl("/taskfetch")
							.param("url", sb.toString().trim())
							.param("title", post.title)
							.param("mt_excerpt", post.mt_excerpt)
							.param("date_created_gmt", post.date_created_gmt));
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

	private String delWhoPost(String src){
		delAds(src);
		return src.replaceAll("<b>感谢.+的投递</b><br />", "");
	}
	private String delAds(String src){
		String adWord="<img width='1' height='1'";
		int i=src.indexOf(adWord);
		if(i>-1)src=src.substring(0, i);
		return src;
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
