package com.mogu.activity;

import java.net.HttpCookie;

import org.xutils.common.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.entity.login.Login;
import com.mogu.utils.GsonUtils;

public class SplashActivity extends AppCompatActivity {

	private ImageView imageView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		// Animation animation = AnimationUtils.loadAnimation(this,
		// R.anim.splash_anim);
		//
		// imageView1.startAnimation(animation);
		imageView1.postDelayed(new Runnable() {
			public void run() {
				SharedPreferences sp = getSharedPreferences("Test", 0);
				boolean isFirst = sp.getBoolean("isFirst", true);

				Intent intent = null;
				if (isFirst) {
					intent = new Intent(SplashActivity.this,
							GuideActivity.class);
				} else {
					SharedPreferences sp1 = getSharedPreferences(
							Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
					boolean islogin = sp1.getBoolean("islogin", false);

					LogUtil.i(islogin + "");

					if (islogin) {
						String session = sp1.getString(Constant.SESSION, "");
						LogUtil.i("session:" + session);
						MyCookieStore.user = GsonUtils.parseJSON(session,
								Login.class);
						SharedPreferences cookiePreferences = getSharedPreferences(
								Constant.SP_COOKIE_NAME, MODE_PRIVATE);
						MyCookieStore.cookie = new HttpCookie("JSESSIONID",
								cookiePreferences.getString("Cookie", ""));
						MyCookieStore.cookie.setDomain(cookiePreferences
								.getString("domain", ""));
						intent = new Intent(SplashActivity.this,
								MainActivity.class);

					} else {
						intent = new Intent(SplashActivity.this,
								MainActivity.class);

					}

				}
				startActivity(intent);
				finish();
			}
		}, 200);
	}

}
