package com.imdevice.pipe2wp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

/**
 * @author luchunwei
 *
 */
@Entity
public class XmlRPCProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	private String xmlrpcurl;
	private String blogid;
	private String username;
	private String password;
	private String methodname;
	
	
	public XmlRPCProperties(){
		init();
	}
	public XmlRPCProperties(String xmlrpcurl){
		init();
		this.setXmlrpcurl(xmlrpcurl);
	}
	
	private void init(){
		setXmlrpcurl("http://120.26.88.102/xmlrpc.php");
		setBlogid("1");
		setUsername("admin");
		setPassword("admin");
		setMethodname("metaWeblog.newPost");
	}
	public Key getKey() {
        return key;
    }
	public String getXmlrpcurl() {
		return xmlrpcurl;
	}
	public void setXmlrpcurl(String xmlrpcurl) {
		this.xmlrpcurl = xmlrpcurl;
	}
	public String getBlogid() {
		return blogid;
	}
	public void setBlogid(String blogid) {
		this.blogid = blogid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	@Override
	public String toString(){
		return xmlrpcurl+" -  "+methodname;
	}
}
