package com.mogu.activity;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.util.LogUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mogu.adapter.CommentListAdapter;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.comment.Comment;
import com.mogu.entity.comment.CommentJson;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

public class TopicDetailActivity extends BaseActivity {
	private ListView commentListView;
	private CommentListAdapter myPubAdapter;
	private List<Comment> list = new ArrayList<Comment>();
	public static boolean isProjectAdded = false;

	private PtrFrameLayout mPtrFrameLayout;
	private LoadMoreListViewContainer mLoadMoreListViewContainer;
	private int pageNum = 1;
	private String topicId = "";

	private WebView topicDetailWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		x.view().inject(this);
		TitleUtils.setTitle(this, R.id.top_bar, "详情");
		topicId = getIntent().getStringExtra(Constant.TOPIC_VALUE);
		commentListView = (ListView) findViewById(R.id.lv_comment);
		// findSearchAdapter = new findSearchAdapter(getActivity(),list);
		// projectListView.setAdapter(findSearchAdapter);
		// rightImageView.setImageResource(R.drawable.ic_creat_diary);

		commentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		initPullToRefresh();
		GetMydata();
		initWebView();
	}

	private void initWebView() {
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
		
		topicDetailWebView.setOnTouchListener ( new View.OnTouchListener () {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction ()) {
                case MotionEvent.ACTION_DOWN :
                case MotionEvent.ACTION_UP :
                    if (!v.hasFocus ()) {
                       v.requestFocus ();
                    }
                    break ;
                }
                return false ;
            }
        });
		
		String url = Url.TOPIC_DETAIL_HTML + "?huatbh=" + topicId;
		setWebViewCookie(url);

		topicDetailWebView.loadUrl(url);
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

	private void GetMydata() {
		SessionRequestParams params = new SessionRequestParams(
				Url.TOPIC_COMMENT_LIST);
		params.addBodyParameter("huatbh", topicId);
		x.http().post(params, new HttpCallBack() {

			@Override
			public void onFinished() {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("嗷嗷", "爱迪生失败");
				int errorCode = 0;
				String errorMessage = "加载失败，点击加载更多";
				mLoadMoreListViewContainer.loadMoreError(errorCode,
						errorMessage);
				myPubAdapter.notifyDataSetChanged();
				mPtrFrameLayout.refreshComplete();
			}

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void success(String result) throws JSONException {
				CommentJson json = GsonUtils.parseJSON(result,
						CommentJson.class);
				List<Comment> mlist = json.getHuatiList();
				list.addAll(mlist);
				myPubAdapter.notifyDataSetChanged();
				Log.e("嗷嗷", "爱迪生88    " + list.size());
				Log.e("嗷嗷", "爱迪生");
				mPtrFrameLayout.refreshComplete();
			}
		});
		// params.addBodyParameter("nrong", "ss");

	}

	private void initPullToRefresh() {
		// 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
		// View headerMarginView = new View(TopicDetailActivity.this);
		// headerMarginView.setLayoutParams(new AbsListView.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
		View headerView = LayoutInflater.from(TopicDetailActivity.this)
				.inflate(R.layout.head_topic_detail, null);
		topicDetailWebView = (WebView) headerView
				.findViewById(R.id.wv_topic_detail);
		commentListView.addHeaderView(headerView);
		myPubAdapter = new CommentListAdapter(TopicDetailActivity.this, list);
		commentListView.setAdapter(myPubAdapter);
		// 3.设置下拉刷新组件和事件监听
		mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
		mPtrFrameLayout.setLoadingMinTime(100);
		mPtrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// here check list view, not content.
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						commentListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// 实现下拉刷新的功能
				list.clear();
				GetMydata();
			}

		});
		// 设置延时自动刷新数据
		// mPtrFrameLayout.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// mPtrFrameLayout.autoRefresh(true);
		// }
		// }, 200);
		// 4.加载更多的组件
		mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
		mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
		mLoadMoreListViewContainer.useDefaultFooter();
		// 5.添加加载更多的事件监听
		mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
			@Override
			public void onLoadMore(LoadMoreContainer loadMoreContainer) {
				// pageNum++;
				// updateListView(pageNum);
				list.clear();
				GetMydata();
			}
		});
	}

	// public void updateListView(final int pageNum) {
	// HttpUtils http = new HttpUtils();
	// http.configCurrentHttpCacheExpiry(100);
	// SessionRequestParams params = new SessionRequestParams();
	//
	// params.addBodyParameter("pageNum", pageNum+"");
	//
	// http.send(HttpMethod.POST, Url.PROJECT_LIST, params, new HttpCallBack() {
	//
	// @Override
	// public void success(String result) throws JSONException {
	// ProjectJson json=GsonUtils.parseJSON(result, ProjectJson.class);
	// List<FindSearchItem> mList=json.get;
	// if (pageNum==1) {
	// mPtrFrameLayout.refreshComplete();
	// list.clear();
	// }
	// if (mList==null||mList.size()==0) {
	// LogUtils.i("null");
	// if (pageNum==1) {
	// // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
	// mLoadMoreListViewContainer.loadMoreFinish(true,
	// false);
	// }else {
	// mLoadMoreListViewContainer.loadMoreFinish(false,
	// false);
	// }
	// findSearchAdapter.notifyDataSetChanged();
	// return;
	// }
	//
	// list.addAll(mList);
	// mLoadMoreListViewContainer.loadMoreFinish(false,
	// true);
	// findSearchAdapter.notifyDataSetChanged();
	// LogUtils.i(list.size()+"");
	// }
	//
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// super.onFailure(arg0, arg1);
	// if (pageNum==1) {
	// mPtrFrameLayout.refreshComplete();
	// }
	// // 以下是加载失败的情节
	// int errorCode = 0;
	// String errorMessage = "加载失败，点击加载更多";
	// mLoadMoreListViewContainer.loadMoreError(errorCode,
	// errorMessage);
	// findSearchAdapter.notifyDataSetChanged();
	//
	// }
	//
	// });
	// }
	//
	@Override
	public void onResume() {
		super.onResume();
		if (!isProjectAdded) {
			return;
		}
		// pageNum=1;
		// updateListView(pageNum);
		// HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(100);
		// RequestParams params = new RequestParams();
		//
		// params.addHeader("Cookie", "JSESSIONID=" + user.getSessionid());
		//
		// http.send(HttpMethod.POST, Url.PROJECT_LIST, params, new
		// HttpCallBack() {
		//
		// @Override
		// public void success(String result) throws JSONException {
		// Project json=GsonUtils.parseJSON(result, Project.class);
		// List<Listproject> mList=json.getListproject();
		// list.clear();
		// list.addAll(mList);
		// findSearchAdapter.notifyDataSetChanged();
		// isProjectAdded=false;
		// LogUtils.i(list.size()+"");
		// }
		// });
	}

}
