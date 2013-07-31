package com.imdevice.test;

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
public class LoadDic extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		try{//task的执行时间限制是10分钟
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(withUrl("/clearreader")
					//.param("link", link)
					.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
					);			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

