package com.imdevice.pipe2wp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imdevice.WebSpider.Extractor;

public class ClearReader extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/html;charset=UTF-8");
		
		PrintWriter o=resp.getWriter();
		o.println("<html>");
		o.println("<head>");
		o.println(" <link rel='stylesheet' href='style.css' type='text/css' media='screen' />");
		o.println("<script src=\"tinymce/tinymce.min.js\"></script>");
		o.println("<script>tinymce.init({selector:'textarea',height:'600',");
		o.println("			plugins: [");
		o.println("			          'advlist autolink lists link image charmap print preview anchor',");
		o.println("			          'searchreplace visualblocks code fullscreen',");
		o.println("			          'insertdatetime media table contextmenu paste '");
		o.println("			      ],");
		o.println("			toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image'");
		o.println("		});");
		o.println("</script>");
		o.println("<script>function save(){document.getElementById('postform').submit();}");
		o.println("</script>");
		o.println("</head>");
		o.println("<body><div class='wrapper'>");
		
		o.println("<div class='headerform'>");
		o.println("<h1>Read it Clearly</h1>");
		o.println("<form action='clearreader' method='get' id='urlform'>");
		o.println("<input type='text' placeholder='请输入需要阅读的网址' id='url' name='url' value='' />");
		o.println("<input type='submit' id='submit' value='&raquo;' />");
		o.println("</form>");
		o.println("</div>");
		
		String url=req.getParameter("url");
		boolean debug=false;
		if(null!=url){
			String debugPara=req.getParameter("debug");
			if(null!=debugPara){
				if(debugPara.toLowerCase().equals("true"))debug=true;
			}
			Extractor extractor=new Extractor(url);
			if(debug)extractor.debug=debug;
			extractor.extract();
			/*Post post=new Post();
			post.setTitle(extractor.getTitle());
			post.setDescription(extractor.getContent());
			req.getSession(true).setAttribute("post"+this.hashCode(), post);*/
			o.println("<form method='post' id='postform' action='post2wp'>");
			o.println("<input type='text' id='title' name='title' value='"+extractor.getTitle()+"' style='width: 100%; padding: 3px 8px;  font-size: 1.7em;  line-height: 100%;'/>");
			o.println("<textarea id='content' name='content'>");
			//o.println("<div class='container'>");
			//if(!debug)	o.println("<h2>"+extractor.getTitle()+"</h2>");
			o.println(extractor.getContent());
			//o.println("</div>");    post2wp?pid="+this.hashCode()+"
			o.println("</textarea>");
			o.println("<div class='post2wplink' id='submitbtn'><a href='javascript:save()'>POST</a></div>");
			o.println("</form>");
		}
		o.println("</div></body></html>");
        o.flush();
        o.close();
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
}
