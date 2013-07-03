package com.imdevice.pipe2wp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MySAXHandler1 extends DefaultHandler {
	StringBuffer sb;
	boolean inItem=false;
	int count=0;
	
	@Override
	public void startDocument() throws SAXException {
		sb=new StringBuffer();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase("item"))inItem=true;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		sb.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equalsIgnoreCase("item"))inItem=false;
		if(inItem){
			/*if(qName.matches("title|guid|pubDate"))
				System.out.println("["+qName+"]"+sb.toString().trim());*/
			if(qName.equalsIgnoreCase("guid")){
				if(count<10){
				System.out.println(count+"  "+sb.toString().trim()+"<hr>");
				getContent(sb.toString());
				}
				count++;
			}
		}
		sb.setLength(0);
	}

	private void getContent(String guid) {
		
		 URL url;
		try {
			url = new URL(guid);
         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"GBK"));
         String line;
         StringBuffer sb=new StringBuffer("============================\n");
         boolean flag=false;
         while ((line = reader.readLine()) != null) {
        	 if(line.trim().startsWith("<div class=\"digbox\">")){flag=false;break;}
        	 if(line.trim().startsWith("<div id=\"news_content\">")){flag=true;continue;}
        	 if(flag)sb.append(line);
         }
			reader.close();
			System.out.println(sb.toString().trim());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
