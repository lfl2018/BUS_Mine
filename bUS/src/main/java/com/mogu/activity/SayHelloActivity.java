package com.mogu.activity;

import org.json.JSONException;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.entity.common.CommonJson;
import com.mogu.entity.wfuser.FriendsList;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;
import com.xiaomi.network.HttpUtils;

/**
 * 打招呼页面
 * 
 * @author Administrator
 *
 */
public class SayHelloActivity extends Activity implements OnClickListener {
	private TextView tvSayHello;
	private EditText et_content;
	private String hybh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello);
		hybh = getIntent().getStringExtra(Constant.RECEIVE_ID);
		TitleUtils.setTitle(this, R.id.topbar, "打招呼");
		initViews();
	}

	private void initViews() {

		tvSayHello = (TextView) findViewById(R.id.tv_say_hello);
		et_content = (EditText) findViewById(R.id.et_content);
		tvSayHello.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_say_hello:
			sayHello();
			break;

		default:
			break;
		}
	}

	private void sayHello() {
		String content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			content = "你好";
		}
		SessionRequestParams params = new SessionRequestParams(
				MrLiUrl.URL_SAY_HELLO);
		params.addBodyParameter("content", content);
		params.addBodyParameter("to0000", hybh);
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
					ToastShow.shortShowToast("发送成功！");
					finish();
				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});

	}
}
