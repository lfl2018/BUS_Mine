package com.mogu.activity;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.mogu.app.Url;
import com.mogu.entity.common.CommonJson;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangePasswordActivity extends Activity {

	// 输入旧密码
	@ViewInject(R.id.et_old_password)
	private EditText oldPasswordEditText;

	// 输入新密码
	@ViewInject(R.id.et_new_password)
	private EditText newPasswordEditText;

	// 确认密码
	@ViewInject(R.id.et_confirm_password)
	private EditText confirmPasswordEditText;
	
	// 保存
	private Button btnSendRandom;

	private String oldPasswordString;
	private String newPasswordString;
	private String confirmPasswordString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		x.view().inject(this);
		TitleUtils.setTitle(this, R.id.top_bar, "修改密码");
	}

	@Event(R.id.btn_save)
	private void toChangeAvatar(View view) {
		oldPasswordString = oldPasswordEditText.getText().toString();
		newPasswordString = newPasswordEditText.getText().toString();
		confirmPasswordString = confirmPasswordEditText.getText().toString();
		
		if (!newPasswordString.equals(confirmPasswordString)) {
			ToastShow.shortShowToast("两次密码输入不一样！");
			return;
		}
		SessionRequestParams params = new SessionRequestParams(Url.UPDATE_PASSWORD);
		params.addBodyParameter("oldmima", oldPasswordString);
		params.addBodyParameter("newmima", newPasswordString);
		params.addBodyParameter("agmima", confirmPasswordString);

		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastShow.shortShowToast("修改密码失败！");
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				CommonJson json = GsonUtils.parseJSON(arg0, CommonJson.class);
				String code = json.getCode();
				String msg = json.getMsg();
				if ("1".equals(code)) {
					ToastShow.shortShowToast("修改密码成功！");
					finish();
				} else {
					ToastShow.shortShowToast(msg);

				}
			}
		});
	}

}
