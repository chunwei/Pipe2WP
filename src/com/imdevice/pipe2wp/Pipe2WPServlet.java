package com.imdevice.pipe2wp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;




@SuppressWarnings("serial")
public class Pipe2WPServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ServletContext sc=this.getServletContext();
		Long last_time=(Long)sc.getAttribute("last_time");
		Date d=new Date();
		if(null==last_time){
				last_time=d.getTime()-120*60*1000;//from 10mins ago
				last_time=last_time/1000;
			}
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		DateFormat shortDateFormat = DateFormat.getDateTimeInstance( 
				DateFormat.SHORT, DateFormat.SHORT);
		o.println(d);
		o.println(d.getTime());
		o.println(last_time);
		try {
            URL url = new URL("http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=30&spec=&type=&date=&ch=05&k=&offset_page=0&offset_num=0&num=3&asc=&page=1&last_time="+last_time+"&r="+Math.random());
            o.println(url.toString());
            StringBuffer jsonResponse=new StringBuffer();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"GBK"));//"GBK" should set to the source's encode
            String line;
            while ((line = reader.readLine()) != null) {
            	jsonResponse.append(line);
            }
            reader.close();        
    		
            System.out.println(jsonResponse.toString());
            o.println(jsonResponse.toString());
            try {
				JSONObject jsonData=new JSONObject(jsonResponse.substring(jsonResponse.indexOf("{"), jsonResponse.lastIndexOf("}")+1));
				//System.out.println(jsonData.toString());
				
				JSONArray list=jsonData.getJSONArray("list");
				d.setTime(last_time*1000);//d.setTime(Long.parseLong(last_time));
				o.println("Get "+list.length()+" news since "+shortDateFormat.format(d));
				if(list.length()>0)sc.setAttribute("last_time", jsonData.getLong("last_time"));
				for(int i=0;i<list.length();i++){
					JSONObject article=list.getJSONObject(i);
					o.println("<br><hr>"+article.getString("title"));
					o.println("<br>"+article.get("url"));
					o.println("<br>"+URLFetch(article.getString("url")));
				}
            }catch (Exception e){
            	e.printStackTrace();        	
            }
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        } 
	/*	
	       Properties props = new Properties();
	        Session session = Session.getDefaultInstance(props, null);

	        String msgBody = "Google appengine mail service";

	        try {
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("luchunwei@gmail.com", "Admin"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress("luchunwei@hotmail.com", "Mr. User"));
	            msg.setSubject("Hello from Pipe2WP "+count+"/n"+d+"/n"+d.getTime());
	            msg.setText(msgBody);
	            Transport.send(msg);
	    
	        } catch (AddressException e) {
	        	e.printStackTrace(o);
	        } catch (MessagingException e) {
	        	e.printStackTrace(o);
	        }
	        o.println("mail sent. count:"+count);
	        count++;
	        sc.setAttribute("last_time", count);
	        */
	}

	private String URLFetch(String url){
		StringBuffer sb=new StringBuffer();
		try {
			URL Url = new URL(url);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(Url.openStream(),"GBK"));//"GBK" should set to the source's encode
        String line;boolean flag=false;
        while ((line = reader.readLine()) != null) {
        	if(line.indexOf("publish_helper_end")>-1){
        		flag=false;break;
        	}
        	if(line.indexOf("publish_helper")>-1){
        		flag=true;continue;
        	}
        	if(flag)sb.append(line);
        }
        reader.close();        
	} catch (MalformedURLException e) {
        // ...
    } catch (IOException e) {
        // ...
    }
    return sb.toString();
	}
	
	private String CutContent(String source){
		String content="";
		 Pattern p1 = Pattern.compile("f(.+?)i(.+?)h"); 
	        Matcher m1 = p1.matcher("finishabigfishfrish"); 
	        while (m1.find()) { 
	                String s0 = m1.group(); 
	                String s1 = m1.group(1); 
	                String s2 = m1.group(2); 
	                System.out.println(s0 + "||" + s1 + "||" + s2); 
	        } 
		return content;
	}
}
