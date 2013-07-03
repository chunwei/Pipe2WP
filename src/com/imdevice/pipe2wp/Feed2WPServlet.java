package com.imdevice.pipe2wp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@SuppressWarnings("serial")
public class Feed2WPServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		resp.getWriter().println(new XmlRPCHandler().test());
		if(true)return;
			ServletContext sc=this.getServletContext();
/*			Date lastFetch=(Date)sc.getAttribute("lastFetch");
			Date lastPub=(Date)sc.getAttribute("lastPub");*/
			try{
			URL url=new URL("http://cnbeta.com/backend.php");
			
			InputSource is=new InputSource(url.openStream());
			SAXParserFactory  factory=SAXParserFactory.newInstance();
			SAXParser parser= factory.newSAXParser();
			MySAXHandler myHandler=new MySAXHandler();
			myHandler.init(sc.getAttribute("lastFetch"),sc.getAttribute("lastPub"));
			parser.parse(is, myHandler);
			sc.setAttribute("lastFetch", myHandler.getLastFetch());
			sc.setAttribute("lastPub", myHandler.getLastPub());
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
}

