package com.imdevice.pipe2wp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

        Pattern p1 = Pattern.compile("f(.+?)i(.+?)h"); 
        Matcher m1 = p1.matcher("finishabigfishfrish"); 
        while (m1.find()) { 
                String s0 = m1.group(); 
                String s1 = m1.group(1); 
                String s2 = m1.group(2); 
                System.out.println(s0 + "||" + s1 + "||" + s2); 
        } 

	}

}
