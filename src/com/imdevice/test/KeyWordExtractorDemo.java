package com.imdevice.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.imdevice.WebSpider.Extractor;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class KeyWordExtractorDemo {
    public static void main(String[] args) {
        KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        String keywords=result.toString();
        if(keywords.length()>2)keywords=keywords.substring(1,keywords.length()-1);
        System.out.println(keywords);
        System.out.println(result);
        /*        String url=//"http://www.ifanr.com/323342";
        		"http://www.36kr.com/p/204969.html";
		URL u;
		try {
			u = new URL(url);
			String host=u.getHost();
			System.out.println(host);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Extractor extrator=new Extractor(url);
		extrator.debug=false;
		extrator.extract();
		String title=extrator.getTitle();
		String content =extrator.getContentText();
		//String title="App Store 与 Google Play 有趣的对比数字";
		//String content="这种 PK 我们见过很多次了： Google Play 应用数量超过 100 万款，高于苹果的 90 万款；Google Play 商店应用下载数量达到 500 亿次，已经与 App Store 齐平。 但实际上，Google Play 与 App Store 是相当不同的两个应用商店，还有其他有趣的对比维度。移动云服务公司 Kinvey 就做出了这样的一组信息图，坚持从“有趣”的角度来审视这两个应用商店。 App Store 每天挣的钱很多 过去两年 Android 在全球范围内攻城掠地，目前 Google Play 在“应用商店”中的份额已经高达 74.4%，苹果 App Store 则只有 18.2%。其他的市场份额归属 Windows Phone 和黑莓 Blackberry World。 不过虽然 Google Play 获得了绝大多数市场份额，但它每天挣的钱却只有 App Store 的五分之一。App Store 每天获得 510 万美元收入，Google Play 则只有 110 万美元。 Google Play 的“小工具”很受欢迎 由于 Android 的开放性——当然也由于它的不完善性，个性化的小定制工具在 Google Play 很受欢迎。它是仅次于“游戏和娱乐”的 App 种类，有 12% 为这种小工具。 App Store 与 Google Play 有两个共通点：前三名都有“游戏与娱乐”“书籍与图书”两个品类，而 App Store 上第二名的位置上不是“小工具”，而是“教育”，似乎看起来 iOS 设备持有者更好学，而 Android 设备持有者更富极客精神更爱折腾？ 顶级应用出版商：App Store 完爆 Google Play 该图表综合了出版商所开发应用的下载量和总共开发应用的数量。就下载数来看，毫无疑问，苹果与 Google 官方出品的应用是下载很高的，他们自己就是 TOP 1 的应用出版商。 但是论开发应用数量多少，App Store 前五名出版商远远比 Google Play 优质：Gameloft 在 App Store 上架了 266 款应用，排在后面的是迪斯尼（144 款）、EA（126 款）、Google（23 款）。 Google Paly 开发应用数量最多的是：Go Dev（135 款），Google（60 款）、Adobe（31 款）。这与图二相符，即 Android 用户很乐意折腾自己的手机。而且这也可以看出，Android 是 Google 服务的重要载体，承载了 Google 在移动平台上的努力。 Google Play 的应用有很多种定价方式 在这份调查中，考察的是 TOP100 应用的定价方式。在 Google Play 前 100 名应用中，共有 29 个不同的定价档次，而 App Store 的前 100 名只有 7 种不同定价方式。 原因也很容易理解，即 App Store 基本是以“.99”结尾的价格，一美元内只有一种定价方式。而 Google Play 则没有这样的限制，所以他们的定价方式“百花齐放”。 “最贵应用”都在 App Store App Store 的“最贵应用”高达 999.99 美元，Google Play 最贵应用价格是 200 美元。 从前五个最贵应用来看，App Store 比 Google Play 更“金贵”——它们的定价在 399-999 美元不等。而 Google Play 前五个最贵应用，价格比较均匀地分布在 159-200 美元区间内。 谁在卖“最贵应用”，请看下图： Google Play 比 App Store 更有活力 在这组统计中，新应用所占的 TOP 300 总收入的比重，Google Play 的份额将近 20%，但 App Store 的比例不到 15%。这说明新应用在 App Store 没有那么好赚钱。 而新加入的出版商数量在总出版商中的比重，Google Play 与 App Store 相当，这意味着随着 Android 用户基数的加大，很多开发者在两个平台同时发布产品，至少 Google Play 摆脱了被开发者忽略的命运。 Google Play 保持着比较好的活力，加上新加入者能获得不错的报酬，而不是像 App Store 一样收入多被才出版商垄断，Google Play 的应用商店未来不错。这比它的单纯应用数量、应用下载次数超过 App Store 更有意义。教育的未来在孩子，应用商店的未来在新创业者。 题图来自dascloud 以下为完整信息图（比文字解读信息更加丰富）：";
        */
/*		try {
			String url = "http://www.leiphone.com/feed";
			 //url="http://www.36kr.com/feed";
			 //url="http://www.ifanr.com/feed";
			//url="http://www.cnbeta.com/backend.php";
			 //url="http://www.theverge.com/rss/index.xml";
			URL feedUrl = new URL(url);

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			System.out.println(feed.getTitle());

			@SuppressWarnings("unchecked")
			List<SyndEntry> entries = feed.getEntries();
			if (entries != null && !entries.isEmpty()) {
				for (SyndEntry entry : entries) {
					title=entry.getTitle();
					Extractor extractor=new Extractor();
					extractor.setUrl(entry.getLink());
	            	extractor.extract();
					content=extractor.getContentText();
					
					Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
					System.out.println(result);
					System.out.println(title);
					System.out.println(content);
					
					List<Term> terms = ToAnalysis.parse(title);
					new NatureRecognition(terms).recognition() ;
					System.out.println( terms);
					terms = ToAnalysis.parse(content);
					new NatureRecognition(terms).recognition() ;
					System.out.println( terms);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: " + ex.getMessage());
		}*/
		
    }
}
