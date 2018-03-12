package com.mogu.app;

/**
 * 存放Mr Li's请求地址
 * 
 */
public class MrLiUrl {

	public static final String BASE_URL = Url.IP_ADDRESS + Url.PORT + Url.URL_SPLITTER;


	/**
	 * 新闻列表
	 */
	public static final String URL_News = BASE_URL
			+ "NewsList.action";
	/**
	 * 据点升级
	 */
	public static final String URL_JuDianUpDate = BASE_URL
			+ "User/MyWifiUpdate.action";
	/**
	 * 可添加好友列表
	 */
	public static final String URL_Wifi_friends_list = BASE_URL
			+ "User/ListFriendsInTheWifi.action";
	/**
	 * 打招呼
	 */
	public static final String URL_SAY_HELLO = BASE_URL
			+ "User/SendChatMsg.action";
	/**
	 * 处理打招呼
	 */
	public static final String URL_HANDLER_HELLO = BASE_URL
			+ "User/ReceiveChatMsg.action";
	/**
	 * 游客分享wifi
	 */
	public static final String URL_VISITER_SHARE_WIFI = BASE_URL
			+ "VisitorAddWifi.action";
	/**
	 * QQ登陆
	 */
	public static final String URL_QQ_LOGIN = BASE_URL
			+ "LogonByOthers.action";
	/**
	 * 上传身份证
	 */
	public static final String URL_UP_IDENTITY = BASE_URL
			+ "User/AuthPicUpload.action";
	/**
	 * get token
	 */
	public static final String URL_GET_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token"
			;
	/**
	 * get LABLE
	 */
	public static final String URL_GET_LABLE = BASE_URL +"User/IndustryCategoryList.action";
}
