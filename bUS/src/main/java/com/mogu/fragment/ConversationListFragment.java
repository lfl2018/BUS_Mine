package com.mogu.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.Event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.mogu.activity.ChatActivity;
import com.mogu.activity.LoginActivity;
import com.mogu.activity.MsgCheckActivity;
import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.utils.ToastShow;
import com.mogu.utils.ValueUtils;

/**
 * conversation list fragment
 *
 */
public class ConversationListFragment extends Fragment {
	private final static int MSG_REFRESH = 2;
	protected EditText query;
	protected ImageButton clearSearch;
	protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
	protected EaseConversationList conversationListView;
	protected FrameLayout errorItemContainer;

	protected boolean isConflict;

	protected EMConversationListener convListener = new EMConversationListener() {

		@Override
		public void onCoversationUpdate() {
			refresh();
		}

	};

	private View mLayout;
	protected InputMethodManager inputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_conversation_list,
					container, false);
			x.view().inject(this, mLayout);
			inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			initView();
			setConversationListItemClickListener(new EaseConversationListItemClickListener() {

				@Override
				public void onListItemClicked(EMConversation conversation) {
					EMMessage message = conversation.getLastMessage();
					String id = message.getStringAttribute(EaseConstant.HYID,
							"");
					String userNick = null;
					String userAvatar = null;

					if (id.equalsIgnoreCase(ValueUtils
							.getHXId(MyCookieStore.user.getHybh00()))) {
						userNick = message.getStringAttribute(
								EaseConstant.RECEIVER_NICK, "");
						userAvatar = message.getStringAttribute(
								EaseConstant.RECEIVER_AVATAR, "");

					} else {
						userNick = message.getStringAttribute(
								EaseConstant.NICK, "");
						userAvatar = message.getStringAttribute(
								EaseConstant.AVATAR, "");

					}

					startActivity(new Intent(getActivity(), ChatActivity.class)
							.putExtra(EaseConstant.EXTRA_USER_ID,
									conversation.getLastMessage().getUserName())
							.putExtra(EaseConstant.RECEIVER_NICK, userNick)
							.putExtra(EaseConstant.RECEIVER_AVATAR, userAvatar));
				}
			});
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
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		conversationListView = (EaseConversationList) mLayout
				.findViewById(R.id.list);
		query = (EditText) mLayout.findViewById(R.id.query);
		// button to clear content in search bar
		clearSearch = (ImageButton) mLayout.findViewById(R.id.search_clear);
		errorItemContainer = (FrameLayout) mLayout
				.findViewById(R.id.fl_error_item);
	}

	private void setUpView() {
		conversationList.addAll(loadConversationList());
		conversationListView.init(conversationList);

		if (listItemClickListener != null) {
			conversationListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							EMConversation conversation = conversationListView
									.getItem(position);
							listItemClickListener
									.onListItemClicked(conversation);
						}
					});
		}

		EMClient.getInstance().addConnectionListener(connectionListener);

		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				conversationListView.filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				hideSoftKeyboard();
			}
		});

		conversationListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
	}

	protected EMConnectionListener connectionListener = new EMConnectionListener() {

		@Override
		public void onDisconnected(int error) {
			if (error == EMError.USER_REMOVED
					|| error == EMError.USER_LOGIN_ANOTHER_DEVICE
					|| error == EMError.SERVER_SERVICE_RESTRICTED) {
				isConflict = true;
			} else {
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onConnected() {
			handler.sendEmptyMessage(1);
		}
	};
	private EaseConversationListItemClickListener listItemClickListener;

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				onConnectionDisconnected();
				break;
			case 1:
				onConnectionConnected();
				break;

			case MSG_REFRESH: {
				conversationList.clear();
				conversationList.addAll(loadConversationList());
				conversationListView.refresh();
				break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * connected to server
	 */
	protected void onConnectionConnected() {
		errorItemContainer.setVisibility(View.GONE);
	}

	/**
	 * disconnected with server
	 */
	protected void onConnectionDisconnected() {
		errorItemContainer.setVisibility(View.VISIBLE);
	}

	/**
	 * refresh ui
	 */
	public void refresh() {
		if (!handler.hasMessages(MSG_REFRESH)) {
			handler.sendEmptyMessage(MSG_REFRESH);
		}
	}

	/**
	 * load conversation list
	 * 
	 * @return +
	 */
	protected List<EMConversation> loadConversationList() {
		// get all conversations
		Map<String, EMConversation> conversations = EMClient.getInstance()
				.chatManager().getAllConversations();
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		/**
		 * lastMsgTime will change if there is new message during sorting so use
		 * synchronized to make sure timestamp of last message won't change.
		 */
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * sort conversations according time stamp of last message
	 * 
	 * @param conversationList
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first.equals(con2.first)) {
							return 0;
						} else if (con2.first.longValue() > con1.first
								.longValue()) {
							return 1;
						} else {
							return -1;
						}
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

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EMClient.getInstance().removeConnectionListener(connectionListener);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isConflict) {
			outState.putBoolean("isConflict", true);
		}
	}

	public interface EaseConversationListItemClickListener {
		/**
		 * click event for conversation list
		 * 
		 * @param conversation
		 *            -- clicked item
		 */
		void onListItemClicked(EMConversation conversation);
	}

	/**
	 * set conversation list item click listener
	 * 
	 * @param listItemClickListener
	 */
	public void setConversationListItemClickListener(
			EaseConversationListItemClickListener listItemClickListener) {
		this.listItemClickListener = listItemClickListener;
	}

	@Event(R.id.tv_check)
	private void toMsgCheckActivity(View view) {
		SharedPreferences sp1 = getActivity().getSharedPreferences(
				Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		boolean islogin = sp1.getBoolean("islogin", false);

		if (!islogin) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
			ToastShow.shortShowToast("请登录");
			return;
		}

		Intent intent = new Intent(getActivity(), MsgCheckActivity.class);
		startActivity(intent);
	}

}
