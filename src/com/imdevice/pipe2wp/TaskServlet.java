package com.imdevice.pipe2wp;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class TaskServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		System.out.println("a="+req.getParameter("a")+"; b="+req.getParameter("b"));
		
	}
	

}

