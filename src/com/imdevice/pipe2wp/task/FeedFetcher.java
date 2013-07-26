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
import com.google.appengine.api.taskqueue.TaskOptions.Method;
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
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
/*		 URL feedUrl = new URL("http://www.36kr.com/feed");

         SyndFeedInput input = new SyndFeedInput();
         try {
			SyndFeed feed = input.build(new XmlReader(feedUrl));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
         
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		
		String keyString=req.getParameter("key");

		//link="http://www.ifanr.com/feed";
		EntityManager em = EMF.get().createEntityManager();
		Key key=KeyFactory.stringToKey(keyString);
		Subscribe subscribe=em.find(Subscribe.class, key);
		
		try {			
						
            URL feedUrl = new URL(subscribe.getLink());

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            
            if(feed.getPublishedDate().after(subscribe.getLastPubDate())){            	
            	
            	@SuppressWarnings("unchecked")
				List<SyndEntry> entries = feed.getEntries();
            	if (entries != null && !entries.isEmpty()) {
            		for (SyndEntry entry : entries) {
            			if(entry.getPublishedDate().after(subscribe.getLastFetchDate())){
            				System.out.println(entry.getLink());
            				System.out.println(entry.getTitle());
            				Queue queue=QueueFactory.getQueue("FetchPageQueue");
            				queue.add(withUrl("/tasks/pagefetcher")
            						.param("a_id", subscribe.getUid())
        							.param("link", entry.getLink())
        							.param("title", entry.getTitle())
        							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
        							);	
            				/*
	            			@SuppressWarnings("unchecked")
							List<SyndContent> contents=entry.getContents();
	            			if (contents != null && !contents.isEmpty()){  
	            				System.out.println(contents.get(0).getValue());
	            				Queue queue=QueueFactory.getQueue("WashPageQueue");
	            				queue.add(withUrl("/tasks/pagewasher")
	            						.param("link", entry.getLink())
	            						.param("title", entry.getTitle())
	            						//.param("mt_excerpt", entry.getDescription().getValue())
	            						.param("dirtypage", contents.get(0).getValue())//可能需要循环contents来获取网站的内容？
	            						.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
	            						);            				
	            			}else{
	            				Queue queue=QueueFactory.getQueue("FetchPageQueue");
	            				queue.add(withUrl("/tasks/pagefetcher")
	        							.param("link", entry.getLink())
	        							.param("title", entry.getTitle())
	        							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
	        							);	
	            			}*/
            			}
            		}
            		if(entries.get(0).getPublishedDate().after(subscribe.getLastFetchDate())){
            			em.getTransaction().begin();
            			subscribe.setLastFetchDate(entries.get(0).getPublishedDate());
            			em.getTransaction().commit();
            		}
            	}
            	em.getTransaction().begin();
            	subscribe.setLastPubDate(feed.getPublishedDate());
    			em.getTransaction().commit();
            }            

        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
            o.println("ERROR: "+ex.getMessage());
        }finally{
        	em.close();
        }
	}
	

}

