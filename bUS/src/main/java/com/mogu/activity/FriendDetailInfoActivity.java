package com.mogu.activity;

import org.json.JSONException;
import org.xutils.x;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.exceptions.HyphenateException;
import com.mogu.app.Constant;
import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.entity.friend.Friend;
import com.mogu.entity.friend.SingleFriendJson;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;

/**
 * 详细资料页面
 * 
 * @author Administrator
 *
 */
public class FriendDetailInfoActivity extends Activity implements
		OnClickListener {
	private TextView tvName;
	private TextView tvSign;
	private TextView handleRelationTextView;
	private TextView emptyHistoryTextView;
	private ImageView ImgHead;

	private Friend friend;

	private String friendId;
	// 是否好友
	private boolean isFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_detail_info);
		friendId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
		isFriend = getIntent().getBooleanExtra(Constant.IS_FRIEND, true);
		TitleUtils.setTitle(this, R.id.topbar, "详细资料");
		initViews();
		initData();
	}

	private void initData() {
		SessionRequestParams params = new SessionRequestParams(
				Url.GET_USER_INFO_BYID);
		params.addBodyParameter("hybh00", friendId);
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
				SingleFriendJson json = GsonUtils.parseJSON(result,
						SingleFriendJson.class);
				String code = json.getCode();
				String msg = json.getMsg();
				if ("1".equals(code)) {
					friend = json.getUserHyb000();
					setInfo();
				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});
	}

	private void initViews() {

		tvName = (TextView) findViewById(R.id.tv_name);
		tvSign = (TextView) findViewById(R.id.tv_sign);
		handleRelationTextView = (TextView) findViewById(R.id.tv_relation_handle);
		emptyHistoryTextView = (TextView) findViewById(R.id.tv_empty_history);
		ImgHead = (ImageView) findViewById(R.id.img_head);

		handleRelationTextView.setOnClickListener(this);
		emptyHistoryTextView.setOnClickListener(this);
		if (isFriend) {
			handleRelationTextView.setText("加入黑名单");
			emptyHistoryTextView.setVisibility(View.VISIBLE);
		} else {
			handleRelationTextView.setText("加为好友");
			emptyHistoryTextView.setVisibility(View.INVISIBLE);
		}
	}

	public void setInfo() {
		tvName.setText(friend.getHyming());
		tvSign.setText(friend.getGxqm00());
		x.image().bind(ImgHead, ImageUtils.getImageUrl(friend.getTxiang()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_relation_handle:
			// 加入黑名单
			if (isFriend) {

				moveToBlacklist(friendId);
			} else {
				addFriend(friendId);
			}
			break;
		case R.id.tv_empty_history:
			String msg = getResources().getString(
					R.string.Whether_to_empty_all_chats);
			new EaseAlertDialog(FriendDetailInfoActivity.this, null, msg, null,
					new AlertDialogUser() {

						@Override
						public void onResult(boolean confirmed, Bundle bundle) {
							if (confirmed) {
								EMClient.getInstance().chatManager()
										.deleteConversation(friendId, true);
							}
						}
					}, true).show();
			break;

		default:
			break;
		}
	}

	private void addFriend(final String tobeRemoveUser) {
		if (friend == null) {
			ToastShow.shortShowToast("加好友失败！");
			return;
		}

		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.be_removing));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().contactManager()
							.removeUserFromBlackList(tobeRemoveUser);
					SessionRequestParams params = new SessionRequestParams(
							Url.SET_FRIEND_FLAG);
					params.addBodyParameter("frdbh0", friend.getHybh00());
					params.addBodyParameter("flag00", "1");
					x.http().post(params, new HttpCallBack() {

						@Override
						public void onFinished() {
							pd.dismiss();
						}

						@Override
						public void onError(Throwable arg0, boolean arg1) {

						}

						@Override
						public void onCancelled(CancelledException arg0) {

						}

						@Override
						public void success(String result) throws JSONException {
							CommonJson json = GsonUtils.parseJSON(result,
									CommonJson.class);
							String code = json.getCode();
							String msg = json.getMsg();

							if ("1".equals(code)) {
								ToastShow.shortShowToast("加好友成功！");
							} else {
								ToastShow.shortShowToast(msg);
							}
						}
					});

				} catch (HyphenateException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(),
									R.string.Removed_from_the_failure,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * move user to blacklist
	 */
	protected void moveToBlacklist(final String username) {
		final String st2 = getResources().getString(
				R.string.Move_into_blacklist_success);
		final String st3 = getResources().getString(
				R.string.Move_into_blacklist_failure);
		if (friend == null) {
			ToastShow.shortShowToast(st3);
			return;
		}
		final ProgressDialog pd = new ProgressDialog(
				FriendDetailInfoActivity.this);
		String st1 = getResources().getString(R.string.Is_moved_into_blacklist);

		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					// move to blacklist
					EMClient.getInstance().contactManager()
							.addUserToBlackList(username, false);
					SessionRequestParams params = new SessionRequestParams(
							Url.SET_FRIEND_FLAG);
					params.addBodyParameter("frdbh0", friend.getHybh00());
					params.addBodyParameter("flag00", "2");
					x.http().post(params, new HttpCallBack() {

						@Override
						public void onFinished() {
							pd.dismiss();
						}

						@Override
						public void onError(Throwable arg0, boolean arg1) {

						}

						@Override
						public void onCancelled(CancelledException arg0) {

						}

						@Override
						public void success(String result) throws JSONException {
							CommonJson json = GsonUtils.parseJSON(result,
									CommonJson.class);
							String code = json.getCode();
							String msg = json.getMsg();

							if ("1".equals(code)) {
								Toast.makeText(FriendDetailInfoActivity.this,
										st2, Toast.LENGTH_SHORT).show();
							} else {
								ToastShow.shortShowToast(msg);
							}
						}
					});
				} catch (HyphenateException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(FriendDetailInfoActivity.this, st3,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

}
