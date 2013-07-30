package com.imdevice.WebSpider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class TheVergeDic {

	public static void main(String[] args) {
		//文件生成路径  
        PrintStream ps;
		try {
			ps = new PrintStream("theverge2.txt");
			System.setOut(ps); 
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}  
		
		String url="http://www.theverge.com/products/categories/laptops/6/29";
		try {
			Document doc=Jsoup.connect(url).timeout(300000).get();
			Elements categoryList=doc.getElementsByClass("plist-category");
			Elements categories=categoryList.first().children();
			int i=0;
			for(Element c:categories){
				if(i<4){ i++;continue;}
				Element a=c.children().first();
				String cate_1st_page=a.attr("abs:href");
				String name=a.text();
				System.out.println("#"+name);
				getProducts(cate_1st_page);
				//System.out.println("#"+name);
				//getProducts(url);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void getProducts(String url){
		try {
			System.out.println("#"+url);
			Document doc=Jsoup.connect(url).timeout(300000).get();
			Elements products=doc.getElementsByClass("product-grid-item");
			for(Element p:products){
				System.out.println(p.text());
			}
			String next_page=doc.getElementsByClass("next_page").first().attr("abs:href");
			if(!next_page.isEmpty())getProducts(next_page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
