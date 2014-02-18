package com.imdevice.pipe2wp.task;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.library.UserDefineLibrary;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.imdevice.WebSpider.Extractor;
import com.imdevice.pipe2wp.UserDefinedDict;

@SuppressWarnings("serial")
public class PageWasher extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req,resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String dirtypage=req.getParameter("dirtypage");
		String title=req.getParameter("title");
		String keywords=req.getParameter("keywords");
		System.out.println("PageWash: "+req.getParameter("link"));
		System.out.println("----------------------------------------------------------");
		System.out.println( req.getParameter("title"));
		Extractor extractor=new Extractor();
		extractor.setTitle(title);
		extractor.setUrl(req.getParameter("link"));
		//extractor.setIgnoreSporadic(true);
		String clearPage=extractor.getContent(dirtypage);
		String content=extractor.getContentText();
		String first_image_url=extractor.getFirstImage();
		//太耗资源
/*		if(null==keywords || keywords.length()<2){
			KeyWordComputer kwc = new KeyWordComputer(10);
			Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
			keywords=result.toString();
			//"脱掉[]"
			if(keywords.length()>2)keywords=keywords.substring(1,keywords.length()-1);
		}*/
		
		//System.out.println(clearPage);
		try{
			Queue queue=QueueFactory.getQueue("PostQueue");
			queue.add(withUrl("/tasks/post2wp")
					.param("a_id", req.getParameter("a_id"))
					.param("link", req.getParameter("link"))
					.param("title", req.getParameter("title"))
					.param("publish_date", req.getParameter("publish_date"))
					.param("first_image_url", first_image_url)
					.param("keywords", keywords)
					//.param("mt_excerpt", req.getParameter("mt_excerpt"))
					.param("description", clearPage)
					.method(Method.POST)//POST是默认值，传递长参数时最好不要用'GET'，因为'GET' url最大长度有限制，而且各浏览器和服务器软件支持不一致
					);			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}

