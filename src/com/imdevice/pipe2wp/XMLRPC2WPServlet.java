package com.imdevice.pipe2wp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;



@SuppressWarnings("serial")
public class XMLRPC2WPServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		new XmlRPCHandler().test();if(true)return;
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://imdevice.com/wordpress/xmlrpc.php"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Date pubDate=new Date();
        pubDate.setTime(pubDate.getTime()+1000*60*2);//publish it in 2mins later
        System.out.println(pubDate);
	Map<Object, Object> post = new HashMap<Object, Object>();
	post.put("title", "测试帖：Hello, Blog!");
	post.put("dateCreated", pubDate);
	post.put("mt_excerpt", "摘要excerpt");
	post.put("description", "This is the content of a trivial post.xmlrpc测试帖<br><img alt=\"富士通云计算参考架构\" src=\"http://i2.sinaimg.cn/IT/it/2011-04-11/U2707P2T1D5390629F13DT20110411121713.JPG\">");

	Object[] params = new Object[]{"1", "lcw", "Hncwx123", post, Boolean.TRUE};

	String result;
	try {
		result = (String) client.execute("metaWeblog.newPost", params);
		System.out.println(" 通过xmlrpc发布 postid= " + result);
	} catch (XmlRpcException e) {
		e.printStackTrace();
	}
	}
	


	private String URLFetch(String url){
		StringBuffer sb=new StringBuffer();
		try {
			URL Url = new URL(url);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(Url.openStream(),"GBK"));//"GBK" should set to the source's encode
        String line;boolean flag=false;
        while ((line = reader.readLine()) != null) {
        	if(line.indexOf("publish_helper_end")>-1){
        		flag=false;break;
        	}
        	if(line.indexOf("publish_helper")>-1){
        		flag=true;continue;
        	}
        	if(flag)sb.append(line);
        }
        reader.close();        
	} catch (MalformedURLException e) {
        // ...
    } catch (IOException e) {
        // ...
    }
    return sb.toString();
	}
	
}
