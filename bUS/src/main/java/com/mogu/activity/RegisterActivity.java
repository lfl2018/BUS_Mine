package com.mogu.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mogu.activity.R.drawable;
import com.mogu.app.Url;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

/**
 * 注册界面
 * 
 * @author quanyi
 * @date 2015年8月28日
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	// 输入手机号码
	private EditText edtUserPhone;
	// 输入验证码
	private EditText edtRandomDel;
	// 输入新密码
	private EditText edtNewPassworDel;
	// 发送验证码
	private Button btnSendRandom;
	private TimeCount time;
	// 注册
	private Button btnRegister;
	// 注册协议
	private TextView tvRegister;
	// top顶部内容，返回 提示 下一步


	private String mobile;
	private CheckBox agree;

	private String phone;
	private String yanzhengma;
	private String password;
	private String userid;
	private int modelid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m_regist);
		initView();
		btnRegister.setClickable(false);
		btnRegister.setBackgroundResource(drawable.edt_shap);
		agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1 != true) {
					btnRegister.setClickable(arg1);
					btnRegister.setBackgroundResource(drawable.edt_shap);
				} else {
					btnRegister.setClickable(arg1);
					btnRegister.setBackgroundResource(drawable.btn_selector);
				}

			}
		});
	}

	/**
	 * 绑定控件Id
	 */
	private void initView() {
		TitleUtils.setTitle(RegisterActivity.this, R.id.topbar, "注册");
		agree = (CheckBox) findViewById(R.id.regist_check_box);
		edtUserPhone = (EditText) findViewById(R.id.userPhone);
		edtRandomDel = (EditText) findViewById(R.id.yan_zheng_ma);
		edtNewPassworDel = (EditText) findViewById(R.id.new_pass_word);
		btnSendRandom = (Button) findViewById(R.id.btn_register_sendRamdon);
		btnRegister = (Button) findViewById(R.id.btn_regist);
		tvRegister = (TextView) findViewById(R.id.tv_register_agreement);

		// 设置文本下划线
		tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvRegister.setTextColor(Color.BLUE);
		// 设置顶部标题
		// 监听时间
		btnSendRandom.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		edtUserPhone.setOnFocusChangeListener(mOnFocusChangeListener);
		edtRandomDel.setOnFocusChangeListener(mOnFocusChangeListener);
		edtNewPassworDel.setOnFocusChangeListener(mOnFocusChangeListener);
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
		Intent intent = new Intent(context, RegisterActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送验证码
		case R.id.btn_register_sendRamdon:

			Sendyanzheng();
			break;
		// 注册
		case R.id.btn_regist:
			rigister();

			break;
		// 注册协议
		case R.id.tv_register_agreement:
			startActivity(new Intent().setClass(getApplicationContext(),
					ServiceAgreementActivity.class));
			break;
		case R.id.btn_return:
			finish();
			break;
		}
	}

	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();

	}

	private void Sendyanzheng() {
		phone = edtUserPhone.getText().toString().trim();
		if (isMobileNum(phone)) {
			time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
			time.start();// 开始计时
			RequestParams params = new RequestParams(Url.YZM_URL);
			params.addBodyParameter("sjhm00", phone);
			x.http().post(params, new CommonCallback<String>() {

				@Override
				public void onCancelled(CancelledException arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(RegisterActivity.this, "嗷嗷",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {
					// TODO Auto-generated method stub
					Log.e("..", arg0.getMessage());
				}

				@Override
				public void onFinished() {
					// TODO Auto-generated method stub
					Toast.makeText(RegisterActivity.this, "完成",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(String arg0) {
					Log.e("   ", arg0);
					// TODO Auto-generated method stub
					try {
						JSONObject obj = new JSONObject(arg0);
						if (obj.get("code").equals("1")) {
							Toast.makeText(RegisterActivity.this, "验证码发送成功",
									Toast.LENGTH_SHORT).show();
						} 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} else {
			Toast.makeText(RegisterActivity.this, "输入正确格式手机号",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void rigister() {
		phone = edtUserPhone.getText().toString().trim();
		password = edtNewPassworDel.getText().toString();
		yanzhengma = edtRandomDel.getText().toString().trim();
		Log.e("asadsadasdasdasd", "111" + password + "2222");
		// String vericode = edtRandomDel.getText().toString().trim();
		Log.d("phone", phone);
		Log.d("pass", password);
		if (!isMobileNum(phone)) {
			Toast.makeText(RegisterActivity.this, "手机号格式不正确",
					Toast.LENGTH_SHORT).show();
		} else if (password.contains(" ")) {
			Toast.makeText(RegisterActivity.this, "密码不能包含空格",
					Toast.LENGTH_SHORT).show();
		} else if (password.length() < 6 || password.length() > 15) {
			Toast.makeText(RegisterActivity.this, "密码为6-15位",
					Toast.LENGTH_SHORT).show();
		} else if (yanzhengma.length() < 6 || yanzhengma.length() > 6) {
			Toast.makeText(RegisterActivity.this, "请输入6位验证码",
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
							MyRegist(phone, password);
						} else {
							Toast.makeText(RegisterActivity.this, "请检查验证码",
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

	private void MyRegist(String phone, String pass) {
		RequestParams params = new RequestParams(Url.REGIST_URL);
		params.addBodyParameter("sjhm00", phone);
		params.addBodyParameter("mima00", pass);
		params.addBodyParameter("agmima", pass);
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
				try {
					JSONObject obj = new JSONObject(arg0);
					if (obj.get("code").equals("1")) {
						Toast.makeText(RegisterActivity.this, "恭喜您注册成功",
								Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(RegisterActivity.this, obj.getString("msg"),
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
