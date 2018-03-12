package com.mogu.activity;

import java.net.HttpCookie;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mogu.activity.wxapi.WXEntryActivity;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.entity.txtnews.NewsList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TimeUtils;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;

public class HtmlNewsDetailActivity extends BaseActivity {

	public static final String SHARE_NEWS = "share_news";
	private static final int COMMENT = 0;
	private WebView newsDetailWebView;
	private String newsId = "";
	private NewsList mNewsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_news_detail);
		x.view().inject(this);
		TitleUtils.setTitle(this, R.id.top_bar, "详情");
		newsDetailWebView = (WebView) findViewById(R.id.wv_news_detail);
		mNewsList = (NewsList) getIntent().getSerializableExtra(
				Constant.NEWS_VALUE);
		newsId = mNewsList.getNewsbh();
		initWebView();
	}

	private void initWebView() {
		newsDetailWebView.requestFocus();
		newsDetailWebView.removeJavascriptInterface("searchBoxJavaBridge_");
		WebSettings settings = newsDetailWebView.getSettings();
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
		newsDetailWebView.setWebChromeClient(new WebChromeClient());

		newsDetailWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});

		newsDetailWebView.setOnTouchListener(new View.OnTouchListener() {

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

		String url = Url.NEWS_DETAIL_HTML + "?newsbh=" + newsId;
		setWebViewCookie(url);

		newsDetailWebView.loadUrl(url);
	}

	private void setWebViewCookie(String url) {
		String url4load = url;
		CookieSyncManager.createInstance(this);
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

	@Event(R.id.tv_comment)
	private void sendComment(View view) {
		startActivityForResult(new Intent(HtmlNewsDetailActivity.this,
				CommentActivity.class), COMMENT);

	}

	public void send(final String commentString) {

		if (commentString == null) {
			ToastShow.shortShowToast("评论不能为空！");
			return;
		}
		SessionRequestParams params = new SessionRequestParams(
				Url.SEND_NEWS_COMMENT);
		params.addBodyParameter("newsbh", newsId);
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
					String url = "javascript:showLastPinglun(" + "'"
							+ MyCookieStore.user.getTxiang() + "','"
							+ MyCookieStore.user.getHyming() + "','"
							+ TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")
							+ "','" + commentString + "')";
					newsDetailWebView.loadUrl(url);

				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});
	}

	@Event(R.id.iv_good)
	private void clickGood(View view) {

		SessionRequestParams params = new SessionRequestParams(Url.CLICK_GOOD);
		params.addBodyParameter("huatbh", newsId);

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

	@Event(R.id.iv_share)
	private void clickShare(View view) {
		Intent intent = new Intent(HtmlNewsDetailActivity.this,
				WXEntryActivity.class);
		intent.putExtra(Constant.SHARE_URL, Url.NEWS_DETAIL_HTML + "?newsbh="
				+ newsId);
		intent.putExtra(Constant.SHARE_TITLE, mNewsList.getBiaoti());
		intent.putExtra(Constant.SHARE_DESCRIPTION, mNewsList.getXwjsao());

		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case COMMENT:

			send(data.getStringExtra(Constant.COMMENT_VALUE));
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (newsDetailWebView != null) {

			newsDetailWebView.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (newsDetailWebView != null) {

			newsDetailWebView.onResume();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (newsDetailWebView != null) {

			newsDetailWebView.destroy();
		}
	}
}
