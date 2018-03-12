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
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mogu.adapter.MsgCheckAdapter;
import com.mogu.app.Url;
import com.mogu.entity.yanzen.HelloMsg;
import com.mogu.entity.yanzen.MsgList;
import com.mogu.entity.yanzen.MsgList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
/**
 * 好友验证页面
 * @author Administrator
 *
 */
public class MsgCheckActivity extends BaseActivity {
	private ListView lv_msg_check;
	private MsgCheckAdapter msgCheckAdapter;
	private List<MsgList> list = new ArrayList<MsgList>();
	private PtrFrameLayout mPtrFrameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	public void initView() {
		setContentView(R.layout.activity_msg_check);
		TitleUtils.setTitle(MsgCheckActivity.this, R.id.topbar, "好友验证");
		initPullToRefresh();
	}

	public void initData() {

	}

	private void initPullToRefresh() {
		// 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
		lv_msg_check = (ListView) findViewById(R.id.lv_msg_check);
		View headerMarginView = new View(this);
		headerMarginView.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
		lv_msg_check.addHeaderView(headerMarginView);
		msgCheckAdapter = new MsgCheckAdapter(this, list);
		lv_msg_check.setAdapter(msgCheckAdapter);
		// 3.设置下拉刷新组件和事件监听
		mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
		mPtrFrameLayout.setLoadingMinTime(100);
		mPtrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// here check list view, not content.
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						lv_msg_check, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// 实现下拉刷新的功能
				LogUtil.i("-----onRefreshBegin-----");
				updateListView();

			}

		});
		updateListView();
	}

	public void updateListView() {
		SessionRequestParams params = new SessionRequestParams(
				Url.GET_CHECK_LIST);

		x.http().post(params, new HttpCallBack() {

			@Override
			public void success(String result) throws JSONException {
				HelloMsg json = GsonUtils.parseJSON(result, HelloMsg.class);
				List<MsgList> mList = json.getMsgList();
				list.clear();

				list.addAll(mList);
				msgCheckAdapter.notifyDataSetChanged();
				LogUtil.i(list.size() + "");
			}

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinished() {
				mPtrFrameLayout.refreshComplete();
			}

		});
	}

}
