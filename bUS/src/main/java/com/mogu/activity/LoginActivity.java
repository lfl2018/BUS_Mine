package com.mogu.activity;

import java.net.HttpCookie;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.login.Login;
import com.mogu.entity.login.LoginJson;
import com.mogu.listener.BaseUIListener;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.ToastShow;
import com.mogu.utils.Util;
import com.mogu.utils.ValueUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 登陆页面
 * 
 * @author Administrator
 *
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
	private EditText phoneNumEditText;
	private EditText mPassWord;
	private Button mLogin;
	// 用于存储默认分享wifi状态
	private SharedPreferences sp2;
	View focusView = null;

	private ProgressDialog dialog;
	private String phonenum;
	private String userid;
	private SharedPreferences accountPreferences;
	private TextView regist;
	private TextView tv_forget_password;
	private TextView tv_quick_login;
	private CheckBox cb_Agree;
	private boolean isAgree;

	private ImageView img_qq;
	private ImageView img_wx;
	private LinearLayout ll_quick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m_login);
		ll_quick = (LinearLayout) findViewById(R.id.quick);
		// ActionBar actionBar = getSupportActionBar();
		// actionBar.setTitle("登录");
		listener = new BaseUiListener();
		// Drawable bg = getResources().getDrawable(R.drawable.Toolbar);
		// actionBar.setBackgroundDrawable(bg);
		mLogin = (Button) findViewById(R.id.login_sign_in_button);
		tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
		img_qq = (ImageView) findViewById(R.id.img_qq);
		img_wx = (ImageView) findViewById(R.id.img_wx);
		img_qq.setOnClickListener(this);
		img_wx.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		regist = (TextView) findViewById(R.id.tv_register);
		regist.setOnClickListener(this);
		tv_forget_password.setOnClickListener(this);
		cb_Agree = (CheckBox) findViewById(R.id.check_agree);
		phoneNumEditText = (EditText) findViewById(R.id.edt_phonenum);
		mPassWord = (EditText) findViewById(R.id.edt_password);

		accountPreferences = getSharedPreferences(
				Constant.ACCOUNT_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		phonenum = accountPreferences.getString(Constant.CURRENT_ACCOUNT_NAME,
				"");
		phoneNumEditText.setText(phonenum);

		mTencent = Tencent.createInstance("1105879053", this);
		api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
		api.registerApp(WX_APP_ID);
		mInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_sign_in_button:
			isAgree = cb_Agree.isChecked();
			attemptLogin();
			break;
		case R.id.tv_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.tv_forget_password:
			startActivity(new Intent(LoginActivity.this,
					ForgetPassActivity.class));
			break;
		case R.id.tv_quick_login:
			startActivity(new Intent(LoginActivity.this, QQloginActivity.class));
			break;
		case R.id.img_qq:
			qqlogin();
			break;
		case R.id.img_wx:
			wxLogin();
			break;
		default:
			break;
		}
	}

	private void attemptLogin() {
		boolean cancel = false;
		phoneNumEditText.setError(null);
		mPassWord.setError(null);
		phonenum = phoneNumEditText.getText().toString();
		String password = mPassWord.getText().toString();
		if (TextUtils.isEmpty(password)) {
			mPassWord.setError(getString(R.string.error_isEmpty_password));
			focusView = mPassWord;
			cancel = true;
		}
		if (TextUtils.isEmpty(phonenum)) {
			phoneNumEditText
					.setError(getString(R.string.error_isEmpty_phonenum));
			focusView = phoneNumEditText;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage("登录中...");
			dialog.show();
			RequestParams params = new RequestParams(Url.LOGIN_URL);
			params.addBodyParameter("sjhm00", phonenum);
			params.addBodyParameter("mima00", password);
			x.http().post(params, new Callback.CommonCallback<String>() {

				@Override
				public void onSuccess(String result) {
					LoginJson json = GsonUtils.parseJSON(result,
							LoginJson.class);
					LogUtil.e(result);
					String statusCode = json.getCode();
					final Login login = json.getAjaxUserDTO();
					if ("1".equals(statusCode)) {
						// login
						EMClient.getInstance().login(
								ValueUtils.getHXId(login.getHybh00()),
								"123456", new EMCallBack() {

									@Override
									public void onSuccess() {
										loginSuccess(login);
									}

									@Override
									public void onProgress(int progress,
											String status) {

									}

									@Override
									public void onError(int code, String error) {
										if (code == 200) {
											loginSuccess(login);
											return;
										}
										new Thread(new Runnable() {
											public void run() {
												try {
													// call method in SDK
													EMClient.getInstance()
															.createAccount(
																	ValueUtils
																			.getHXId(login
																					.getHybh00()),
																	"123456");
													runOnUiThread(new Runnable() {
														public void run() {
															if (!LoginActivity.this
																	.isFinishing())
																dialog.dismiss();

															// login
															EMClient.getInstance()
																	.login(ValueUtils
																			.getHXId(login
																					.getHybh00()),
																			"123456",
																			new EMCallBack() {

																				@Override
																				public void onSuccess() {
																					loginSuccess(login);
																				}

																				@Override
																				public void onProgress(
																						int progress,
																						String status) {

																				}

																				@Override
																				public void onError(
																						int code,
																						String error) {
																					runOnUiThread(new Runnable() {
																						public void run() {
																							Toast.makeText(
																									getApplicationContext(),
																									"登录失败！",
																									0)
																									.show();
																						}
																					});
																				}
																			});

														}
													});
												} catch (final HyphenateException e) {
													runOnUiThread(new Runnable() {
														public void run() {
															if (!LoginActivity.this
																	.isFinishing())
																dialog.dismiss();
															int errorCode = e
																	.getErrorCode();
															if (errorCode == EMError.NETWORK_ERROR) {
																Toast.makeText(
																		getApplicationContext(),
																		getResources()
																				.getString(
																						R.string.network_anomalies),
																		Toast.LENGTH_SHORT)
																		.show();
															} else if (errorCode == EMError.USER_ALREADY_EXIST) {
																Toast.makeText(
																		getApplicationContext(),
																		getResources()
																				.getString(
																						R.string.User_already_exists),
																		Toast.LENGTH_SHORT)
																		.show();
															} else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
																Toast.makeText(
																		getApplicationContext(),
																		getResources()
																				.getString(
																						R.string.registration_failed_without_permission),
																		Toast.LENGTH_SHORT)
																		.show();
															} else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
																Toast.makeText(
																		getApplicationContext(),
																		getResources()
																				.getString(
																						R.string.illegal_user_name),
																		Toast.LENGTH_SHORT)
																		.show();
															} else {
																Toast.makeText(
																		getApplicationContext(),
																		"登录失败！",
																		Toast.LENGTH_SHORT)
																		.show();
															}
														}
													});
												}
											}
										}).start();

									}
								});

					} else {

						// loginSuccess(login);
						dialog.dismiss();
						Toast.makeText(LoginActivity.this, json.getMsg(),
								Toast.LENGTH_SHORT).show();

					}
				}

				@Override
				public void onError(Throwable ex, boolean isOnCallback) {
					Toast.makeText(x.app(), "登录失败，请检查网络！", Toast.LENGTH_LONG)
							.show();
				}

				@Override
				public void onCancelled(CancelledException cex) {
					Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG)
							.show();
				}

				@Override
				public void onFinished() {
				//	dialog.dismiss();
				}
			});
		}

	}

	private void loginSuccess(Login login) {
		// Toast.makeText(LoginActivity.this, json.getMessage(),
		// Toast.LENGTH_SHORT).show();

		DbCookieStore instance = DbCookieStore.INSTANCE;
		List<HttpCookie> cookies = instance.getCookies();
		for (HttpCookie cookie : cookies) {

			String name = cookie.getName();
			String value = cookie.getValue();
			String domain = cookie.getDomain();
			if ("JSESSIONID".equals(name)) {

				SharedPreferences.Editor editor = getSharedPreferences(
						Constant.SP_COOKIE_NAME, MODE_PRIVATE).edit();
				editor.putString("Cookie", value);
				editor.putString("domain", domain);
				editor.commit();
				MyCookieStore.cookie = cookie;
				break;
			}
		}

		MyCookieStore.user = login;
		SharedPreferences sp1 = getSharedPreferences(
				Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit1 = sp1.edit();

		String jsonString = new Gson().toJson(login);
		LogUtil.i("json:" + jsonString);
		edit1.putBoolean("islogin", true);
		edit1.putString(Constant.SESSION, jsonString);
		edit1.commit();

		Editor accountEditor = accountPreferences.edit();

		accountEditor.putString(Constant.CURRENT_ACCOUNT_NAME, phonenum);
		// accountEditor.putString("user", jsonString);
		accountEditor.commit();
		sp2 = getSharedPreferences("agree", Context.MODE_PRIVATE);
		Editor edt = sp2.edit();
		edt.putBoolean("isagree", isAgree);
		edt.commit();
		startActivity(new Intent(LoginActivity.this, MainActivity.class));

		finish();
	}

	// 快捷
	private static final String WX_APP_ID = "wx5e14bf8ddb337087";
	public static Tencent mTencent;
	private BaseUiListener listener;
	private UserInfo mInfo = null;
	private IWXAPI api;

	private void wxLogin() {
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		api.sendReq(req);
	}

	public void qqlogin() {
		mTencent = Tencent.createInstance("1105879053",
				LoginActivity.this.getApplicationContext());
		if (!mTencent.isSessionValid()) {
			mTencent.login(LoginActivity.this, "all", listener);
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
				return;
			}
			// Util.showResultDialog(QQloginActivity.this, response.toString(),
			// "登录成功");
			// // 有奖分享处理
			// // handlePrizeShare();
			doComplete((JSONObject) response);
			if (LoginActivity.ready(LoginActivity.this)) {
				mInfo.getUserInfo(new BaseUIListener(LoginActivity.this,
						"get_simple_userinfo"));
				Util.showProgressDialog(LoginActivity.this, null, null);
			}
		}

		protected void doComplete(JSONObject values) {
			initOpenidAndToken(values);
		}

		@Override
		public void onError(UiError e) {
			// Util.toastMessage(QQloginActivity.this, "onError: " +
			// e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(LoginActivity.this, "已返回 ");
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
		} catch (Exception e) {
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("爸爸", "-->onActivityResult " + requestCode + " resultCode="
				+ resultCode);
		if (requestCode == Constants.REQUEST_LOGIN
				|| requestCode == Constants.REQUEST_APPBAR) {
			Tencent.onActivityResultData(requestCode, resultCode, data,
					listener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
