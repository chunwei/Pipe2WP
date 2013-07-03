package com.imdevice.pipe2wp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class QueueServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		//Option lastFetch=new Option("lastFetch");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Option lastFetch;
		Option lastPub;
		try {
			lastFetch=pm.getObjectById(Option.class, "lastFetch");
			System.out.println("get lastFetch:"+lastFetch.getDateValue());
		}catch(JDOObjectNotFoundException e){
			lastFetch=new Option("lastFetch");
			pm.makePersistent(lastFetch);
			System.out.println("get lastFetch:"+lastFetch.getDateValue());
	    }
		try {
			lastPub=pm.getObjectById(Option.class, "lastPub");
			System.out.println("get lastPub:"+lastPub.getDateValue());
		}catch(JDOObjectNotFoundException e){
			lastPub=new Option("lastPub");
			pm.makePersistent(lastPub);
			System.out.println("new lastPub:"+lastPub.getDateValue());
	    } finally {
	        pm.close();
	    }
	    
		    //ServletContext session=this.getServletContext();
		    //HttpSession session = req.getSession(true);

		try {
			URL url = new URL("http://cnbeta.com/backend.php");

			InputSource is = new InputSource(url.openStream());
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			TaskQueueSAXHandler myHandler = new TaskQueueSAXHandler();
			// myHandler.init(session.getAttribute("lastFetch"),session.getAttribute("lastPub"));
			myHandler.init(lastFetch.getDateValue(), lastPub.getDateValue());
			parser.parse(is, myHandler);
			try {
				pm = PMF.get().getPersistenceManager();
				lastFetch.setDateValue(myHandler.getLastFetch());
				lastPub.setDateValue(myHandler.getLastPub());
				pm.makePersistent(lastFetch);
				pm.makePersistent(lastPub);
			} finally {
				pm.close();
			}
			// session.setAttribute("lastFetch", myHandler.getLastFetch());
			// session.setAttribute("lastPub", myHandler.getLastPub());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showSysInfo(){
		Map m = System.getenv();

		for ( Iterator it = m.keySet().iterator(); it.hasNext(); )

		{

		String key = (String ) it.next();

		String value = (String ) m.get(key);

		System.out.println(key +":" +value);

		}

		System.out.println( "--------------------------------------" );

		Properties p = System.getProperties();

		for ( Iterator it = p.keySet().iterator(); it.hasNext(); )

		{

		String key = (String ) it.next();

		String value = (String ) p.get(key);

		System.out.println(key +":" +value);

		}
	}
	

}

