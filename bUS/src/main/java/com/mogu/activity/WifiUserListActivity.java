package com.mogu.activity;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.ex.HttpException;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.adapter.WifiUserListAdapter;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.entity.login.Login;
import com.mogu.entity.wfuser.FriendsList;
import com.mogu.entity.wfuser.WfUser;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

/**
 * 同一wifi下的用户列表
 * 
 * @author Administrator
 *
 */
public class WifiUserListActivity extends Activity implements OnClickListener {

	private ListView projectListView;
	private View mLayout;
	private WifiUserListAdapter projectAdapter;
	private List<FriendsList> list = new ArrayList<FriendsList>();
	private Login user;
	public static boolean isProjectChanged = false;

	private PtrFrameLayout mPtrFrameLayout;
	private LoadMoreListViewContainer mLoadMoreListViewContainer;
	private int pageNum = 1;
	private int oldPageNum = 1;
	private String wifiMac;

	private String sex="";
	private TextView selectTextView;
	private String[] sexItems = { "男", "女", "全部", "取消" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_user_list);
		TitleUtils.setTitle(this, R.id.topbar, "人员");
		projectListView = (ListView) findViewById(R.id.lv_project);
		// projectAdapter = new ProjectAdapter(WifiUserListActivity.this,list);
		// projectListView.setAdapter(projectAdapter);

		selectTextView = (TextView) findViewById(R.id.topbar).findViewById(
				R.id.right);

		selectTextView.setText("筛选");

		selectTextView.setOnClickListener(this);

		initData();

		initPullToRefresh();
	}

	private void initData() {
		wifiMac = getIntent().getStringExtra(Constant.WIFI_MAC);
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
		// list.addAll(mList);
		// projectAdapter.notifyDataSetChanged();
		// LogUtils.i(list.size()+"");
		// }
		// });
	}

	private void initPullToRefresh() {
		// 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
		View headerMarginView = new View(WifiUserListActivity.this);
		headerMarginView.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
		projectListView.addHeaderView(headerMarginView);
		projectAdapter = new WifiUserListAdapter(WifiUserListActivity.this,
				list);
		projectListView.setAdapter(projectAdapter);
		// 3.设置下拉刷新组件和事件监听
		mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
		mPtrFrameLayout.setLoadingMinTime(100);
		mPtrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// here check list view, not content.
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						projectListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// 实现下拉刷新的功能
				pageNum = 1;
				updateListView();

			}

		});
		// 设置延时自动刷新数据
		mPtrFrameLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPtrFrameLayout.autoRefresh(false);
			}
		}, 200);
		// 4.加载更多的组件
		mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
		mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
		mLoadMoreListViewContainer.useDefaultFooter();
		// 5.添加加载更多的事件监听
		mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
			@Override
			public void onLoadMore(LoadMoreContainer loadMoreContainer) {
				if (mPtrFrameLayout.isRefreshing()) {
					return;
				}
				pageNum++;
				updateListView();
			}
		});
	}

	public void updateListView() {

		SessionRequestParams params = new SessionRequestParams(
				MrLiUrl.URL_Wifi_friends_list);

		params.addBodyParameter("pageIndex", pageNum + "");
		params.addBodyParameter("mac000", wifiMac + "");
		params.addBodyParameter("xbie00", sex + "");
		
		x.http().post(params, new HttpCallBack() {

			@Override
			public void success(String result) throws JSONException {
				WfUser json = GsonUtils.parseJSON(result, WfUser.class);
				List<FriendsList> mList = json.getFriendsList();
				oldPageNum = pageNum;
				if (pageNum == 1) {
					mPtrFrameLayout.refreshComplete();
					list.clear();
				}
				if (mList == null || mList.size() == 0) {
					if (pageNum == 1) {
						// 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
						mLoadMoreListViewContainer.loadMoreFinish(true, false);
					} else {
						mLoadMoreListViewContainer.loadMoreFinish(false, false);
					}
					projectAdapter.notifyDataSetChanged();
					return;
				}

				list.addAll(mList);
				mLoadMoreListViewContainer.loadMoreFinish(false, true);
				projectAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {

				if (pageNum == 1) {
					mPtrFrameLayout.refreshComplete();
					pageNum = oldPageNum;
					return;
				}
				// 以下是加载失败的情节
				int errorCode = 0;
				pageNum--;
				oldPageNum = pageNum;
				String errorMessage = "加载失败，点击加载更多";
				mLoadMoreListViewContainer.loadMoreError(errorCode,
						errorMessage);
				projectAdapter.notifyDataSetChanged();

			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isProjectChanged) {
			return;
		}
		isProjectChanged = false;
		pageNum = 1;
		updateListView();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.right:
			showSelectSexDialog();
			break;

		default:
			break;
		}
	}

	private void showSelectSexDialog() {
		// 展示一个选择性别的弹窗
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择性别")
				.setItems(sexItems, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:
							sex = "001";
							break;
						case 1:
							sex = "002";
							break;
						case 2:
							sex = "";
							break;
						default:
							break;
						}

						if (which != sexItems.length - 1) {
							pageNum = 1;
							updateListView();
						}

					}
				}).create();
		dialog.show();
	}

}
