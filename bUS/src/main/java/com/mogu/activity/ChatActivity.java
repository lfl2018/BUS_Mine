package com.mogu.activity;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.RightLayoutClickListener;

public class ChatActivity extends EaseBaseActivity {
	public static ChatActivity activityInstance;
	private EaseChatFragment chatFragment;
	String toChatUsername;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_chat);
		activityInstance = this;
		// user or group id
		toChatUsername = getIntent().getExtras().getString(
				EaseConstant.EXTRA_USER_ID);
		chatFragment = new EaseChatFragment();
		// 设置右上角图标的监听
		chatFragment
				.setRightLayoutClickListener(new RightLayoutClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent().setClass(ChatActivity.this,
								FriendDetailInfoActivity.class).putExtra(
								EaseConstant.EXTRA_USER_ID,
								toChatUsername.toLowerCase(Locale.US)));
					}
				});
		// set arguments
		chatFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, chatFragment).commit();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// enter to chat activity when click notification bar, here make sure
		// only one chat activiy
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		chatFragment.onBackPressed();
	}

	public String getToChatUsername() {
		return toChatUsername;
	}
}
