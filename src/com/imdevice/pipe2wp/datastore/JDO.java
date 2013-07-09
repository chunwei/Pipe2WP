/**
 * 
 */
package com.imdevice.pipe2wp.datastore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imdevice.pipe2wp.PMF;

/**
 * @author lcw601474
 *	This class demonstrates how to use JDO to access the Google AppEngine Datastore.
 */
@SuppressWarnings("serial")
public class JDO extends HttpServlet { 

	/**
	 * @param args
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		
		ContactInfo contInfo=new ContactInfo("Ningbo", "Zhejiang","315800");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(contInfo);
			
			Query q=pm.newQuery(ContactInfo.class);
			try{
				@SuppressWarnings("unchecked")
				List<ContactInfo> results=(List<ContactInfo>) q.execute();
				if(!results.isEmpty()){
					for(ContactInfo info:results){
						o.print("<p>");
						o.print(info.getKey()+" : ");
						o.print(info);
						o.print("</p>");
					}
				}else{
					o.print("No result!");
				}
			}finally{
				q.closeAll();			
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pm.close();			
		}
	}

}
