package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.imdevice.WebSpider.Extractor;
import com.imdevice.pipe2wp.Post;
import com.imdevice.pipe2wp.XmlRPCHandler;

@SuppressWarnings("serial")
public class PageWasher extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String dirtypage=req.getParameter("dirtypage");
		Extractor extractor=new Extractor();
		String clearPage=extractor.getContent(dirtypage);
		Queue queue=QueueFactory.getQueue("PostQueue");
		queue.add(withUrl("/tasks/post2wp")
				.param("link", req.getParameter("link"))
				.param("title", req.getParameter("title"))
				.param("mt_excerpt", req.getParameter("mt_excerpt"))
				.param("description", clearPage)
				);
	}
	

}

