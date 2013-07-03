package com.imdevice.pipe2wp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class Feed2WP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
/*		try {
			String strDate="Thu, 14 Apr 2011 01:23:12 GMT";
			SimpleDateFormat sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

			Date pubDate=sdf.parse(strDate);
			System.out.println(pubDate);
			System.out.println(sdf.format(pubDate));

			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(true)return;*/
		try{
		
		URL url=new URL("http://cnbeta.com/backend.php");
		
		InputSource is=new InputSource(url.openStream());
		SAXParserFactory  factory=SAXParserFactory.newInstance();
		SAXParser parser;

			parser = factory.newSAXParser();
			//parser.parse(is, new MySAXHandler());
			MySAXHandler myHandler=new MySAXHandler();
			myHandler.init(null,null);
			parser.parse(is, myHandler);
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

}

