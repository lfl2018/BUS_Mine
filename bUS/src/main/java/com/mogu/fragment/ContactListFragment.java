package com.mogu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.mogu.activity.ChatActivity;
import com.mogu.activity.FriendDetailInfoActivity;
import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.app.Url;
import com.mogu.entity.friend.FriendJson;
import com.mogu.entity.friend.FriendList;
import com.mogu.utils.FriendListHttpCallBack;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ValueUtils;

/**
 * 联系人fragment
 *
 */
public class ContactListFragment extends Fragment {
	private List<EaseUser> contactList = new ArrayList<EaseUser>();
	private ListView listView;
	private EaseContactAdapter contactAdapter;

	private View mLayout;
	protected InputMethodManager inputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_contact_list,
					container, false);
			inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			initView();

			setUpView();

		}
		return mLayout;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		listView = (ListView) mLayout.findViewById(R.id.list);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadContactList();
	}

	private void setUpView() {

		contactAdapter = new EaseContactAdapter(getActivity(),
				R.layout.item_row_contact, contactList);
		listView.setAdapter(contactAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EaseUser user = contactList.get(position);
				if ("黑名单".equals(user.getInitialLetter())) {
					startActivity(new Intent(getActivity(),
							FriendDetailInfoActivity.class).putExtra(
							Constant.IS_FRIEND, false).putExtra(
							EaseConstant.EXTRA_USER_ID, user.getNickname()));
					return;
				}

				startActivity(new Intent(getActivity(), ChatActivity.class)
						.putExtra(EaseConstant.EXTRA_USER_ID,
								ValueUtils.getHXId(user.getNickname()))
						.putExtra(EaseConstant.RECEIVER_NICK,
								user.getUsername())
						.putExtra(EaseConstant.RECEIVER_AVATAR,
								user.getAvatar()));
			}
		});

	}

	// private void loadConversationList() {
	// List<EaseUser> mList = new ArrayList<EaseUser>();
	// EaseUser mEaseUser = new EaseUser("xzz");
	// mList.add(mEaseUser);
	//
	// mEaseUser=new EaseUser("aaa");
	// mEaseUser.setInitialLetter("好友");
	// mList.add(mEaseUser);
	//
	// mEaseUser=new EaseUser("xaa");
	// mEaseUser.setInitialLetter("好友");
	// mList.add(mEaseUser);
	//
	// mEaseUser=new EaseUser("xaa");
	// mList.add(mEaseUser);
	//
	// }

	private void loadContactList() {
		contactList.clear();
		contactAdapter.notifyDataSetChanged();
		loadFriendList();
		loadBlackList();
	}

	private void loadFriendList() {
		SessionRequestParams params = new SessionRequestParams(
				Url.GET_FRIENDS_LIST);
		params.addBodyParameter("flag00", "1");
		x.http().post(params, new FriendListHttpCallBack() {

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
				FriendJson json = GsonUtils.parseJSON(result, FriendJson.class);
				List<FriendList> friendList = json.getFriendList();
				List<EaseUser> mList = new ArrayList<EaseUser>();

				for (FriendList friend : friendList) {
					EaseUser mEaseUser = new EaseUser(friend.getHyming());
					mEaseUser.setNickname(friend.getHybh00());
					mEaseUser.setInitialLetter("好友");
					mEaseUser.setAvatar(ImageUtils.getImageUrl(friend
							.getTxiang()));
					mList.add(mEaseUser);

				}
				contactList.addAll(0, mList);
				contactAdapter.notifyDataSetChanged();
			}
		});
	}

	private void loadBlackList() {
		SessionRequestParams params = new SessionRequestParams(
				Url.GET_FRIENDS_LIST);
		params.addBodyParameter("flag00", "2");
		x.http().post(params, new FriendListHttpCallBack() {

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
				FriendJson json = GsonUtils.parseJSON(result, FriendJson.class);
				List<FriendList> friendList = json.getFriendList();
				List<EaseUser> mList = new ArrayList<EaseUser>();

				for (FriendList friend : friendList) {
					EaseUser mEaseUser = new EaseUser(friend.getHyming());
					mEaseUser.setNickname(friend.getHybh00());
					mEaseUser.setInitialLetter("黑名单");
					mEaseUser.setAvatar(ImageUtils.getImageUrl(friend
							.getTxiang()));

					mList.add(mEaseUser);

				}
				contactList.addAll(mList);
				contactAdapter.notifyDataSetChanged();
			}
		});
	}

	protected void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
