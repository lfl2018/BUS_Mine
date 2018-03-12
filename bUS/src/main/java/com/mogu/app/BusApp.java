package com.mogu.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.hyphenate.util.EMLog;
import com.mogu.activity.ChatActivity;
import com.mogu.activity.MainActivity;
import com.mogu.utils.NetworkUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.List;

public class BusApp extends Application {

	// 单例
	private static BusApp instance;
	static Object object = new Object();
	private boolean isInputLog = true;

	protected EMMessageListener messageListener = null;
	private EaseUI easeUI;
	private EMConnectionListener connectionListener;

	@Override
	public void onCreate() {
		super.onCreate();
		EaseUI.getInstance().init(this, null);

		instance = this;
		NetworkUtil.checkNetworkStatus(this);


		x.Ext.setDebug(isInputLog); // 是否输出debug日志, 开启debug会影响性能.

		registerConnectionListener();x.Ext.init(this);
		registerMessageListener();
	}

	public static BusApp getInstance() {
		synchronized (object) {
			if (instance == null) {
				instance = new BusApp();
			}
			return instance;
		}
	}

	/**
	 * user met some exception: conflict, removed or forbidden
	 */
	protected void onUserException(String exception) {
		LogUtil.e("onUserException: " + exception);
		Intent intent = new Intent(instance, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(exception, true);
		instance.startActivity(intent);
	}

	protected void registerConnectionListener() {
		// create the global connection listener
		connectionListener = new EMConnectionListener() {
			@Override
			public void onDisconnected(int error) {
				EMLog.d("global listener", "onDisconnect" + error);
				if (error == EMError.USER_REMOVED) {
					onUserException(Constant.ACCOUNT_REMOVED);
				} else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
					onUserException(Constant.ACCOUNT_CONFLICT);
				} else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
					onUserException(Constant.ACCOUNT_FORBIDDEN);
				}
			}

			@Override
			public void onConnected() {
			}
		};

		// register connection listener
		EMClient.getInstance().addConnectionListener(connectionListener);
	}

	protected void registerMessageListener() {
		easeUI = EaseUI.getInstance();

		// set notification options, will use default if you don't set it
		easeUI.getNotifier().setNotificationInfoProvider(
				new EaseNotificationInfoProvider() {

					@Override
					public String getTitle(EMMessage message) {
						// you can update title here
						return null;
					}

					@Override
					public int getSmallIcon(EMMessage message) {
						// you can update icon here
						return 0;
					}

					@Override
					public String getDisplayedText(EMMessage message) {
						// be used on notification bar, different text according
						// the message type.
						// String ticker =
						// EaseCommonUtils.getMessageDigest(message,
						// appContext);
						// if(message.getType() == Type.TXT){
						// ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
						// }
						// EaseUser user = getUserInfo(message.getFrom());
						// if(user != null){
						// if(EaseAtMessageHelper.get().isAtMeMsg(message)){
						// return
						// String.format(appContext.getString(R.string.at_your_in_group),
						// user.getNick());
						// }
						// return user.getNick() + ": " + ticker;
						// }else{
						// if(EaseAtMessageHelper.get().isAtMeMsg(message)){
						// return
						// String.format(appContext.getString(R.string.at_your_in_group),
						// message.getFrom());
						// }
						// return message.getFrom() + ": " + ticker;
						// }
						return null;

					}

					@Override
					public String getLatestText(EMMessage message,
							int fromUsersNum, int messageNum) {
						// here you can customize the text.
						// return fromUsersNum + "contacts send " + messageNum +
						// "messages to you";
						return null;
					}

					@Override
					public Intent getLaunchIntent(EMMessage message) {
						// you can set what activity you want display when user
						// click the notification
						Intent intent = new Intent(instance, ChatActivity.class);
						// open calling activity if there is call
						ChatType chatType = message.getChatType();
						if (chatType == ChatType.Chat) { // single chat
															// message
							intent.putExtra("userId", message.getFrom());
							intent.putExtra("chatType",
									EaseConstant.CHATTYPE_SINGLE);
							intent.putExtra(EaseConstant.RECEIVER_NICK, message
									.getStringAttribute(EaseConstant.NICK, ""));
							intent.putExtra(EaseConstant.RECEIVER_AVATAR,
									message.getStringAttribute(
											EaseConstant.AVATAR, ""));
						} else { // group chat message
							// message.getTo() is the group id
							intent.putExtra("userId", message.getTo());
							if (chatType == ChatType.GroupChat) {
								intent.putExtra("chatType",
										EaseConstant.CHATTYPE_GROUP);
							} else {
								intent.putExtra("chatType",
										EaseConstant.CHATTYPE_CHATROOM);
							}

						}
						return intent;
					}
				});

		messageListener = new EMMessageListener() {
			private BroadcastReceiver broadCastReceiver = null;

			@Override
			public void onMessageReceived(List<EMMessage> messages) {
				for (EMMessage message : messages) {
					LogUtil.d("onMessageReceived id : " + message.getMsgId());
					// in background, do not refresh UI, notify it in
					// notification bar
					if (!easeUI.hasForegroundActivies()) {
						getNotifier().onNewMsg(message);
					}
				}
			}

			@Override
			public void onCmdMessageReceived(List<EMMessage> messages) {
			}

			@Override
			public void onMessageRead(List<EMMessage> messages) {
			}

			@Override
			public void onMessageDelivered(List<EMMessage> message) {
			}

			@Override
			public void onMessageChanged(EMMessage message, Object change) {

			}
		};

		EMClient.getInstance().chatManager()
				.addMessageListener(messageListener);
	}

	public EaseNotifier getNotifier() {
		return easeUI.getNotifier();
	}

}
