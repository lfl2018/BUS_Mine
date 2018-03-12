package com.mogu.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mogu.activity.LoginActivity;
import com.mogu.activity.MainActivity;
import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.MyCookieStore;
import com.mogu.entity.login.Login;
import com.mogu.entity.login.LoginJson;
import com.mogu.utils.Des;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.Util;
import com.mogu.utils.ValueUtils;
import com.tencent.mm.sdk.platformtools.Log;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseUIListener implements IUiListener {
	private Activity mContext;
	private String mScope;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;

	private String openid;
	private String nick;
	private String sex;
	private String head;
	private String sexcode;

	// 用于存储默认分享wifi状态
	private SharedPreferences sp2;

	private SharedPreferences accountPreferences;

	private void getUserInfo() {
		if ("男".equals(sex)) {
			sexcode = "001";
		} else {
			sexcode = "002";
		}
		openid = LoginActivity.mTencent.getOpenId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
		String date = sdf.format(new Date());
		String ciphertext = null;
		try {
			ciphertext = Des.encryptDES(openid, date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SessionRequestParams params = new SessionRequestParams(
				MrLiUrl.URL_QQ_LOGIN);
		params.addBodyParameter("qq0000", ciphertext);
		params.addBodyParameter("hync00", nick);
		params.addBodyParameter("xbie00", sexcode);
		params.addBodyParameter("txiang", head);
		x.http().post(params, new HttpCallBack() {

			@Override
			public void onFinished() {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Util.dismissDialog();
				Toast.makeText(x.app(), "登录失败，请检查网络！", Toast.LENGTH_LONG)
						.show();

			}

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void success(String result) throws JSONException {
				Log.e("爸爸", result);

				LoginJson json = GsonUtils.parseJSON(result, LoginJson.class);
				LogUtil.e(result);
				String statusCode = json.getCode();
				final Login login = json.getAjaxUserDTO();
				if ("1".equals(statusCode)) {
					// login
					EMClient.getInstance().login(
							ValueUtils.getHXId(login.getHybh00()), "123456",
							new EMCallBack() {

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
												mContext.runOnUiThread(new Runnable() {
													public void run() {
														// if
														// (!mContext.isFinishing())
														// dialog.dismiss();

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
																				mContext.runOnUiThread(new Runnable() {
																					public void run() {
																						Util.dismissDialog();
																						Toast.makeText(
																								mContext,
																								"登录失败！",
																								Toast.LENGTH_SHORT)
																								.show();
																					}
																				});
																			}
																		});

													}
												});
											} catch (final HyphenateException e) {
												mContext.runOnUiThread(new Runnable() {
													public void run() {
														if (!mContext
																.isFinishing())
															Util.dismissDialog();
														int errorCode = e
																.getErrorCode();
														if (errorCode == EMError.NETWORK_ERROR) {
															Toast.makeText(
																	mContext,
																	mContext.getResources()
																			.getString(
																					R.string.network_anomalies),
																	Toast.LENGTH_SHORT)
																	.show();
														} else if (errorCode == EMError.USER_ALREADY_EXIST) {
															Toast.makeText(
																	mContext,
																	mContext.getResources()
																			.getString(
																					R.string.User_already_exists),
																	Toast.LENGTH_SHORT)
																	.show();
														} else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
															Toast.makeText(
																	mContext,
																	mContext.getResources()
																			.getString(
																					R.string.registration_failed_without_permission),
																	Toast.LENGTH_SHORT)
																	.show();
														} else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
															Toast.makeText(
																	mContext,
																	mContext.getResources()
																			.getString(
																					R.string.illegal_user_name),
																	Toast.LENGTH_SHORT)
																	.show();
														} else {
															Toast.makeText(
																	mContext,
																	mContext.getResources()
																			.getString(
																					R.string.Registration_failed),
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
					Util.dismissDialog();
					Toast.makeText(mContext, json.getMsg(), Toast.LENGTH_SHORT)
							.show();

				}
			}
		});
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ON_COMPLETE:
				JSONObject response = (JSONObject) msg.obj;
				try {
					LogUtil.e(response.toString());
					nick = response.getString("nickname");
					head = response.getString("figureurl_qq_2");
					sex = response.getString("gender");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				getUserInfo();
				// Util.showResultDialog(mContext, response.toString(),
				// "onComplete");
				// Util.dismissDialog();
				break;
			case ON_ERROR:
				UiError e = (UiError) msg.obj;
				Util.showResultDialog(mContext, "errorMsg:" + e.errorMessage
						+ "errorDetail:" + e.errorDetail, "onError");
				Util.dismissDialog();
				break;
			case ON_CANCEL:
				Util.toastMessage((Activity) mContext, "onCancel");
				break;
			}
		}
	};

	public BaseUIListener(Activity mContext) {
		super();
		this.mContext = mContext;
		accountPreferences = mContext.getSharedPreferences(
				Constant.ACCOUNT_SHARE_FILE_NAME, Context.MODE_PRIVATE);
	}

	public BaseUIListener(Activity mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
		accountPreferences = mContext.getSharedPreferences(
				Constant.ACCOUNT_SHARE_FILE_NAME, Context.MODE_PRIVATE);
	}

	public void cancel() {
		mIsCaneled = true;
	}

	@Override
	public void onComplete(Object response) {
		if (mIsCaneled)
			return;
		Message msg = mHandler.obtainMessage();
		msg.what = ON_COMPLETE;
		msg.obj = response;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled)
			return;
		Message msg = mHandler.obtainMessage();
		msg.what = ON_ERROR;
		msg.obj = e;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled)
			return;
		Message msg = mHandler.obtainMessage();
		msg.what = ON_CANCEL;
		mHandler.sendMessage(msg);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Activity mContext) {
		this.mContext = mContext;
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

				SharedPreferences.Editor editor = mContext
						.getSharedPreferences(Constant.SP_COOKIE_NAME,
								Context.MODE_PRIVATE).edit();
				editor.putString("Cookie", value);
				editor.putString("domain", domain);
				editor.commit();
				MyCookieStore.cookie = cookie;
				break;
			}
		}

		MyCookieStore.user = login;
		SharedPreferences sp1 = mContext.getSharedPreferences(
				Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit1 = sp1.edit();

		String jsonString = new Gson().toJson(login);
		LogUtil.i("json:" + jsonString);
		edit1.putBoolean("islogin", true);
		edit1.putString(Constant.SESSION, jsonString);
		edit1.commit();

		Editor accountEditor = accountPreferences.edit();

		accountEditor.putString(Constant.CURRENT_ACCOUNT_NAME, "");
		// accountEditor.putString("user", jsonString);
		accountEditor.commit();
		sp2 = mContext.getSharedPreferences("agree", Context.MODE_PRIVATE);
		Editor edt = sp2.edit();
		edt.putBoolean("isagree", false);
		edt.commit();
		mContext.startActivity(new Intent(mContext, MainActivity.class));

		LoginActivity.mTencent.logout(mContext);
		mContext.finish();
		Util.dismissDialog();
	}

}
