package com.Common;

/**
 * 存放一些全局变量
 * 
 * @author MuTou_TQ
 */
public class Constants {
	    // Login
		public static String NET_ERROR = "无网络链接！";
		public static String APPKEY = "14c16a2115677";    
	    public static String APPSECRETE = "f1c5fb59ff599fa2527160a9e5f756c7"; 
	    //Share
	    /** 友盟分享 **/
	    public static final String DESCRIPTOR = "com.umeng.share";
	    /** 友盟登录 **/
	    public static final String UMENGLOGIN = "com.umeng.login";
	    
	    //即时通信
	    public static final String MEMBER_ID = getPrefixConstant("member_id");

	    private static String getPrefixConstant(String str) {
	        return  str;
	      }
		
}
