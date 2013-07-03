package com.imdevice.pipe2wp;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TaskPostServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		Post post=new Post();
		//post.setPost_status("pending");//comment for 远端blog自主判断状态
		post.setTitle(req.getParameter("title"));
		post.setMt_excerpt(req.getParameter("mt_excerpt"));
		post.setDate_created_gmt(req.getParameter("date_created_gmt"));
		post.setDescription(req.getParameter("description"));

		try {
			new XmlRPCHandler().callRpc1(post);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}

