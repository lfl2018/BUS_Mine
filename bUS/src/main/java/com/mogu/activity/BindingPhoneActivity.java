package com.mogu.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mogu.app.Url;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.IsMobile;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

/**
 * 绑定手机界面
 * 
 */
public class BindingPhoneActivity extends BaseActivity implements
		OnClickListener {
	// 输入手机号码
	private EditText edtUserPhone;
	// 输入验证码
	private EditText edtRandomDel;
	// 输入密码
	private EditText edtPassword;
	// 发送验证码
	private Button btnSendRandom;
	private TimeCount time;
	// 绑定
	private Button btnBinding;

	private String phone;
	private String yanzhengma;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m_binding_phone);
		TitleUtils.setTitle(this, R.id.top_bar, "绑定手机");
		initView();
	}

	/**
	 * 绑定控件Id
	 */
	private void initView() {
		edtUserPhone = (EditText) findViewById(R.id.et_user_phone);
		edtRandomDel = (EditText) findViewById(R.id.yan_zheng_ma);
		edtPassword = (EditText) findViewById(R.id.et_pass_word);
		btnSendRandom = (Button) findViewById(R.id.btn_send_ramdon);
		btnBinding = (Button) findViewById(R.id.btn_binding);
		// 监听时间
		btnSendRandom.setOnClickListener(this);
		btnBinding.setOnClickListener(this);
		edtUserPhone.setOnFocusChangeListener(mOnFocusChangeListener);
		edtRandomDel.setOnFocusChangeListener(mOnFocusChangeListener);
		edtPassword.setOnFocusChangeListener(mOnFocusChangeListener);
	}

	/**
	 * EditText获取焦点时hint自动消失方法
	 */
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
			} else {
				hint = textView.getTag().toString();
				textView.setHint(hint);
			}
		}
	};

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnSendRandom.setText("重新发送");
			btnSendRandom.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnSendRandom.setClickable(false);
			btnSendRandom.setText("还剩" + millisUntilFinished / 1000 + "秒");
		}
	}

	/**
	 * 实现跳转到自身
	 * 
	 * @param context
	 */
	public static void actionIntent(Context context) {
		Intent intent = new Intent(context, BindingPhoneActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送验证码
		case R.id.btn_send_ramdon:

			sendYanzheng();
			break;
		// 绑定
		case R.id.btn_binding:
			binding();

			break;
		// 协议
		case R.id.tv_register_agreement:
			startActivity(new Intent().setClass(getApplicationContext(),
					ServiceAgreementActivity.class));
			break;
		}
	}

	private void sendYanzheng() {
		phone = edtUserPhone.getText().toString().trim();
		if (IsMobile.isMobileNO(phone)) {
			time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
			time.start();// 开始计时
			RequestParams params = new RequestParams(Url.YZM_URL);
			params.addBodyParameter("sjhm00", phone);
			x.http().post(params, new CommonCallback<String>() {

				@Override
				public void onCancelled(CancelledException arg0) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {
					// TODO Auto-generated method stub
					Log.e("..", arg0.getMessage());
				}

				@Override
				public void onFinished() {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(String arg0) {
					Log.e("   ", arg0);
					try {
						JSONObject obj = new JSONObject(arg0);
						if (obj.get("code").equals("1")) {
							Toast.makeText(BindingPhoneActivity.this,
									"验证码发送成功", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} else {
			Toast.makeText(BindingPhoneActivity.this, "请输入正确格式的手机号",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void binding() {
		// 验证验证码
		phone = edtUserPhone.getText().toString().trim();
		password = edtPassword.getText().toString();
		yanzhengma = edtRandomDel.getText().toString().trim();
		// String vericode = edtRandomDel.getText().toString().trim();
		Log.d("phone", phone);
		Log.d("pass", password);
		if (!IsMobile.isMobileNO(phone)) {
			Toast.makeText(BindingPhoneActivity.this, "手机号格式不正确",
					Toast.LENGTH_SHORT).show();
		} else if (password.contains(" ")) {
			Toast.makeText(BindingPhoneActivity.this, "密码不能包含空格",
					Toast.LENGTH_SHORT).show();
		} else if (password.length() < 6 || password.length() > 15) {
			Toast.makeText(BindingPhoneActivity.this, "密码为6-15位",
					Toast.LENGTH_SHORT).show();
		} else if (yanzhengma.length() < 6 || yanzhengma.length() > 6) {
			Toast.makeText(BindingPhoneActivity.this, "请输入6位验证码",
					Toast.LENGTH_SHORT).show();
		} else {
			RequestParams params = new RequestParams(Url.YZyzm_URL);
			params.addBodyParameter("sjhm00", phone);
			params.addBodyParameter("yzma00", yanzhengma);

			x.http().get(params, new CommonCallback<String>() {

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
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(String arg0) {
					// TODO Auto-generated method stub
					try {
						JSONObject obj = new JSONObject(arg0);
						if (obj.get("code").equals("1")) {
							bindingPhone(phone, password);
						} else {
							Toast.makeText(BindingPhoneActivity.this, "请检查验证码",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
	}

	/*
	 * 验证验证码后，绑定手机号码
	 */
	private void bindingPhone(String phone, String pass) {
		SessionRequestParams params = new SessionRequestParams(
				Url.BINDING_PHONE);
		params.addBodyParameter("sjhm00", phone);
		params.addBodyParameter("mima00", pass);
		x.http().post(params, new HttpCallBack() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {

			}

			@Override
			public void onFinished() {

			}

			@Override
			public void success(String arg0) {
				try {
					JSONObject obj = new JSONObject(arg0);
					if (obj.get("code").equals("1")) {
						Toast.makeText(BindingPhoneActivity.this, "绑定手机成功！",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(BindingPhoneActivity.this,
								obj.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
