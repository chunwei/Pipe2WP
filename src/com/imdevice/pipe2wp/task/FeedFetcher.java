package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("serial")
public class FeedFetcher extends HttpServlet {
	final String  DIRTYFEED=".*(?)(leiphone.com|ifanr.com).*";
	final String PollutedUrl=".*(?)tech2ipo.feedsportal.com.*";
	
	private String urlFilter(String pollutedUrl){
		String filterRegex=".*N0C(\\d+)/.*";
        Pattern p2 = Pattern.compile(filterRegex); 
        Matcher m2 = p2.matcher(pollutedUrl); 
        m2.find();
        String realUrl=null;
        try{
        	realUrl=m2.group(1);
        	if(realUrl.length()>1){
        		realUrl="http://tech2ipo.com/"+realUrl;
        		}
        }catch(Exception e){
        	//避免没匹配异常
        }
		return realUrl!=null?realUrl:pollutedUrl;
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
/*		 URL feedrrl = new URL("http://cn.engadget.com/rss.xml");

         SyndFeedInput input1 = new SyndFeedInput();
         try {
			SyndFeed feed = input1.build(new XmlReader(feedrrl));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}
         */
         
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		
		String keyString=req.getParameter("key");

		
		EntityManager em = EMF.get().createEntityManager();
		Key key=KeyFactory.stringToKey(keyString);
		Subscribe subscribe=em.find(Subscribe.class, key);
		System.out.println("FeedFetch: "+subscribe.getLink());
		
		try {						
            URL feedUrl = new URL(subscribe.getLink());

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            
            @SuppressWarnings("unchecked")
			List<SyndEntry> entries = feed.getEntries();
        	if (entries != null && !entries.isEmpty()) {
        		String lastItemLink=subscribe.getLastItemLink();
        		String link0=entries.get(0).getLink();
        		if(link0.matches(PollutedUrl)){
        			link0=urlFilter(link0);
        		}
        		if(!link0.equals(lastItemLink)){
        			em.getTransaction().begin();
        			subscribe.setLastItemLink(link0);
        			em.getTransaction().commit();
        		}
        		Date lastFetchDate=subscribe.getLastFetchDate();
        		if(entries.get(0).getPublishedDate().after(lastFetchDate)){
        			em.getTransaction().begin();
        			subscribe.setLastFetchDate(entries.get(0).getPublishedDate());
        			em.getTransaction().commit();
        		}
            	em.getTransaction().begin();
	            	if(null!=feed.getPublishedDate())
	            		subscribe.setLastPubDate(feed.getPublishedDate());
	            	else
	            		subscribe.setLastPubDate(entries.get(0).getPublishedDate());
    			em.getTransaction().commit();
        		
        		for (SyndEntry entry : entries) {
        			String link=entry.getLink();
            		if(link.matches(PollutedUrl)){
            			link=urlFilter(link);
            		}
        			if(link==null)continue;
        			if(link.equals(lastItemLink))break;
        			if(entry.getPublishedDate()==null)continue;
        			if(!entry.getPublishedDate().after(lastFetchDate))break;
        			System.out.println(link);
    				System.out.println(entry.getTitle());
    				@SuppressWarnings("unchecked")
					List<SyndCategory>cates=entry.getCategories();
    				StringBuilder categories=new StringBuilder("");
    	              if (cates != null && !cates.isEmpty()) {
    	            	  for (SyndCategory cate : cates) {
    	            		  categories.append( cate.getName().trim() );
    	            		  categories.append(",");
    	            	  }
    	            	  System.out.println(categories);
    	              }
    	            //-----------------------
      				String dirtypage=null;
          			@SuppressWarnings("unchecked")
						List<SyndContent> contents=entry.getContents();
          			if (contents != null && !contents.isEmpty()){  
          				StringBuilder dirtySB=new StringBuilder("");
          				for(SyndContent content:contents){
          					if (null!=content.getValue()) dirtySB.append(content.getValue());
          				}
          				if(dirtySB.length()>0) dirtypage=dirtySB.toString();
          			}
      				if(!entry.getLink().matches(DIRTYFEED)&&null!=dirtypage){
      					System.out.println("Get content from feed. goto: pageWasher");
      					Queue queue = QueueFactory.getQueue("WashPageQueue");
      					queue.add(withUrl("/tasks/pagewasher")
      							.param("a_id", subscribe.getUid())
      							.param("link", link)
      							.param("title", entry.getTitle())
      							.param("publish_date", entry.getPublishedDate().toString())
      							.param("keywords", categories.toString())
      							.param("dirtypage", dirtypage)
      							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
      							);	
      				}else{
      					System.out.println("Get content from webpage. goto: pageFetcher");
      					Queue queue=QueueFactory.getQueue("FetchPageQueue");
          				queue.add(withUrl("/tasks/pagefetcher")
          						.param("a_id", subscribe.getUid())
      							.param("link", link)
      							.param("title", entry.getTitle())
      							.param("publish_date", entry.getPublishedDate().toString())
      							.param("keywords", categories.toString())
      							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
      							);	
      				}
      				//------------------------	
        		}

        		
/****
 * 以下方法是通过feed更新时间及文章发布时间来判断是否抓取            
 * 

            //ATTENTION: feed.getPublishedDate() may return null
            //if return null, then just go and check entry.getPushlishedDate()
            if(null==feed.getPublishedDate() || feed.getPublishedDate().after(subscribe.getLastPubDate())){            	
            	
            	@SuppressWarnings("unchecked")
				List<SyndEntry> entries = feed.getEntries();
            	if (entries != null && !entries.isEmpty()) {
            		for (SyndEntry entry : entries) {
            			if(entry.getPublishedDate()==null)continue;
            			if(entry.getPublishedDate().after(subscribe.getLastFetchDate())){
            				System.out.println(entry.getLink());
            				System.out.println(entry.getTitle());
            				@SuppressWarnings("unchecked")
							List<SyndCategory>cates=entry.getCategories();
            				StringBuilder categories=new StringBuilder("");
            	              if (cates != null && !cates.isEmpty()) {
            	            	  for (SyndCategory cate : cates) {
            	            		  categories.append( cate.getName().trim() );
            	            		  categories.append(",");
            	            	  }
            	            	 // String[] cs=categories.toString().split(";");
            	            	  System.out.println(categories);
            	            	  
            	              }
//           				Queue queue=QueueFactory.getQueue("FetchPageQueue");
//            				queue.add(withUrl("/tasks/pagefetcher")
//            						.param("a_id", subscribe.getUid())
//        							.param("link", entry.getLink())
//        							.param("title", entry.getTitle())
//        							.param("keywords", categories.toString())
//        							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
//        							);	
            				//-----------------------
            				String dirtypage=null;
	            			@SuppressWarnings("unchecked")
							List<SyndContent> contents=entry.getContents();
	            			if (contents != null && !contents.isEmpty()){  
	            				StringBuilder dirtySB=new StringBuilder("");
	            				for(SyndContent content:contents){
	            					if (null!=content.getValue()) dirtySB.append(content.getValue());
	            				}
	            				if(dirtySB.length()>0) dirtypage=dirtySB.toString();
	            			}
            				if(null!=dirtypage){
            					System.out.println("Get content from feed. goto: pageWasher");
            					Queue queue = QueueFactory.getQueue("WashPageQueue");
            					queue.add(withUrl("/tasks/pagewasher")
            							.param("a_id", subscribe.getUid())
	        							.param("link", entry.getLink())
	        							.param("title", entry.getTitle())
	        							.param("publish_date", entry.getPublishedDate().toString())
            							.param("keywords", categories.toString())		
            							.param("dirtypage", dirtypage)	
            							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
            							);	
            				}else{
            					System.out.println("Get content from webpage. goto: pageFetcher");
            					Queue queue=QueueFactory.getQueue("FetchPageQueue");
	            				queue.add(withUrl("/tasks/pagefetcher")
	            						.param("a_id", subscribe.getUid())
	        							.param("link", entry.getLink())
	        							.param("title", entry.getTitle())
	        							.param("publish_date", entry.getPublishedDate().toString())
	        							.param("keywords", categories.toString())
	        							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
	        							);	
            				}
            				//------------------------	            			
            			}
            		}
            		if(entries.get(0).getPublishedDate().after(subscribe.getLastFetchDate())){
            			em.getTransaction().begin();
            			subscribe.setLastFetchDate(entries.get(0).getPublishedDate());
            			em.getTransaction().commit();
            		}
            	}
	 */
        		
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

