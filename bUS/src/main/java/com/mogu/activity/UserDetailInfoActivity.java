package com.mogu.activity;

import org.json.JSONException;
import org.xutils.x;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.entity.wfuser.FriendsList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;
import com.mogu.utils.ValueUtils;

/**
 * 详细资料页面
 * 
 * @author Administrator
 *
 */
public class UserDetailInfoActivity extends Activity implements OnClickListener {
	private TextView tvName;
	private TextView tvSign;
	private TextView tvSayHello;
	private TextView tvSendMsg;
	private ImageView ImgHead;
	private FriendsList friend;
	private String flag;
	private TextView tvInBlacklist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail_info);
		friend = (FriendsList) getIntent().getSerializableExtra(
				Constant.FRIEND_INFO);
		flag = getIntent().getStringExtra("flag");
		TitleUtils.setTitle(this, R.id.topbar, "详细资料");
		initViews();
	}

	private void initViews() {

		tvName = (TextView) findViewById(R.id.tv_name);
		tvSign = (TextView) findViewById(R.id.tv_sign);
		tvSayHello = (TextView) findViewById(R.id.tv_say_hello);
		tvSendMsg = (TextView) findViewById(R.id.tv_send_msg);
		tvInBlacklist = (TextView) findViewById(R.id.tv_in_blacklist);
		ImgHead = (ImageView) findViewById(R.id.img_head);

		tvName.setText(friend.getHyming());
		tvSign.setText(friend.getGxqm00());
		x.image().bind(ImgHead, ImageUtils.getImageUrl(friend.getTxiang()));

		if ("2".equals(flag)) {
			tvSayHello.setVisibility(View.GONE);
			tvSendMsg.setVisibility(View.GONE);
			tvInBlacklist.setVisibility(View.VISIBLE);
		} else if ("".equals(flag)) {
			tvSayHello.setVisibility(View.VISIBLE);
			tvSendMsg.setVisibility(View.GONE);
			tvInBlacklist.setVisibility(View.GONE);
		} else {
			tvSayHello.setVisibility(View.GONE);
			tvSendMsg.setVisibility(View.VISIBLE);
			tvInBlacklist.setVisibility(View.GONE);
		}

		tvSayHello.setOnClickListener(this);
		tvSendMsg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_say_hello:
			// Intent intent = new Intent();
			// intent.putExtra(Constant.RECEIVE_ID, friend.getHybh00());
			// intent.setClass(UserDetailInfoActivity.this,
			// SayHelloActivity.class);
			// startActivity(intent);
			// 加好友
			SessionRequestParams params = new SessionRequestParams(
					Url.SET_FRIEND_FLAG);

			params.addBodyParameter("frdbh0", friend.getHybh00());
			params.addBodyParameter("flag00", "1");
			x.http().post(params, new HttpCallBack() {

				@Override
				public void onFinished() {

				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {

				}

				@Override
				public void onCancelled(CancelledException arg0) {

				}

				@Override
				public void success(String result) throws JSONException {
					CommonJson json = GsonUtils.parseJSON(result,
							CommonJson.class);
					String code = json.getCode();
					String msg = json.getMsg();
					if ("1".equals(code)) {
						ToastShow.shortShowToast("添加好友成功！");

						EMMessage message = EMMessage.createTxtSendMessage("我是"
								+ MyCookieStore.user.getHyming(),
								ValueUtils.getHXId(friend.getHybh00()));
						// 增加自己特定的属性
						message.setAttribute(EaseConstant.NICK,
								MyCookieStore.user.getHyming());
						message.setAttribute(EaseConstant.AVATAR, ImageUtils
								.getImageUrl(MyCookieStore.user.getTxiang()));
						message.setAttribute(EaseConstant.HYID,
								MyCookieStore.user.getHybh00());
						message.setAttribute(EaseConstant.RECEIVER_NICK,
								friend.getHyming());
						message.setAttribute(EaseConstant.RECEIVER_AVATAR,
								ImageUtils.getImageUrl(friend.getTxiang()));
						// send message
						EMClient.getInstance().chatManager()
								.sendMessage(message);

						toChat();
					} else {
						ToastShow.shortShowToast(msg);
					}
				}
			});
			break;
		case R.id.tv_send_msg:
			toChat();
			break;

		default:
			break;
		}
	}

	public void toChat() {
		Intent intent = new Intent();
		intent.putExtra(EaseConstant.EXTRA_USER_ID,
				ValueUtils.getHXId(friend.getHybh00()));
		intent.putExtra(EaseConstant.RECEIVER_NICK, friend.getHyming());
		intent.putExtra(EaseConstant.RECEIVER_AVATAR,
				ImageUtils.getImageUrl(friend.getTxiang()));
		intent.setClass(UserDetailInfoActivity.this, ChatActivity.class);
		startActivity(intent);
		finish();
	}
}
