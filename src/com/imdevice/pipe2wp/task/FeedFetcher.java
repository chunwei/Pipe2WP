package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.imdevice.WebSpider.Extractor;
import com.imdevice.pipe2wp.EMF;
import com.imdevice.pipe2wp.Subscribe;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("serial")
public class FeedFetcher extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		
		String keyString=req.getParameter("key");
		/*
		 * **for debug only
		//url="http://www.leiphone.com/feed";
		//url="http://www.36kr.com/feed";
		//url="http://www.ifanr.com/feed";
		url="http://www.cnbeta.com/backend.php";
		//url="http://www.theverge.com/rss/index.xml";
		String param=req.getParameter("link");
		if(null!=param&&param.length()>5)url=param;
		*/
		//link="http://www.ifanr.com/feed";
		EntityManager em = EMF.get().createEntityManager();
		Key key=KeyFactory.stringToKey(keyString);
		em.getTransaction().begin();
		Subscribe subscribe=em.find(Subscribe.class, key);
		
		try {			
						
            URL feedUrl = new URL(subscribe.getLink());

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            
            if(feed.getPublishedDate().after(subscribe.getLastPubDate())){            	
            	@SuppressWarnings("unchecked")
				List<SyndEntry> entries = feed.getEntries();
            	if (entries != null && !entries.isEmpty()) {
            		Extractor extractor=new Extractor();
            		for (SyndEntry entry : entries) {
            			if(entry.getPublishedDate().after(subscribe.getLastFetchDate())){
	            			@SuppressWarnings("unchecked")
							List<SyndContent> contents=entry.getContents();
	            			if (contents != null && !contents.isEmpty()){    
	            				Queue queue=QueueFactory.getQueue("WashPageQueue");
	            				queue.add(withUrl("/tasks/pagewasher")
	            						.param("link", entry.getLink())
	            						.param("title", entry.getTitle())
	            						.param("mt_excerpt", entry.getDescription().getValue())
	            						.param("dirtypage", contents.get(0).getValue())//可能需要循环contents来获取网站的内容？
	            						);            				
	            			}else{
	            				Queue queue=QueueFactory.getQueue("FetchPageQueue");
	            				queue.add(withUrl("/tasks/pagefetcher")
	            						.param("link", entry.getLink())
	            						.param("title", entry.getTitle())
	            						.param("mt_excerpt", entry.getDescription().getValue())            						
	            						);    
	            			}
            			}
            		}
            		if(entries.get(0).getPublishedDate().after(subscribe.getLastFetchDate()))
            			subscribe.setLastFetchDate(entries.get(0).getPublishedDate());
            	}
            	subscribe.setLastPubDate(feed.getPublishedDate());
            	//em.getTransaction().begin();//!move to top, update must in one transaction!
    			//em.clear();
            	//em.merge(subscribe);
    			em.getTransaction().commit();
            }
            
            /*for dubug only
            //o.println(feed);
            o.println(feed.getTitle());
            o.println(feed.getPublishedDate());
            o.println(feed.getFeedType());
            o.println(feed.getImage());
            o.println("<hr>");
            
            @SuppressWarnings("unchecked")
			List<SyndEntry> entries = feed.getEntries();
            if (entries != null && !entries.isEmpty()) {
            	int i=1;
            	Extractor extractor;
             for (SyndEntry entry : entries) {
              o.println(i+" : "+entry.getTitle());
              o.println(entry.getPublishedDate());
              o.println(entry.getLink());
              @SuppressWarnings("unchecked")
              List<SyndContentImpl>contents=entry.getContents();
              //o.println(contents.get(0).getValue());
              o.println("<div style='background:#DDD;'>");
              extractor=new Extractor();
              if (contents != null && !contents.isEmpty()){
            	  o.println("Content from feed:");
            	  o.println(extractor.getContent(contents.get(0).getValue()));
              }else{
            	  o.println("Content from Extractor:");
            	  extractor.setUrl(entry.getLink());
            	  extractor.extract();
            	  o.println(extractor.getContent());            	  
              }
              o.println("</div>");
              i++;
              if(i>10)break;
             }
            }
            */

        }
        catch (Exception ex) {
            ex.printStackTrace();
            o.println("ERROR: "+ex.getMessage());
        }finally{
        	em.close();
        }
	}
	

}

