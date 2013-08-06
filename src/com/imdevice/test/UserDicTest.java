package com.imdevice.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.imdevice.pipe2wp.Post;
import com.imdevice.pipe2wp.XmlRPCHandler;

@SuppressWarnings("serial")
public class UserDicTest extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		String txt=req.getParameter("words");
		if(txt==null||txt.length()==0)
		txt="google play和app store及Moto X相机应用泄露 下来玩玩吧。";
		o.println("<form>");
		o.println("<p><textarea name='words' cols='45' rows='8' >"+txt+"</textarea></p>");
		o.println("<p><input name=submit type=submit id=submit value='Update'></p>");
		o.println("</form>");
		o.println("<hr/>");
		List<Term> terms = ToAnalysis.parse(txt);
		o.println(terms);
		o.flush();
		o.close();
		
	}
	

}

