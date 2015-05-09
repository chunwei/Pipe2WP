package com.imdevice.WebSpider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexTest {
		/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s="其位于美国德克萨斯州奥斯丁的企业园区，希望通过此举来筹措资金进军 PC 以外的市场。据悉该园区的面积为 58 英亩，AMD 计划在 2013 年第二季完成交易。不知道在拿到这笔钱后 AMD 会将其投向哪个行业，总之希望他们能找到其它有助于其挨过 PC 业";
		
		String punctuation="，、。：；！？‘’“”,;!\'\"";
    	String regex="["+punctuation+"][^"+punctuation+"]{5,}["+punctuation+"]";
    	Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
        }
    	Matcher m = pattern.matcher(s);
    		System.out.println(m.find());
    	int t=3;int c=t;int p=t+1;
    	while(p<4){
    		c=p;
    		p=p+1;
    		System.out.println("c="+c);
    	}
    	System.out.println("result c="+c);

	}

}
