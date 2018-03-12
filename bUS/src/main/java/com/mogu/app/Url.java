package com.mogu.app;

/**
 * 存放一些请求地址
 * 
 */
public class Url {
	// public static final String IP_ADDRESS = "http://115.159.123.91";
	// public static final String IP_ADDRESS = "http://192.168.0.200";
	public static final String IP_ADDRESS = ImageBaseUrl.IP_ADDRESS;

	// public static final String PORT = ":8020";
	public static final String PORT = ImageBaseUrl.PORT;

	public final static String URL_SPLITTER = "/";

	public static final String BASE_IMAGE_URL = IP_ADDRESS + PORT;

	public static final String BASE_URL = IP_ADDRESS + PORT + URL_SPLITTER;
	// 注册
	public static final String REGIST_URL = BASE_URL + "RegisterSJHM.action";
	// 登陆
	public static final String LOGIN_URL = BASE_URL + "LogonSJHM.action";
	// 退出登陆
	public static final String LOGOUT_URL = BASE_URL + "User/LogoutSJHM.action";
	// 会员上传头像
	public static final String UPDATE_AVATAR = BASE_URL
			+ "User/AddUserTxiang.action";
	// 获取当前会员信息
	public static final String GET_USER_INFO = BASE_URL
			+ "User/GetUserInfo.action";
	// 发送注册验证码
	public static final String YZM_URL = BASE_URL + "SendSJHM.action";
	// FindSJHMcode.action验证验证码
	public static final String YZyzm_URL = BASE_URL + "FindSJHMcode.action";
	// 意见反馈
	public static final String FeedB_URL = BASE_URL
			+ "User/AddYiJianFanKui.action";
	// 根据上传的WIFI列表判断有哪些已被分享
	public static final String WIFI_LIST = BASE_URL + "GetShareWifiList.action";

	// 附近wifi列表
	public static final String NearbyWFList_URL = BASE_URL
			+ "GetWifiList.action";
	// 分享wifi
	public static final String Share_wifi_URL = BASE_URL
			+ "User/UserAddWifi.action";
	// 获取安卓APP版本号
	public static final String APP_UPDATE_URL = BASE_URL
			+ "GetAndroidVersion.action";

	// 会员信息
	public static final String MyInfo_URL = BASE_URL
			+ "User/GetUserInfo.action";
	// 更新会员信息
	public static final String UpMyinfo_URL = BASE_URL
			+ "User/UpdateGeRenZiLiao.action";
	// 我分享的和被占领
	public static final String MyShareWifi_URL = BASE_URL
			+ "User/MyShareWifi.action";
	// 更新会员密码
	public static final String UPDATE_PASSWORD = BASE_URL
			+ "User/UpdateHuiYuanMiMa.action";

	// 我分享的和被占领
	public static final String RESET_PASSWORD = BASE_URL
			+ "ResetPassword.action";
	// 会员连接WIFI
	public static final String LINK_WIFI = BASE_URL
			+ "User/UserLinkWifi.action";
	// 会员获取每日任务列表
	public static final String Mission_List = BASE_URL
			+ "User/UserGetMrrwList.action";
	// 会员领取奖励
	public static final String Mission_jl = BASE_URL
			+ "User/UserGetMrrwjl.action";
	// 会员签到
	public static final String Sign_url = BASE_URL + "User/UserSignIn.action";
	// 成就任务
	public static final String Chengjiu_url = BASE_URL
			+ "User/UserGetCjList.action";
	// 成就奖励
	public static final String Chengjl_url = BASE_URL
			+ "User/UserGetCjjl.action";
	/**
	 * 分享记录
	 */
	public static final String Share_jilu_url = BASE_URL
			+ "User/AddShareAppRecord.action";
	/**
	 * 话题列表
	 */
	public static final String Topic_List_URL = BASE_URL + "HuatiList.action";
	/**
	 * 话题列表
	 */
	public static final String TOPIC_COMMENT_LIST = BASE_URL
			+ "HuatiPinglunList.action";
	/**
	 * 通过html页面查看话题详情
	 */
	public static final String TOPIC_DETAIL_HTML = BASE_URL
			+ "HuatiDetailHtml.action";
	/**
	 * 话题列表
	 */
	public static final String PUB_TOPIC = BASE_URL + "User/SaveHuati.action";
	/**
	 * 给指定话题发表评论
	 */
	public static final String SEND_TOPIC_COMMENT = BASE_URL
			+ "User/FabuPinglun.action";
	/**
	 * 给指定话题点赞
	 */
	public static final String CLICK_GOOD = BASE_URL + "User/DianZan.action";
	/**
	 * 通过html查看新闻详情
	 */
	public static final String NEWS_DETAIL_HTML = BASE_URL
			+ "NewsDetailHtml.action";
	/**
	 * 给指定话题发表评论
	 */
	public static final String SEND_NEWS_COMMENT = BASE_URL
			+ "User/NewsPinglun.action";
	/**
	 * 打招呼消息列表
	 */
	public static final String GET_CHECK_LIST = BASE_URL
			+ "User/ListChatMsg.action";
	/**
	 * 获取好友列表
	 */
	public static final String GET_FRIENDS_LIST = BASE_URL
			+ "User/ListFriends.action";
	/**
	 * 获取用户信息通过id
	 */
	public static final String GET_USER_INFO_BYID = BASE_URL
			+ "GetUserInfoByHybh00.action";
	/**
	 * 设置是否是好友
	 */
	public static final String SET_FRIEND_FLAG = BASE_URL
			+ "User/SetFriendFlag.action";
	/**
	 * 绑定手机号
	 */
	public static final String BINDING_PHONE = BASE_URL
			+ "User/BoundMobile.action";
	/**
	 * 认证WiFi
	 */
	public static final String AUTHENTICATE_WIFI = "http://192.168.18.1/common/api/xl_login.php";
	/**
	 * 是否商家
	 */
	public static final String IS_SELLER = BASE_URL + "User/IsShangHu.action";

}
