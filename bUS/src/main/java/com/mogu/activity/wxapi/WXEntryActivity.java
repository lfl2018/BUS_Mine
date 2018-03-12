package com.mogu.activity.wxapi;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mogu.activity.BaseActivity;
import com.mogu.activity.Constants;
import com.mogu.activity.HtmlTopicDetailActivity;
import com.mogu.activity.LoginActivity;
import com.mogu.activity.MainActivity;
import com.mogu.activity.QQloginActivity;
import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.login.Login;
import com.mogu.entity.login.LoginJson;
import com.mogu.entity.topiclist.HuatiList;
import com.mogu.utils.Des;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;
import com.mogu.utils.ValueUtils;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * 获取微信回调的方法
 * 
 * @author Mr Li
 *
 */

public class WXEntryActivity extends BaseActivity implements
		IWXAPIEventHandler, OnClickListener {
	// private static final String APP_ID = "wx5e14bf8ddb337087";
	private IWXAPI api;
	private LinearLayout ll_s;
	private LinearLayout ll_share;
	private LinearLayout ll_share_hy;
	private TextView tv_pyq;
	private TextView tv_hy;

	private String shareTitle;
	private String shareUrl;
	private String shareDescription;
	private String token;

	private AlertDialog dialog;
	private SharedPreferences sp2;
	private SharedPreferences accountPreferences;
	private TextView tvCancel;

	/**
	 * 取消分享干掉此页面
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_to_wx);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		accountPreferences = getSharedPreferences(
				Constant.ACCOUNT_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		// ll_s = (LinearLayout) findViewById(R.id.ll_s);
		// ll_share = (LinearLayout) findViewById(R.id.ll_share_pyq);
		// ll_share.setOnClickListener(this);
		// ll_share_hy = (LinearLayout) findViewById(R.id.ll_share_wxhy);
		// ll_share_hy.setOnClickListener(this);

		ll_s = (LinearLayout) findViewById(R.id.ll_s);
		tv_hy = (TextView) findViewById(R.id.tv_s_hy);
		tv_hy.setOnClickListener(this);
		tv_pyq = (TextView) findViewById(R.id.tv_s_pyq);
		tv_pyq.setOnClickListener(this);
		tvCancel = (TextView) findViewById(R.id.tv_cancel);
		tvCancel.setOnClickListener(this);

		api.handleIntent(getIntent(), this);
		// TitleUtils.setTitle(WXEntryActivity.this, R.id.topbar, "微信");

		shareTitle = getIntent().getStringExtra(Constant.SHARE_TITLE);
		shareUrl = getIntent().getStringExtra(Constant.SHARE_URL);
		shareDescription = getIntent().getStringExtra(
				Constant.SHARE_DESCRIPTION);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			switch (resp.getType()) {
			case ConstantsAPI.COMMAND_SENDAUTH:
				result = "登陆成功";
				ll_s.setVisibility(View.GONE);
				dialogON();
				token = ((SendAuth.Resp) resp).token;
				getASToken(token);
				// getToken(token);
				break;
			case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
				// 分享回调,处理分享成功后的逻辑
				result = "分享成功";
				sendMsgToBack();
				break;
			default:
				break;
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "被拒绝";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			break;

		default:
			result = "返回";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			break;

		}

	}

	/**
	 * 创建对话框
	 */
	private void dialogON() {
		if (dialog != null) {
			dialog.show();
		} else {
			dialog = new AlertDialog.Builder(WXEntryActivity.this).setMessage(
					"数据拉取中...").create();
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	/**
	 * 发消送数据到AOO后台
	 */
	private void sendMsgToBack() {
		SessionRequestParams params = new SessionRequestParams(
				Url.Share_jilu_url);
		params.addBodyParameter("fxdzhi", "微信");
		params.addBodyParameter("fxnr00", "http://www.wxcw.net/");
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
				JSONObject json = new JSONObject(result);
				if (json.getString("code").equals("1")) {
					finish();
				} else {
					ToastShow.shortShowToast(json.getString("msg"));
				}
			}
		});
	}

	// private void goToGetMsg() {
	// Intent intent = new Intent(this, GetFromWXActivity.class);
	// intent.putExtras(getIntent());
	// startActivity(intent);
	// finish();
	// }
	//
	// private void goToShowMsg(ShowMessageFromWX.Req showReq) {
	// WXMediaMessage wxMsg = showReq.message;
	// WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
	//
	// StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
	// msg.append("description: ");
	// msg.append(wxMsg.description);
	// msg.append("\n");
	// msg.append("extInfo: ");
	// msg.append(obj.extInfo);
	// msg.append("\n");
	// msg.append("filePath: ");
	// msg.append(obj.filePath);
	//
	// Intent intent = new Intent(this, ShowFromWXActivity.class);
	// intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
	// intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
	// intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
	// startActivity(intent);
	// finish();
	// }

	/**
	 * 分享到微信朋友圈
	 */
	// private void wxShare() {
	// WXWebpageObject webpage = new WXWebpageObject();
	// webpage.webpageUrl = "http://www.wxcw.net/";
	// WXMediaMessage msg = new WXMediaMessage(webpage);
	// msg.title = "同在WIFI";
	// msg.description = "再也不用担心流量不够用了";
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.ic_wxc);
	// msg.thumbData = Util.bmpToByteArray(thumb, true);
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("webpage");
	// req.message = msg;
	// req.scene = SendMessageToWX.Req.WXSceneTimeline;
	// api.sendReq(req);
	// //finish();
	// }

	/**
	 * 分享到微信朋友圈
	 */
	private void wxShare(String url, String title, String description) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_wxc);
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
		// finish();
	}

	/**
	 * 分享给微信好友
	 */
	// private void wxSharehy() {
	// WXWebpageObject webpage = new WXWebpageObject();
	// webpage.webpageUrl = "http://www.wxcw.net/";
	// WXMediaMessage msg = new WXMediaMessage(webpage);
	// msg.title = "WIFI无限城";
	// msg.description = "再也不用担心流量不够用了";
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.ic_wxc);
	// msg.thumbData = Util.bmpToByteArray(thumb, true);
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("webpage");
	// req.message = msg;
	// req.scene = SendMessageToWX.Req.WXSceneSession;
	// api.sendReq(req);
	// //finish();
	// }

	/**
	 * 分享给微信好友
	 */
	private void wxSharehy(String url, String title, String description) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		if (TextUtils.isEmpty(description)) {
			description = "";
		} else if (description.length() > 500) {
			description = description.substring(0, 500);
		}
		msg.description = description;
		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_wxc);
		msg.thumbData = Util.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		// finish();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 判断微信是否安装
	 */
	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_s_pyq:
			if (isWeixinAvilible(WXEntryActivity.this)) {

				wxShare(shareUrl, shareTitle, shareDescription);
			} else {
				ToastShow.shortShowToast("未安装微信");
			}
			break;
		case R.id.tv_s_hy:
			if (isWeixinAvilible(WXEntryActivity.this)) {
				wxSharehy(shareUrl, shareTitle, shareDescription);
			} else {
				ToastShow.shortShowToast("未安装微信");
			}
			break;

		case R.id.tv_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取数据
	 */
	private void getASToken(String code) {

		LogUtil.e(MrLiUrl.URL_GET_TOKEN + "?appid=" + Constants.APP_ID
				+ "&secret=50995404b5454092e8f161764dfb15ef&code=" + code
				+ "&grant_type=authorization_code");
		RequestParams params = new RequestParams(MrLiUrl.URL_GET_TOKEN
				+ "?appid=" + Constants.APP_ID
				+ "&secret=50995404b5454092e8f161764dfb15ef&code=" + code
				+ "&grant_type=authorization_code");
		x.http().get(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				ToastShow.shortShowToast("TK数据拉取失败");
				Log.e("错了", arg0.toString());
				dialog.dismiss();
				finish();
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				Log.e("aadad", arg0);
				try {
					JSONObject json = new JSONObject(arg0);
					String opid = json.getString("openid");
					String astoken = json.getString("access_token");
					Log.e("asdfasfdgdsg", astoken);
					getData2(astoken, opid);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void getData2(String astoken, String opid) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ astoken + "&openid=" + opid;
		LogUtil.e(url);
		RequestParams params = new RequestParams(url);
		x.http().get(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.e("错了", "错了");
				ToastShow.shortShowToast("数据获取失败");
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				Log.e("aadad", arg0);
				try {
					JSONObject json = new JSONObject(arg0);
					String opid = json.getString("openid");
					String sex = json.getString("sex");
					String nick = json.getString("nickname");
					String head = json.getString("headimgurl");
					postData(opid, sex, nick, head);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void postData(String openid, String sex, String nick, String head) {
		String sexcode;
		if (head == null || "".equals(head)) {
			head = "aafas";
		}
		if ("1".equals(sex)) {
			sexcode = "001";
		} else {
			sexcode = "002";
		}
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
		params.addBodyParameter("weixin", ciphertext);
		params.addBodyParameter("hync00", nick);
		params.addBodyParameter("xbie00", sexcode);
		params.addBodyParameter("txiang", head);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastShow.shortShowToast("登录失败失败");
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				Log.e("爸爸", arg0);

				LoginJson json = GsonUtils.parseJSON(arg0, LoginJson.class);
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
												WXEntryActivity.this
														.runOnUiThread(new Runnable() {
															public void run() {
																// if
																// (!WXEntryActivity.this.isFinishing())
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
																						WXEntryActivity.this
																								.runOnUiThread(new Runnable() {
																									public void run() {
																										dialog.dismiss();
																										Toast.makeText(
																												WXEntryActivity.this,
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
												WXEntryActivity.this
														.runOnUiThread(new Runnable() {
															public void run() {
																if (!WXEntryActivity.this
																		.isFinishing())
																	dialog.dismiss();
																int errorCode = e
																		.getErrorCode();
																if (errorCode == EMError.NETWORK_ERROR) {
																	Toast.makeText(
																			WXEntryActivity.this,
																			WXEntryActivity.this
																					.getResources()
																					.getString(
																							R.string.network_anomalies),
																			Toast.LENGTH_SHORT)
																			.show();
																} else if (errorCode == EMError.USER_ALREADY_EXIST) {
																	Toast.makeText(
																			WXEntryActivity.this,
																			WXEntryActivity.this
																					.getResources()
																					.getString(
																							R.string.User_already_exists),
																			Toast.LENGTH_SHORT)
																			.show();
																} else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
																	Toast.makeText(
																			WXEntryActivity.this,
																			WXEntryActivity.this
																					.getResources()
																					.getString(
																							R.string.registration_failed_without_permission),
																			Toast.LENGTH_SHORT)
																			.show();
																} else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
																	Toast.makeText(
																			WXEntryActivity.this,
																			WXEntryActivity.this
																					.getResources()
																					.getString(
																							R.string.illegal_user_name),
																			Toast.LENGTH_SHORT)
																			.show();
																} else {
																	Toast.makeText(
																			WXEntryActivity.this,
																			WXEntryActivity.this
																					.getResources()
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
					dialog.dismiss();
					Toast.makeText(WXEntryActivity.this, json.getMsg(),
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	/**
	 * shua
	 */
	private void getToken(String code) {
		// "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=wx5e14bf8ddb337087&grant_type=refresh_token&refresh_token=Nkoe9nEirHq2SHntK3TbAsVVfEvRE0FdXYMMLMUZcWBI6z7UiidxaKKTLYtq9mVKd8h-xyAvwdf7Bv2KrzFt21TplDlZ0aCxxQQtxJc7Mbg"
		LogUtil.e("https://api.weixin.qq.com/sns/oauth2/refresh_token"
				+ "?appid=" + Constants.APP_ID
				+ "&grant_type=refresh_token&refresh_token=" + code);
		RequestParams params = new RequestParams(
				"https://api.weixin.qq.com/sns/oauth2/refresh_token"
						+ "?appid="
						+ Constants.APP_ID
						+ "&grant_type=refresh_token&refresh_token="
						+ "Nkoe9nEirHq2SHntK3TbAsVVfEvRE0FdXYMMLMUZcWBI6z7UiidxaKKTLYtq9mVKd8h-xyAvwdf7Bv2KrzFt21TplDlZ0aCxxQQtxJc7Mbg");
		x.http().get(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.e("错了", "错了");
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				Log.e("爸爸", arg0);
				try {
					JSONObject json = new JSONObject(arg0);
					token = json.getString("refresh_token");
					getASToken(token);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
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

				SharedPreferences.Editor editor = WXEntryActivity.this
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
		SharedPreferences sp1 = WXEntryActivity.this.getSharedPreferences(
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
		sp2 = WXEntryActivity.this.getSharedPreferences("agree",
				Context.MODE_PRIVATE);
		Editor edt = sp2.edit();
		edt.putBoolean("isagree", false);
		edt.commit();
		WXEntryActivity.this.startActivity(new Intent(WXEntryActivity.this,
				MainActivity.class));

		LoginActivity.mTencent.logout(WXEntryActivity.this);
		ToastShow.shortShowToast("登录成功");
		finish();
		dialog.dismiss();
	}
}
