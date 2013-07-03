package com.imdevice.pipe2wp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class Post2WPServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		/*String pid=req.getParameter("pid");
		HttpSession ses=req.getSession();
		if(ses.getAttribute("post"+pid)!=null){
			Post post=(Post)ses.getAttribute("post"+pid);
			try {
				new XmlRPCHandler().callRpc1(post);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		Post post=new Post();
		post.setPost_status("pending");
		post.setTitle(req.getParameter("title"));
		post.setDescription(req.getParameter("content"));
		try {
			new XmlRPCHandler().callRpc1(post);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			post=null;//也许能帮助加快内存回收
		}
		PrintWriter o=resp.getWriter();
		o.println(req.getParameter("title"));
		o.println("<hr>");
		o.println(req.getParameter("content"));
		o.flush();
        o.close();
		//Post post=new Post();
		//post.setPost_status("pending");//comment out for 远端blog自主判断状态
		//post.setTitle((String)ses.getAttribute("title"));
		//post.setMt_excerpt(req.getParameter("mt_excerpt"));
		//post.setDate_created_gmt(req.getParameter("date_created_gmt"));
		//post.setDescription((String)ses.getAttribute("description"));

		
	}
	

}

