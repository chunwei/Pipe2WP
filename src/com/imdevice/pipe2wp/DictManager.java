package com.imdevice.pipe2wp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.library.UserDefineLibrary;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class DictManager extends HttpServlet {
	private String url="/dicManager";
	public void doPost(HttpServletRequest req, HttpServletResponse resp)  throws IOException {
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)  throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter o=resp.getWriter();
		String action=req.getParameter("action");
		o.println(actionLink("create","Create New"));
		o.println(actionLink("all","Query All"));
		o.println(actionLink("load","Dynamic Load"));
		if(null==action){
			syncTags();
		}else if("loadUserDict".equals(action)){
			this.loadUserDict();
		}else if("create".equals(action)){
			String name="newdic";
			String rname=req.getParameter("name");
			if(rname!=null&&rname.length()>0)name=rname;
			String rwords=req.getParameter("words");
			if(rwords==null||rwords.length()==0)
				rwords="google glass;moto x";
			Text words=new Text(rwords);
			createDic(name,words);	
			o.println("create new Dic :"+name);
			o.println(queryAllDic());	
		}else if("update".equals(action)){
			String key=req.getParameter("key");
			String name=req.getParameter("name");
			Text words=new Text(req.getParameter("words"));
			updateDic(key,name,words);	
			o.println("update Dic :"+name);
			o.println(queryAllDic());	
		}else if("all".equals(action)){
			o.println(queryAllDic());			
		}else if("edit".equals(action)){
			String key=req.getParameter("key");	
			o.println(edit(key));
		}else if("load".equals(action)){
			o.println(dynamicLoad());			
		}else{
			o.println("Choose an action above.");	
		}
		o.flush();
        o.close();		
	}
	public String actionLink(String url,String action,String title){
		return "<a href='"+url+"?action="+action+"'>"+title+"</a>";
	}
	public String actionLink(String action,String title){
		return actionLink(url,action,title);
	}
	public void createDic(String name,Text words){
		EntityManager em = EMF.get().createEntityManager();
		try{
			UserDefinedDict uDict=new UserDefinedDict(name);
			uDict.setWords(words);
			em.getTransaction().begin();
			em.persist(uDict);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();			
		}
	}
	public void updateDic(String keyString,String name,Text words){
		EntityManager em = EMF.get().createEntityManager();
		try{
			Key key=KeyFactory.stringToKey(keyString);
			UserDefinedDict dict=em.find(UserDefinedDict.class,key);
			em.getTransaction().begin();
			dict.setName(name);
			dict.setWords(words);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();			
		}
	}
	public String edit(String keyString){
		StringBuilder sb=new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try{
			Key key=KeyFactory.stringToKey(keyString);
			UserDefinedDict dict=em.find(UserDefinedDict.class,key);
			sb.append("<form method='post' action='"+url+"'>");
			sb.append("<input type='hidden' name='key' value='");
			sb.append(KeyFactory.keyToString(dict.getKey()));
			sb.append("'>");
			sb.append("<input type='hidden' name='action' value='update'>");
			sb.append("<p><input type='text' name='name' value='");
			sb.append(dict.getName());
			sb.append("'></p>");
			sb.append("<p><textarea name='words' cols='45' rows='8' >");			
			sb.append(dict.getWords().getValue());
			sb.append("</textarea></p>");
			sb.append("<p><input name=submit type=submit id=submit value='Update'></p>");
			sb.append("</form>");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();			
		}
		return sb.toString();
		}
	public String queryAllDic(){
		StringBuilder sb=new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try{
		TypedQuery<UserDefinedDict> q = em.createQuery("SELECT e FROM UserDefinedDict e",UserDefinedDict.class);
		List<UserDefinedDict> dicts=q.getResultList();
		if(!dicts.isEmpty()){
			for(UserDefinedDict dict:dicts){
				sb.append("<p>");
				sb.append(dict.getName()+ actionLink("edit&key="+KeyFactory.keyToString(dict.getKey()),"Edit"));
				sb.append("</p>");
				sb.append("<p>");
				sb.append(dict.getWords().getValue());
				sb.append("</p>");
				getServletContext().setAttribute("userDefinedDict-"+dict.getName(), dict.getWords().getValue());
			}
		}else{
			sb.append("No result!");
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();	
		}
		return sb.toString();
	}
	
	public void loadUserDict() {
		if(getServletContext().getAttribute("userDefinedDict-techWords")==null){
			queryAllDic();
		}
		Enumeration<?> attrNames=getServletContext().getAttributeNames();
		while(attrNames.hasMoreElements()){
			String attrName=(String)attrNames.nextElement();
			if(attrName.startsWith("userDefinedDict-")){
				String wordT=(String)getServletContext().getAttribute(attrName);
				String[] words=wordT.split(";");
				for(String word:words){
					UserDefineLibrary.insertWord(word, "n", 10);					
				}
			}
		}
	}
	
	public String dynamicLoad(){
		StringBuilder sb=new StringBuilder();
		EntityManager em = EMF.get().createEntityManager();
		try{
		TypedQuery<UserDefinedDict> q = em.createQuery("SELECT e FROM UserDefinedDict e",UserDefinedDict.class);
		List<UserDefinedDict> dicts=q.getResultList();
		if(!dicts.isEmpty()){
			for(UserDefinedDict dict:dicts){
				sb.append("<p>");
				sb.append(dict.getName()+" : ");
				sb.append("</p>");
				sb.append("<p>");
				String wordT=dict.getWords().getValue();
				sb.append(wordT);
				sb.append("</p>");
				String[] words=wordT.split(";");
				for(String word:words){
					UserDefineLibrary.insertWord(word, "n", 1);					
				}
			}
		}else{
			sb.append("No result!");
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();			
		}
		return sb.toString();
	}
	
	public String getAllTags(String url){
		StringBuffer sb = new StringBuffer();
		try {
			URL Url = new URL(url);

			BufferedReader reader = new BufferedReader(new InputStreamReader(Url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public void syncTags(){
		String allTags=getAllTags("http://www.imdevice.com/alltags/");
		if(allTags!=null&&allTags.length()>0){
			String[] words=allTags.split(";");
			for(String word:words){
				UserDefineLibrary.insertWord(word, "n", 1);					
			}
		}
	}
}

