package com.mogu.fragment;

import java.net.HttpCookie;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.mogu.activity.MainActivity;
import com.mogu.activity.R;
import com.mogu.activity.ShopPubActivity;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TimeUtils;
import com.mogu.utils.ToastShow;
import com.mogu.utils.WifiUtils;

public class ShopFragment extends Fragment {

	private WebView topicDetailWebView;
	private String topicId = "";
	private View mLayout;
	private TextView tvPublish;
	private String url = Url.BASE_URL + "BizReleaseJsp.action?mac000=";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("爸爸","来了");

		if (mLayout == null) {
			Log.e("爸爸","去了");
			mLayout = inflater.inflate(R.layout.fragment_shop, null);
			x.view().inject(this, mLayout);
			topicDetailWebView = (WebView) mLayout
					.findViewById(R.id.wv_topic_detail);
			tvPublish = (TextView) mLayout.findViewById(R.id.tv_publish);
			tvPublish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String wifiMac;
					WifiUtils wifiUtils = new WifiUtils(getActivity());
					wifiUtils.getConnectedInfo();
					wifiMac = wifiUtils.getConnectedBSSID();

					SessionRequestParams params = new SessionRequestParams(
							Url.IS_SELLER);
					params.addBodyParameter("mac000", wifiMac);
					x.http().post(params, new HttpCallBack() {

						@Override
						public void onFinished() {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(Throwable arg0, boolean arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onCancelled(CancelledException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void success(String result) throws JSONException {
							CommonJson json = GsonUtils.parseJSON(result,
									CommonJson.class);
							String code = json.getCode();
							if ("1".equals(code)) {
								toPublish();
							} else {
								ToastShow.shortShowToast("只有认证过的商家才能发布信息！");
							}
						}

					});
				}
			});

		}
		return mLayout;

	}

	private void toPublish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), ShopPubActivity.class);
		startActivity(intent);
	}

	private void initWebView() {
		Log.e("爸爸","走了");
		topicDetailWebView.requestFocus();
		topicDetailWebView.removeJavascriptInterface("searchBoxJavaBridge_");
		WebSettings settings = topicDetailWebView.getSettings();
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setJavaScriptEnabled(true);

		// // 设置可以支持缩放
		// settings.setSupportZoom(true);
		// // 设置出现缩放工具
		// settings.setBuiltInZoomControls(true);
		// // // 扩大比例的缩放
		// // settings.setUseWideViewPort(true);
		// 自适应屏幕

		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		// settings.setPluginsEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setLoadWithOverviewMode(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setUseWideViewPort(true);
		topicDetailWebView.setWebChromeClient(new WebChromeClient());

		topicDetailWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});

		topicDetailWebView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});

		WifiUtils wifiUtils = new WifiUtils(getActivity());
		wifiUtils.getConnectedInfo();
		String connectedBSSID = wifiUtils.getConnectedBSSID();
		url = Url.BASE_URL + "BizReleaseJsp.action?mac000=";
		url += connectedBSSID;

		setWebViewCookie(url);
		setH5Camera(topicDetailWebView);
		topicDetailWebView.loadUrl(url);
	}

	private void setWebViewCookie(String url) {
		String url4load = url;
		CookieSyncManager.createInstance(getActivity());
		CookieManager cookieManager = CookieManager.getInstance();
		HttpCookie sessionCookie = MyCookieStore.cookie;
		if (sessionCookie != null) {
			String cookieString = sessionCookie.getName() + "="
					+ sessionCookie.getValue() + ";domain="
					+ sessionCookie.getDomain();
			LogUtil.e(cookieString);
			cookieManager.setCookie(url4load, cookieString);

			CookieSyncManager.getInstance().sync();
		}
	}

	// @Event(R.id.tv_comment)
	// private void sendComment(View view) {
	// startActivityForResult(new Intent(ShopFragment.this,
	// CommentActivity.class), COMMENT);
	//
	// }

	public void send(final String commentString) {

		if (commentString == null) {
			ToastShow.shortShowToast("评论不能为空！");
			return;
		}
		SessionRequestParams params = new SessionRequestParams(
				Url.SEND_TOPIC_COMMENT);
		params.addBodyParameter("huatbh", topicId);
		params.addBodyParameter("nrong0", commentString);

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
				CommonJson json = GsonUtils.parseJSON(result, CommonJson.class);
				String code = json.getCode();
				String msg = json.getMsg();
				if ("1".equals(code)) {
					ToastShow.shortShowToast("发表评论成功！");
					String url = "javascript:viewLastPinglun(" + "'"
							+ MyCookieStore.user.getTxiang() + "','"
							+ MyCookieStore.user.getHyming() + "','"
							+ TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")
							+ "','" + commentString + "')";
					topicDetailWebView.loadUrl(url);

				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});
	}

	@Event(R.id.iv_good)
	private void clickGood(View view) {

		SessionRequestParams params = new SessionRequestParams(Url.CLICK_GOOD);
		params.addBodyParameter("huatbh", topicId);

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
				CommonJson json = GsonUtils.parseJSON(result, CommonJson.class);
				String code = json.getCode();
				String msg = json.getMsg();
				if ("1".equals(code)) {
					ToastShow.shortShowToast("点赞成功！");
				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});

	}

	// @Event(R.id.iv_share)
	// private void clickShare(View view) {
	// Intent intent = new Intent(ShopFragment.this, WXEntryActivity.class);
	// intent.putExtra(Constant.SHARE_URL, Url.TOPIC_DETAIL_HTML + "?huatbh="
	// + topicId);
	// intent.putExtra(Constant.SHARE_TITLE, mHuatiList.getBiaoti());
	// intent.putExtra(Constant.SHARE_DESCRIPTION, mHuatiList.getNrong0());
	// startActivity(intent);
	// }

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (requestCode == REQUEST_SELECT_FILE) {
				if (uploadMessage == null)
					return;
				uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams
						.parseResult(resultCode, data));
				uploadMessage = null;
			}
		} else if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null
					: data.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		} else {
			// Toast.makeText(getBaseContext(), "Failed to Upload Image",
			// Toast.LENGTH_LONG).show();
		}

		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		// switch (requestCode) {
		// case COMMENT:
		//
		// send(data.getStringExtra(Constant.COMMENT_VALUE));
		// break;
		//
		// default:
		// break;
		// }
	}

	@Override
	public void onPause() {
		super.onPause();
		if (topicDetailWebView != null) {

			topicDetailWebView.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (topicDetailWebView != null) {
			Log.e("Url",url);
			topicDetailWebView.onResume();
			Log.e("你好","爸爸");
			initWebView();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (topicDetailWebView != null) {

			topicDetailWebView.destroy();
		}
	}

	// 以下全局变量要加
	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 2;

	// 以下方法要在 webView.loadUrl(url);之前调用
	// 将webview传进来 走一下下面封装好的方法就好了
	void setH5Camera(WebView v) {
		v.setWebChromeClient(new WebChromeClient() {

			// For 3.0+ Devices (Start)
			// onActivityResult attached before constructor
			protected void openFileChooser(ValueCallback uploadMsg,
					String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				startActivityForResult(Intent.createChooser(i, "文件管理器"),
						FILECHOOSER_RESULTCODE);
			}

			// For Lollipop 5.0+ Devices
			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			public boolean onShowFileChooser(WebView mWebView,
					ValueCallback<Uri[]> filePathCallback,
					WebChromeClient.FileChooserParams fileChooserParams) {
				if (uploadMessage != null) {
					uploadMessage.onReceiveValue(null);
					uploadMessage = null;
				}

				uploadMessage = filePathCallback;

				Intent intent = fileChooserParams.createIntent();
				try {
					startActivityForResult(intent, REQUEST_SELECT_FILE);
				} catch (ActivityNotFoundException e) {
					uploadMessage = null;
					Toast.makeText(getActivity(), "不能打开文件管理器",
							Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}

			// For Android 4.1 only
			protected void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "文件管理器"),
						FILECHOOSER_RESULTCODE);
			}

			protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				startActivityForResult(Intent.createChooser(i, "文件管理器"),
						FILECHOOSER_RESULTCODE);
			}

		});
	}

	// 当然调用系统相机或相册需要回到当前页面 需要把回传值处理一下
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	// if (requestCode == REQUEST_SELECT_FILE) {
	// if (uploadMessage == null)
	// return;
	// uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,
	// data));
	// uploadMessage = null;
	// }
	// } else if (requestCode == FILECHOOSER_RESULTCODE) {
	// if (null == mUploadMessage)
	// return;
	// Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null
	// : data.getData();
	// mUploadMessage.onReceiveValue(result);
	// mUploadMessage = null;
	// } else {
	// Toast.makeText(getBaseContext(), "Failed to Upload Image",
	// Toast.LENGTH_LONG).show();
	// }
	// }

}
