package com.imdevice.pipe2wp;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class TaskFetchServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		String description=delWhoPost(getContent(req.getParameter("url")));

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/taskpost")
				.param("description", description)
				.param("title", req.getParameter("title"))
				.param("mt_excerpt", req.getParameter("mt_excerpt"))
				.param("date_created_gmt", req.getParameter("date_created_gmt")));
	}
	private String getContent(String guid) {
		
		 URL url;
		 StringBuffer sb=new StringBuffer();
		 boolean flag=false;
		try {
			url = new URL(guid);
	         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"utf8"));
	         String line;
	         while ((line = reader.readLine()) != null) {
	        	 if(line.trim().startsWith("<div id=\"googleAd_afc\">")){flag=false;break;}
	        	 if(!flag)if(line.trim().startsWith("<div class=\"content\">")){flag=true;continue;}
	        	 if(line.trim().startsWith("<div style=\"float:right")){continue;}
	        	 if(flag)sb.append(line);
	         }
			reader.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
		
	}
	private String delWhoPost(String src){
		src=delBadImg(src);//<b>感谢<a href="http://www.inwaishe.com" target="_blank">in外设</a>的投递</b><br />
		return src.replaceAll("<strong>感谢.+的投递</strong><br/>", "");
	}
	private String delBadImg(String src){
		return src.replaceAll("<img src=\"pic/down.gif\" />","");
	}

}

