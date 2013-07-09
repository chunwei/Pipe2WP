/**
 * 
 */
package com.imdevice.pipe2wp.datastore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

/**
 * @author lcw601474
 *	This class demonstrates how to use low-level Java API to access the Google AppEngine Datastore.
 */
@SuppressWarnings("serial")
public class LowLevelAPI extends HttpServlet { 

	/**
	 * @param args
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		
		Entity employee=new Entity("Employee");
		employee.setProperty("firstName", "Chunwei");
		employee.setProperty("lastName", "Lu");
		Date hireDate=new Date();
		employee.setProperty("hireDate", hireDate);
		ds.put(employee);
		
		try {
			Entity retrieved=ds.get(employee.getKey());
			Map<String,Object> props=retrieved.getProperties();
			Set<String> propNames=props.keySet();
			for(String propName:propNames){				
				o.println("<p>"+propName+" : "+props.get(propName)+"</p>");
			}
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
