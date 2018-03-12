package com.mogu.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mogu.listener.BaseUIListener;
import com.mogu.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQloginActivity extends Activity{
	TextView ceshi;
	TextView ceshixx;
	TextView ceshitc;
	TextView WX;
	public static Tencent mTencent;
	private BaseUiListener listener;
	private UserInfo mInfo = null;
	private IWXAPI api;
	private static final String WX_APP_ID = "wx5e14bf8ddb337087";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qq_login);
		mTencent = Tencent.createInstance("1105879053", this);
		api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
		api.registerApp(WX_APP_ID);
		listener = new BaseUiListener();
		ceshi = (TextView) findViewById(R.id.tv_ceshi);
		ceshixx = (TextView) findViewById(R.id.tv_ceshixx);
		ceshitc = (TextView) findViewById(R.id.tv_ceshitc);
		WX = (TextView) findViewById(R.id.tv_WX);
		WX.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wxLogin();
			}

		});
		mInfo = new UserInfo(this, mTencent.getQQToken());
		
		
		
		ceshi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		ceshitc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTencent.logout(QQloginActivity.this);
			}
		});
		ceshixx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickUserInfo();
			}
		});
		
	}
	public static boolean ready(Context context) {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getQQToken().getOpenId() != null;
		if (!ready) {
            Toast.makeText(context, "login and get openId first, please!",
					Toast.LENGTH_SHORT).show();
        }
		return ready;
	}
	public void login()
	{
	mTencent = Tencent.createInstance("1105879053", this.getApplicationContext());
	if (!mTencent.isSessionValid())
	{
	mTencent.login(this, "all", listener);
	}
	}
	private void onClickUserInfo() {
		 if (QQloginActivity.ready(this)) {
			mInfo.getUserInfo(new BaseUIListener(this,"get_simple_userinfo"));
	            Util.showProgressDialog(this, null, null);
	        }
    }
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(QQloginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(QQloginActivity.this, "返回为空", "登录失败");
                return;
            }
//			Util.showResultDialog(QQloginActivity.this, response.toString(), "登录成功");
//            // 有奖分享处理
//         //   handlePrizeShare();
			doComplete((JSONObject)response);
            if (QQloginActivity.ready(QQloginActivity.this)) {
    			mInfo.getUserInfo(new BaseUIListener(QQloginActivity.this,"get_simple_userinfo"));
    	            Util.showProgressDialog(QQloginActivity.this, null, null);
    	        }
		}

		protected void doComplete(JSONObject values) {
			initOpenidAndToken(values);
		}

		@Override
		public void onError(UiError e) {
		//	Util.toastMessage(QQloginActivity.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(QQloginActivity.this, "已返回 ");
			Util.dismissDialog();
           
		}
	}
	
	public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.e("爸爸", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
	    if (requestCode == Constants.REQUEST_LOGIN ||
	    	requestCode == Constants.REQUEST_APPBAR) {
	    	Tencent.onActivityResultData(requestCode,resultCode,data,listener);
	    }

	    super.onActivityResult(requestCode, resultCode, data);
	}
	private void wxLogin() {
		 final SendAuth.Req req = new SendAuth.Req();
		    req.scope = "snsapi_userinfo";
		    req.state = "wechat_sdk_demo_test";
		    api.sendReq(req);
	}
}
