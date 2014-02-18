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
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.imdevice.pipe2wp.EMF;
import com.imdevice.pipe2wp.Subscribe;

@SuppressWarnings("serial")
public class Launcher extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		EntityManager em = EMF.get().createEntityManager();
		try{
			TypedQuery<Subscribe> q = em.createQuery("SELECT e FROM Subscribe e",Subscribe.class);
			List<Subscribe> subscribes=q.getResultList();
			if(!subscribes.isEmpty()){
/*				Queue queueD=QueueFactory.getDefaultQueue();
				queueD.add(withUrl("/dicManager")
						.param("action", "loadUserDict")
						.method(Method.POST)
						);*/
				Queue queue=QueueFactory.getQueue("FetchFeedQueue");
				for(Subscribe subscribe:subscribes){	
					System.out.println("Lanucher:"+subscribe.getLink());
					String key=KeyFactory.keyToString(subscribe.getKey());
					queue.add(withUrl("/tasks/feedfetcher")
							.param("key", key)
							.param("link", subscribe.getLink())//参数值不能为null
							.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
							);				
				}
			}
		}finally{
			em.close();
		}
	}
	

}

