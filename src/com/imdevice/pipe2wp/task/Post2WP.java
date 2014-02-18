package com.imdevice.pipe2wp.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
		String contentWithSrc=attachSrc(
				req.getParameter("description"),
				req.getParameter("link"));
		Post post=new Post();
		//post.setPost_status("pending");
		post.setTitle(req.getParameter("title"));
		post.setMt_keywords(req.getParameter("keywords"));
		post.setWp_author_id(req.getParameter("a_id"));
		//post.setMt_excerpt(req.getParameter("mt_excerpt"));
		post.setDescription(contentWithSrc);
		post.add_Custom_field("original_url", req.getParameter("link"));
		post.add_Custom_field("original_first_image_url", req.getParameter("first_image_url"));
		post.add_Custom_field("original_publish_date", req.getParameter("publish_date"));
		try {
			new XmlRPCHandler().callRpc1(post);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			post=null;//也许能帮助加快内存回收
		}
		
	}
	private String attachSrc(String content,String srcUrl){
		String host="Source";
    	try {
    		host=new URL(srcUrl).getHost();
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	}
    	StringBuilder sb=new StringBuilder(content);
		sb.append("<br>本文转自：");
		sb.append("<a href='");
		sb.append(srcUrl);
		sb.append("'>");
		sb.append(host);
		sb.append("</a>");
    	return sb.toString();
	}
	

}

