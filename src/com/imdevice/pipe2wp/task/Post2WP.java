package com.imdevice.pipe2wp.task;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imdevice.pipe2wp.Post;
import com.imdevice.pipe2wp.XmlRPCHandler;

@SuppressWarnings("serial")
public class Post2WP extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		Post post=new Post();
		post.setPost_status("pending");
		post.setTitle(req.getParameter("title"));
		//post.setMt_excerpt(req.getParameter("mt_excerpt"));
		post.setDescription(req.getParameter("description"));
		try {
			new XmlRPCHandler().callRpc1(post);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			post=null;//也许能帮助加快内存回收
		}
		
	}
	

}

