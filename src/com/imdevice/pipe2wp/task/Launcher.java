package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.imdevice.WebSpider.Extractor;
import com.imdevice.pipe2wp.EMF;
import com.imdevice.pipe2wp.Post;
import com.imdevice.pipe2wp.Subscribe;
import com.imdevice.pipe2wp.XmlRPCHandler;
import com.imdevice.pipe2wp.datastore.Employee;

@SuppressWarnings("serial")
public class Launcher extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		EntityManager em = EMF.get().createEntityManager();
		TypedQuery<Subscribe> q = em.createQuery("SELECT e FROM Subscribe e",Subscribe.class);
		List<Subscribe> subscribes=q.getResultList();
		if(!subscribes.isEmpty()){
			Queue queue=QueueFactory.getQueue("FetchFeedQueue");
			for(Subscribe subscribe:subscribes){	
				queue.add(withUrl("/tasks/feedfetcher")
						.param("key", KeyFactory.keyToString(subscribe.getKey()))
						);				
			}
		}
	}
	

}

