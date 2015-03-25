package com.imdevice.pipe2wp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String title="木门电商刚刚萌芽 线上、线下融合是关键 | 品途网";
		title=title.split("[-_|]")[0].trim();  
		System.out.println( title);
        Pattern p1 = Pattern.compile("f(.+?)i(.+?)h"); 
        Matcher m1 = p1.matcher("finishabigfishfrish"); 
        while (m1.find()) { 
                String s0 = m1.group(); 
                String s1 = m1.group(1); 
                String s2 = m1.group(2); 
                System.out.println(s0 + "||" + s1 + "||" + s2); 
        } 
        
        final String  DIRTYFEED=".*(?)(leiphone.com|ifanr.com).*";
        String url="leiphone.com";
        System.out.println( url.matches(DIRTYFEED));
        final String PollutedURL=".*(?)tech2ipo.feedsportal.com.*";
        String pollutedUrl="http://tech2ipo.feedsportal.com/c/34822/f/641707/s/37305a98/sc/24/l/0Ltech2ipo0N0C63419/story01.htm";
        System.out.println( pollutedUrl.matches(PollutedURL));
        String filterRegex=".*N0C(\\d+)/.*";
        Pattern p2 = Pattern.compile(filterRegex); 
        Matcher m2 = p2.matcher(pollutedUrl); 
        m2.find();
        //while (m2.find()) { 
        String realUrl=null;
        try{
            String s0 = m2.group(); 
            realUrl = m2.group(1); 
            if(realUrl.length()>1){
        		realUrl="http://tech2ipo.com/"+realUrl;
        		}
            System.out.println(s0 + "||" + realUrl ); 
        }catch(Exception e){
        	System.out.println(realUrl ); 
        	System.out.println(e.getMessage() ); 
        }

    //} 
	}

}
