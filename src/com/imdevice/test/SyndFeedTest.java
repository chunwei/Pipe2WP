package com.imdevice.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class SyndFeedTest {

	public static void main(String[] args) {
		String link="http://www.yseeker.com/feed";
		try {
			URL feedUrl = new URL(link);
	        SyndFeedInput input = new SyndFeedInput();
	        SyndFeed feed = input.build(new XmlReader(feedUrl));
        
        @SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
        System.out.println("feed published Date="+feed.getPublishedDate());
        System.out.println("entries(0).link="+entries.get(0).getLink());
        
    	if (entries != null && !entries.isEmpty()) {
    		for (SyndEntry entry : entries) {
    			System.out.println(entry.getLink() +" -- "+ entry.getPublishedDate());
    		}
    	}
		} catch (IllegalArgumentException | FeedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
