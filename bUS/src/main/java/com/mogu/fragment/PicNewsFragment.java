package com.mogu.fragment;

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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mogu.activity.HtmlNewsDetailActivity;
import com.mogu.activity.PubTopicActivity;
import com.mogu.activity.R;
import com.mogu.adapter.PicNewsAdapter;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.entity.txtnews.NewsList;
import com.mogu.entity.txtnews.Newstxtnews;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;

/**
 * 图片新闻类
 * 
 * @author Administrator
 *
 */
public class PicNewsFragment extends Fragment implements OnClickListener {
	private ListView projectListView;
	private PicNewsAdapter picNewsAdapter;
	private List<NewsList> list = new ArrayList<NewsList>();
	public static boolean isProjectAdded = false;

	private PtrFrameLayout mPtrFrameLayout;
	private LoadMoreListViewContainer mLoadMoreListViewContainer;
	private int pageNum = 1;
	private int oldPageNum = 1;
	private View MLayout;
	private TextView pub;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (MLayout == null) {
			MLayout = inflater.inflate(R.layout.fragment_picnews, null);
			// TitleUtils.setTitle(getActivity(), R.id.topbar, "我的帖子");
			// pub = (TextView) MLayout.findViewById(R.id.pub);
			// pub.setOnClickListener(this);
			projectListView = (ListView) MLayout.findViewById(R.id.lv_project);
			// findSearchAdapter = new findSearchAdapter(getActivity(),list);
			// projectListView.setAdapter(findSearchAdapter);
			// rightImageView.setImageResource(R.drawable.ic_creat_diary);

			projectListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					startActivity(new Intent(getActivity(),
							HtmlNewsDetailActivity.class).putExtra(
							Constant.NEWS_VALUE, list.get(position - 1)));
				}
			});

			initPullToRefresh();
			GetMydata();
		}

		return MLayout;
	}

	private void GetMydata() {
		SessionRequestParams params = new SessionRequestParams(MrLiUrl.URL_News);
		params.addBodyParameter("xwlmbh", "1002");
		x.http().post(params, new HttpCallBack() {

			@Override
			public void onFinished() {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.e("嗷嗷", "爱迪生失败");
				int errorCode = 0;
				String errorMessage = "加载失败，点击加载更多";
				mLoadMoreListViewContainer.loadMoreError(errorCode,
						errorMessage);
				picNewsAdapter.notifyDataSetChanged();
				mPtrFrameLayout.refreshComplete();
			}

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void success(String result) throws JSONException {
				// TODO Auto-generated method stub
				Newstxtnews json = GsonUtils.parseJSON(result,
						Newstxtnews.class);
				List<NewsList> mlist = json.getNewsList();
				oldPageNum = pageNum;
				// if (pageNum==1) {
				// mPtrFrameLayout.refreshComplete();
				// list.clear();
				// }
				list.clear();
//				if (mlist == null || mlist.size() == 0) {
//					LogUtils.i("null");
//					if (pageNum == 1) {
//						// 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
//						mLoadMoreListViewContainer.loadMoreFinish(true, false);
//					} else {
//						mLoadMoreListViewContainer.loadMoreFinish(false, false);
//					}
//
//				}

				list.addAll(mlist);
				
				mLoadMoreListViewContainer.loadMoreFinish(false, false);
				picNewsAdapter.notifyDataSetChanged();
				mPtrFrameLayout.refreshComplete();
				Log.e("嗷嗷", "爱迪生88    " + list.size());
				Log.e("嗷嗷", "爱迪生");
				// mPtrFrameLayout.refreshComplete();
			}
		});
		// params.addBodyParameter("nrong", "ss");

	}

	private void initPullToRefresh() {
		// 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
		View headerMarginView = new View(getActivity());
		headerMarginView.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
		projectListView.addHeaderView(headerMarginView);
		picNewsAdapter = new PicNewsAdapter(getActivity(), list);
		projectListView.setAdapter(picNewsAdapter);
		// 3.设置下拉刷新组件和事件监听
		mPtrFrameLayout = (PtrFrameLayout) MLayout
				.findViewById(R.id.load_more_list_view_ptr_frame);
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
				GetMydata();
			}

		});
		// 设置延时自动刷新数据
		// mPtrFrameLayout.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// mPtrFrameLayout.autoRefresh(false);
		// }
		// }, 200);
		// 4.加载更多的组件
		mLoadMoreListViewContainer = (LoadMoreListViewContainer) MLayout
				.findViewById(R.id.load_more_list_view_container);
		mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
		mLoadMoreListViewContainer.useDefaultFooter();
		// 5.添加加载更多的事件监听
		mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
			@Override
			public void onLoadMore(LoadMoreContainer loadMoreContainer) {
				// pageNum++;
				// updateListView(pageNum);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pub:
			startActivity(new Intent(getActivity(), PubTopicActivity.class));
			break;

		default:
			break;
		}
	}

}
