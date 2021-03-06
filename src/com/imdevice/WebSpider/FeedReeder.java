package com.imdevice.WebSpider;

import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReeder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url="";
		//url="http://cn.engadget.com/rss.xml";
		//url="http://www.leiphone.com/feed";
		//url="http://www.36kr.com/feed";
		//url="http://www.ifanr.com/feed";
		url="http://feed.feedsky.com/pingwest";
		//url="http://www.cnbeta.com/backend.php";
		//url="http://www.theverge.com/rss/index.xml";
		//url="http://tech2ipo.com/feed";
		//url="http://feed.evolife.cn/";
		//url="http://thewind.hk/feed";
		//url="http://www.yseeker.com/feed";
		//url="http://feeds.geekpark.net/";
		//url="http://www.tmtpost.com/feed/";
		//url="http://rss.mydrivers.com/rss.aspx?Tid=1";
		try {
            URL feedUrl = new URL(url);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            //System.out.println(feed);
            System.out.println(feed.getTitle());
            System.out.println(feed.getPublishedDate());
            System.out.println(feed.getFeedType());
            System.out.println(feed.getImage());
            System.out.println("<hr>");
          //  if(true)return;
            @SuppressWarnings("unchecked")
			List<SyndEntry> entries = feed.getEntries();
            if (entries != null && !entries.isEmpty()) {
            	int i=1;
            	Extractor extractor;
             for (SyndEntry entry : entries) {
              System.out.println(i+" : "+entry.getTitle());
              System.out.println(entry.getPublishedDate());
              System.out.println(entry.getLink());
              List<SyndCategory>cates=entry.getCategories();
              StringBuilder categories=new StringBuilder("");
              if (cates != null && !cates.isEmpty()) {
            	  for (SyndCategory cate : cates) {
            		  categories.append( cate.getName() );
            		  categories.append(";");
            	  }
            	  String[] cs=categories.toString().split(";");
              }
              System.out.println(categories);
              
              //System.out.println(entry.getDescription().getValue());
              @SuppressWarnings("unchecked")
              List<SyndContentImpl>contents=entry.getContents();
              //System.out.println(contents.get(0).getValue());
              System.out.println("<div style='background:#DDD;'>");
              extractor=new Extractor();
              if (false&&contents != null && !contents.isEmpty()){
            	  System.out.println("Content from feed:");
            	  System.out.println(extractor.getContent(contents.get(0).getValue()));
              }else{
            	  System.out.println("Content from Extractor:");
            	  extractor.setUrl(entry.getLink());
            	  extractor.setTitle(entry.getTitle());
            	  extractor.extract();
            	  System.out.println(extractor.getContent());
              }
              
              System.out.println("</div>");
              i++;
              if(i>10)break;
             }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }

	}

}
