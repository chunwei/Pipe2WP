package com.imdevice.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CnbetaAPI {

/*	
 * 文章列表api

	http://www.cnbeta.com/capi?app_key=10000&format=json&method=Article.Lists&timestamp=1415341263&v=1.0&sign=eae5d864a88a9d66c2f375277898d74d

	对应文章内容api:

	http://www.cnbeta.com/capi?app_key=10000&format=json&method=Article.NewsContent&sid=344207&timestamp=1415342012&v=1.0&sign=b5840175087bff146e909149071dd0bb

	对应文章评论api:

	http://www.cnbeta.com/capi?app_key=10000&format=json&method=Article.Comment&page=1&sid=344205&timestamp=1415342285&v=1.0&sign=fe84bdefb0df5cc4817c9e1b648c677c
*/

	public static void main(String[] args) {
		String baseUrl="http://api.cnbeta.com/capi?";
		String str1 = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("app_key=10000")).append("&format=json").toString())).append("&method=Article.NavList").toString())).append("&timestamp=").append(g()).toString() + "&v=1.0";
	    String str2 = str1 + "&sign=" + sign(new StringBuilder(String.valueOf(str1)).append("&mpuffgvbvbttn3Rc").toString());
	    System.out.println(baseUrl+str2);
	}
	
	  private static final char[] a = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
	  
	  public static String sign(String paramString)
	  {
	    try
	    {
	      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
	      localMessageDigest.update(paramString.getBytes());
	      byte[] arrayOfByte = localMessageDigest.digest();
	      StringBuilder localStringBuilder = new StringBuilder(2 * arrayOfByte.length);
	      for (int i = 0;; i++)
	      {
	        if (i >= arrayOfByte.length) {
	          return localStringBuilder.toString().toLowerCase();
	        }
	        localStringBuilder.append(a[((0xF0 & arrayOfByte[i]) >>> 4)]);
	        localStringBuilder.append(a[(0xF & arrayOfByte[i])]);
	      }
	    }
	    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
	    {
	      localNoSuchAlgorithmException.printStackTrace();
	      return "";
	    }
	  }
	  
	  public static String g()
	  {
	    return Long.valueOf(System.currentTimeMillis() / 1000L).toString();
	  }

}
