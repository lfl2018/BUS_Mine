package com.mogu.app;

import com.mogu.activity.R;

import android.R.integer;

/**
 * 存放一些常量
 * 
 */
public class Constant {

	//环信
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String ACCOUNT_CONFLICT = "conflict";
	public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
	public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
	
	public static final String SEPARATOR = "xmwxcwlkjyxgs";
	public static final String SESSION = "user";
	public static final String MAIN_SHARE_FILE_NAME = "bus";
	public static final String ACCOUNT_SHARE_FILE_NAME = "Account";
	public static final String CURRENT_ACCOUNT_NAME = "current";
	public static final String SP_COOKIE_NAME = "cookie";
	
	public static final int[] LOCK_LEVEL_IMAGE = {  R.drawable.icon_wifi_nosignal_lock,
		R.drawable.icon_wifi_weak_lock, R.drawable.icon_wifi_third_lock, R.drawable.icon_wifi_second_lock,
		R.drawable.icon_wifi_strong_lock  };
	
	public static final int[] UNLOCK_LEVEL_IMAGE = { R.drawable.icon_wifi_nosignal_unlock,
			R.drawable.icon_wifi_weak_unlock, R.drawable.icon_wifi_third_unlock, R.drawable.icon_wifi_second_unlock,
			R.drawable.icon_wifi_strong_unlock };
//=======
//	public static final int[] LOCK_LEVEL_IMAGE = { R.drawable.ewifilock0,
//			R.drawable.ewifilock1, R.drawable.ewifilock2,
//			R.drawable.ewifilock3, R.drawable.ewifilock4 };
//	public static final int[] UNLOCK_LEVEL_IMAGE = { R.drawable.ewifi0,
//			R.drawable.ewifi1, R.drawable.ewifi2, R.drawable.ewifi3,
//			R.drawable.ewifi4 };
//>>>>>>> .r173
	public static final String CLICK_WIFI_INFO = "winfo";

	/**
	 * 话题的编号
	 */
	public static final String TOPIC_VALUE = "topic_value";

	/**
	 * 话题的评论回复的标志
	 */
	public static final String REPLY_MARK = "【回复】";
	
	/**
	 * 分享到社交平台的url
	 */
	public static final String SHARE_URL = "share_url";
	
	/**
	 * 分享到社交平台的title
	 */
	public static final String SHARE_TITLE = "share_title";

	/**
	 * 分享到社交平台的description
	 */
	public static final String SHARE_DESCRIPTION = "share_description";
	
	public static final String COMMENT_VALUE = "comment_value";

	/**
	 * 话题的编号
	 */
	public static final String NEWS_VALUE = "news_value";

	/**
	 * wifi的mac地址
	 */
	public static final String WIFI_MAC = "wifi_mac";
	
	/**
	 * 其他用户的个人信息
	 */
	public static final String FRIEND_INFO = "friend";
	
	/**
	 * 接收人编号
	 */
	public static final String RECEIVE_ID = "receive_id";
	
	/**
	 * 昵称
	 */
	public static final String NICK = "nick";
	
	/**
	 * 头像
	 */
	public static final String AVATAR = "avatar";
	/**
	 * 好友
	 */
	public static final String IS_FRIEND = "is_friend";
	/**
	 * 分享据点的名称
	 */
	public static final String JUDIAN_NAME = "judian_name";
	/**
	 * 标签
	 */
	public static final String LABEL_VALUE = "label_value";

}
