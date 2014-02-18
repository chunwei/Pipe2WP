package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class PageFetcher extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String link=req.getParameter("link");
		System.out.println("PageFetch: "+link);
		if(link==null||link.length()<5)return;
		Document doc=Jsoup.connect(link).timeout(5000).get();
		try{
			Queue queue = QueueFactory.getQueue("WashPageQueue");
			queue.add(withUrl("/tasks/pagewasher")
					.param("link", link)
					.param("a_id", req.getParameter("a_id"))
					.param("title", req.getParameter("title"))
					.param("publish_date", req.getParameter("publish_date"))
					.param("keywords", req.getParameter("keywords"))				
					.param("dirtypage", doc.body().html())	
					.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
					);			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

