/**
 * 
 */
package com.imdevice.pipe2wp.datastore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.imdevice.pipe2wp.EMF;
import com.imdevice.pipe2wp.Subscribe;
import com.imdevice.pipe2wp.XmlRPCProperties;

/**
 * @author lcw601474
 *	This class demonstrates how to use JPA to access the Google AppEngine Datastore.
 */
@SuppressWarnings("serial")
public class JPA extends HttpServlet { 

	/**
	 * @param args
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		o.print("add Employee | XmlRPCProperties ?");
		
		String add=req.getParameter("add");
		o.print("add "+add);
		Date hireDate=new Date();
		Employee employee=new Employee("Chunwei", "Lu",hireDate);
		XmlRPCProperties properties=new XmlRPCProperties();
		//String link="http://www.36kr.com/feed";
		//Subscribe sub=new Subscribe(link);

		Subscribe sub1=new Subscribe("test-");

		Date init=new Date();
		init.setTime(init.getTime()-24*60*60*1000);
		//sub.setLastFetchDate(init);
		//sub.setLastPubDate(init);
		EntityManager em = EMF.get().createEntityManager();
		try{
			if(add!=null){
				em.getTransaction().begin();
				if(add.equalsIgnoreCase("Employee")){
					em.persist(employee);
				}else{
					em.persist(properties);
				}
				em.getTransaction().commit();//如果不commit，下面的查询看不到这条记录
			}
			//em.getTransaction().begin();
			//em.persist(sub);
			//em.getTransaction().commit();//如果不commit，下面的查询看不到这条记录

			TypedQuery<Employee> q = em.createQuery("SELECT e FROM Employee e",Employee.class);
			List<Employee> employees=q.getResultList();
			if(!employees.isEmpty()){
				for(Employee employee1:employees){	
					o.print("<p>");
					o.print(employee1.getKey()+" : ");
					o.print(employee1);
					o.print("</p>");
				}
			}else{
				o.print("No result!");
			}
			TypedQuery<Subscribe> q1 = em.createQuery("SELECT e FROM Subscribe e",Subscribe.class);
			List<Subscribe> subscribes=q1.getResultList();
			if(!subscribes.isEmpty()){
				for(Subscribe subscribe:subscribes){
					if(subscribe.getUid()==null){
						em.getTransaction().begin();
						subscribe.setUid("1");
						em.getTransaction().commit();
					}
					o.print("<hr>");
					o.print("uid="+subscribe.getUid()+"  link: "+subscribe.getLink()+"  lastPub:  "+subscribe.getLastPubDate()+"  lastFetch:  "+subscribe.getLastFetchDate());
				}
			}
			
/*			em.getTransaction().begin();
			em.persist(sub1);
			em.getTransaction().commit();
			Subscribe sub2=em.find(Subscribe.class,sub1.getKey());
			em.getTransaction().begin();
			//sub2.setLink(sub2.getLink().substring(0, 5)+init.toString());
			sub2.setLink("http://www.ifanr.com/feed");
			sub2.setUid("2");
			em.getTransaction().commit();
			*/
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();			
		}
	}

}
