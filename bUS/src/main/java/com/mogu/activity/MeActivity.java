package com.mogu.activity;

import org.xutils.x;
import org.xutils.view.annotation.Event;

import com.mogu.app.MyCookieStore;
import com.mogu.utils.IsMobile;
import com.mogu.utils.ToastShow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		x.view().inject(this);
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Event(R.id.tv_change_avatar)
	private void toChangeAvatar(View view) {
		startActivity(new Intent(MeActivity.this, HeadPortraitActivity.class));
	}

	@Event(R.id.tv_my_info)
	private void toChangeInfo(View view) {
		startActivity(new Intent(MeActivity.this, MyInfoActivity.class));
	}

	@Event(R.id.tv_change_password)
	private void toChangePassword(View view) {
		if (!IsMobile.isMobileNO(MyCookieStore.user.getShouji())) {
			ToastShow.shortShowToast("快捷登录不能设置密码！");
			return;
		}
		startActivity(new Intent(MeActivity.this, ChangePasswordActivity.class));
	}

	@Event(R.id.tv_binding_phone)
	private void toBindingPhone(View view) {
		if (IsMobile.isMobileNO(MyCookieStore.user.getShouji())) {
			ToastShow.shortShowToast("已绑定手机！");
			return;
		}
		startActivity(new Intent(MeActivity.this, BindingPhoneActivity.class));
	}
	
}
