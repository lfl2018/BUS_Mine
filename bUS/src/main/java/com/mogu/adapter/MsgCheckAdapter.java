package com.mogu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.mogu.activity.MsgCheckActivity;
import com.mogu.activity.R;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.entity.yanzen.MsgList;
import com.mogu.entity.yanzen.MsgList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;
import com.mogu.utils.ValueUtils;
import com.xiaomi.network.HttpUtils;

public class MsgCheckAdapter extends BaseAdapter {

	private Activity context;

	private List<MsgList> list = new ArrayList<MsgList>();

	public MsgCheckAdapter(Activity context, List<MsgList> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_msg_check, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.head = (ImageView) convertView.findViewById(R.id.img_head);
			holder.agree = (TextView) convertView.findViewById(R.id.tv_agree);
			holder.agreed = (TextView) convertView.findViewById(R.id.tv_agreed);
			holder.content = (TextView) convertView.findViewById(R.id.tv_msg);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getHyming());
		holder.content.setText(list.get(position).getContnt());
		x.image().bind(holder.head,
				ImageUtils.getImageUrl(list.get(position).getTxiang()));
//		if (list.get(position).getYzztai().equals("001")) {
//			holder.agree.setVisibility(View.VISIBLE);
//			holder.agreed.setVisibility(View.GONE);
//		} else {
//			holder.agreed.setVisibility(View.VISIBLE);
//			holder.agree.setVisibility(View.GONE);
//		}
		final int index = position;
		holder.agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 加好友
				SessionRequestParams params = new SessionRequestParams(MrLiUrl.URL_HANDLER_HELLO);

				params.addBodyParameter("msgbh0", list.get(index)
						.getMsgbh0() + "");
				params.addBodyParameter("ret000", "1");
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
						CommonJson json=GsonUtils.parseJSON(result, CommonJson.class);
						String code = json.getCode();
						String msg = json.getMsg();
						if ("1".equals(code)) {
							ToastShow.shortShowToast("添加好友成功！");
							
				            EMMessage message = EMMessage.createTxtSendMessage("我是"+MyCookieStore.user.getHyming(), ValueUtils.getHXId(list.get(index).getFrom00()));
				            // 增加自己特定的属性
				            message.setAttribute(EaseConstant.NICK, MyCookieStore.user.getHyming());
				            message.setAttribute(EaseConstant.AVATAR, ImageUtils.getImageUrl(MyCookieStore.user.getTxiang()));
				            message.setAttribute(EaseConstant.HYID, MyCookieStore.user.getHybh00());
				            message.setAttribute(EaseConstant.RECEIVER_NICK, list.get(index).getHyming());
				            message.setAttribute(EaseConstant.RECEIVER_AVATAR, ImageUtils.getImageUrl(list.get(index).getTxiang()));
							//send message
					        EMClient.getInstance().chatManager().sendMessage(message);
					        
					        list.remove(index);
					        notifyDataSetChanged();
					        
						}else {
							ToastShow.shortShowToast(msg);
						}
					}
				});
			}
		});
		return convertView;
	}

	private static class ViewHolder {

		TextView name;
		ImageView head;
		TextView content;
		TextView agree;
		TextView agreed;
	}

}
