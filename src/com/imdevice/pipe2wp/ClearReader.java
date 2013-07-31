package com.imdevice.pipe2wp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

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
			String title=extractor.getTitle();
			String content=extractor.getContentText();
			String contentHtml=extractor.getContent();
			/*Post post=new Post();
			post.setTitle(extractor.getTitle());
			post.setDescription(extractor.getContent());
			req.getSession(true).setAttribute("post"+this.hashCode(), post);*/
			o.println("<form method='post' id='postform' action='post2wp'>");
			o.println("<input type='text' id='title' name='title' value='"+title+"' style='width: 100%; padding: 3px 8px;  font-size: 1.7em;  line-height: 100%;'/>");
			o.println("<textarea id='content' name='content'>");
			//o.println("<div class='container'>");
			//if(!debug)	o.println("<h2>"+extractor.getTitle()+"</h2>");
			o.println(contentHtml);
			//o.println("</div>");    post2wp?pid="+this.hashCode()+"
			o.println("</textarea>");
			o.println("<div class='post2wplink' id='submitbtn'><a href='javascript:save()'>POST</a></div>");
			o.println("</form>");
			
			KeyWordComputer kwc = new KeyWordComputer(5);
			Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
			o.println("<div class='keywords-extractor'>");
			o.println(result);
			o.println("<hr>");
			o.println(title);
			o.println("<hr>");
			o.println(content);
			o.println("<hr>");
			
			List<Term> terms = NlpAnalysis.parse(title);
			new NatureRecognition(terms).recognition() ;
			o.println( terms);
			o.println("<hr>");
			terms = NlpAnalysis.parse(content);
			new NatureRecognition(terms).recognition() ;
			o.println( terms);
			o.println("</div>");
		}else{//sample test
			o.println("<hr>");
			 KeyWordComputer kwc = new KeyWordComputer(5);
		        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
		        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
		        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
				o.println(result);
			o.println("</div></body></html>");
		}
        o.flush();
        o.close();
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req,resp);
	}
}
