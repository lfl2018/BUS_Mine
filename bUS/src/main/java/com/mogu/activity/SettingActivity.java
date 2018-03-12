package com.mogu.activity;

import java.io.File;
import java.text.DecimalFormat;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.mogu.app.AppConfig;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.update.Version;
import com.mogu.utils.AppUtils;
import com.mogu.utils.DataCleanManager;
import com.mogu.utils.FileUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.IsMobile;
import com.mogu.utils.NetworkUtil;
import com.mogu.utils.NotifycationMr;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;

public class SettingActivity extends Activity implements OnClickListener {
	// 升级相关
	private boolean mCheckUpdateToastLock = false;// 更新提示锁
	private AlertDialog checkUpdateDialog = null;
	// 控件
	private TextView aboutme;
	private TextView tvclean;
	private TextView tvnewVersion;
	private TextView tvInfo;
	private TextView tvChangepass;
	private boolean islogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		tvnewVersion = (TextView) findViewById(R.id.tv_newversion);
		tvInfo = (TextView) findViewById(R.id.tv_my_info);
		tvChangepass = (TextView) findViewById(R.id.tv_change_pass);
		tvInfo.setOnClickListener(this);
		tvChangepass.setOnClickListener(this);

		tvnewVersion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkUpdateOrInstall();
			}
		});

		tvclean = (TextView) findViewById(R.id.tv_clean);
		tvclean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder builder = new Builder(SettingActivity.this)
						.setMessage("清除缓存会退出当前账号")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SessionRequestParams params = new SessionRequestParams(
												Url.LOGOUT_URL);
										x.http().post(params,
												new CommonCallback<String>() {

													@Override
													public void onCancelled(
															CancelledException arg0) {

													}

													@Override
													public void onError(
															Throwable arg0,
															boolean arg1) {

													}

													@Override
													public void onFinished() {

													}

													@Override
													public void onSuccess(
															String arg0) {

													}
												});

										SharedPreferences sp1 = getSharedPreferences(
												Constant.MAIN_SHARE_FILE_NAME,
												Context.MODE_PRIVATE);

										sp1.edit().clear().commit();
										EMClient.getInstance().logout(true);

										DataCleanManager.cleanApplicationData(
												SettingActivity.this,
												FileUtils.SDPATH);

										startActivity(new Intent(
												SettingActivity.this,
												LoginActivity.class));
										finish();
									}
								}).setNegativeButton("取消", null);
				builder.show();
			}
		});
		aboutme = (TextView) findViewById(R.id.tv_about_me);

		aboutme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog builder = new Builder(SettingActivity.this)
						.setMessage("更多精彩\n  尽在无限城网络\n    http://www.wxcw.net")
						.setPositiveButton("确定", null).create();
				builder.show();
			}
		});
		x.view().inject(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("设置");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 1. 方法必须私有限定, 2. 方法参数形式必须和type对应的Listener接口一致. 3. 注解参数value支持数组:
	 * value={id1, id2, id3} 4. 其它参数说明见{@link org.xutils.event.annotation.Event}
	 * 类的说明.
	 **/
	@Event(value = R.id.tv_logout)
	private void logout(View view) {

		Builder builder = new Builder(SettingActivity.this)
				.setMessage("执行此操作会退出当前账号")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SessionRequestParams params = new SessionRequestParams(
								Url.LOGOUT_URL);
						x.http().post(params, new CommonCallback<String>() {

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
							public void onSuccess(String arg0) {

							}
						});

						SharedPreferences sp1 = getSharedPreferences(
								Constant.MAIN_SHARE_FILE_NAME,
								Context.MODE_PRIVATE);

						sp1.edit().clear().commit();
						EMClient.getInstance().logout(true);

						startActivity(new Intent(SettingActivity.this,
								LoginActivity.class));
						finish();
					}
				}).setNegativeButton("取消", null);
		builder.show();

	}

	/**
	 * 检测新版本
	 * 
	 */
	private void checkUpdateOrInstall() {
		boolean isResetFlag = true;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {// 是否挂载sd卡
			LogUtil.i("sd卡ok");
			File file;
			try {
				file = new File(Environment.getExternalStorageDirectory()
						+ AppConfig.apkUpdateDir + "/" + AppConfig.apkFileName
						+ AppConfig.FILE_SUFFIX);
			} catch (Exception e) {
				mCheckUpdateToastLock = false;
				throw new RuntimeException(e);
			}
			if (file.exists()) {

				LogUtil.i("文件存在");
				PackageInfo info = AppUtils.getApkInfo(this,
						file.getAbsolutePath());
				try {
					if (info != null
							&& info.versionCode > AppUtils.getVersionCode(this)) {
						Intent installIntent = AppUtils.AppInstall(file);
						installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						this.startActivity(installIntent);
						// SharePreferenceManager.saveBatchSharedPreference(this,
						// PreferenceUtil.SYS_PARAMETER,
						// PreferenceUtil.SEND_UPDATE_TIME,
						// System.currentTimeMillis());
					} else {
						if (NetworkUtil.CURRENT_NETTYPE != NetworkUtil.NETTYPE_UNKOWN) {
							if (!mCheckUpdateToastLock)
								ToastShow.shortShowToast("正在检测新版本!");
							checkUpdate();
							isResetFlag = false;
						} else {
							if (!mCheckUpdateToastLock)
								ToastShow.shortShowToast("当前无网络请检查");
						}
					}
				} catch (NameNotFoundException e) {
					mCheckUpdateToastLock = false;
					e.printStackTrace();
				}

			} else {
				if (NetworkUtil.CURRENT_NETTYPE != NetworkUtil.NETTYPE_UNKOWN) {
					if (!mCheckUpdateToastLock)
						// ToastUtil.showToast(mContext, "正在检测新版本!");
						checkUpdate();
					isResetFlag = false;
				} else {
					// if (!mCheckUpdateToastLock)
					// ToastUtil.showToast(mContext, "当前无网络请检查!");

				}

			}

		} else {
			// Toast.makeText(LoginActivity.this, "sd卡不可用请检查!",0).show();
		}

		if (isResetFlag)
			mCheckUpdateToastLock = false;

	}

	/**
	 * 向服务器发送更新请求
	 */
	private void checkUpdate() {
		RequestParams params = new RequestParams(Url.APP_UPDATE_URL);
		x.http().post(params, new CommonCallback<String>() {

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
			public void onSuccess(String result) {

				LogUtil.i(result);
				Version json = GsonUtils.parseJSON(result, Version.class);
				if ("1".equals(json.getCode())) {
					// com.graduateentrepreneurship.entity.version.Obj obj =
					// json
					// .getObj().get(0);
					// versionList.addAll(obj);

					if (NetworkUtil.CURRENT_NETTYPE != NetworkUtil.NETTYPE_UNKOWN) {
						int newCode = 0;

						int updateLevel = 0;
						if (json.getAndroidAutoUp() != null) {
							if ("yes".equals(json.getAndroidAutoUp())) {

								updateLevel = 1;
							}
						}
						if (!"".equals(json.getAndroidVerFlag())) {
							newCode = Integer.parseInt(json.getAndroidVerFlag());
						}
						try {
							if (newCode > AppUtils
									.getVersionCode(SettingActivity.this)) {
								showCheckUpdateDialog(json, updateLevel);
							} else {
								ToastShow.shortShowToast("已是最新版本");
							}
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (!mCheckUpdateToastLock)
							Toast.makeText(SettingActivity.this, "当前无网络请检查", 0)
									.show();
					}
				}

			}
		});

	}

	/**
	 * 更新对话框
	 */
	public void showCheckUpdateDialog(final Version u, final int type) {
		String buttonStr = null;
		String button2Str = null;

		if (checkUpdateDialog == null) {
			checkUpdateDialog = new AlertDialog.Builder(SettingActivity.this)
					.create();
			checkUpdateDialog.show();
			checkUpdateDialog.getWindow().setContentView(
					R.layout.checkupversion_dialog);
		}
		switch (type) {
		case 1:// 强制更新
			buttonStr = getString(R.string.str_update_close_app);
			button2Str = getString(R.string.str_update_confirm);
			checkUpdateDialog.setCancelable(false);

			break;
		default:// 非强制更新
			buttonStr = getString(R.string.str_update_cancel);
			button2Str = getString(R.string.str_update_immediately);
			checkUpdateDialog.setCanceledOnTouchOutside(false);
			;

			break;
		}
		checkUpdateDialog.setTitle(getString(R.string.str_update_note));
		TextView message = (TextView) checkUpdateDialog.getWindow()
				.findViewById(R.id.tv_description);
		message.setMovementMethod(new ScrollingMovementMethod());// 设置滚动
		TextView confirm = (TextView) checkUpdateDialog.getWindow()
				.findViewById(R.id.tv_update_confirm);
		TextView cancel = (TextView) checkUpdateDialog.getWindow()
				.findViewById(R.id.tv_update_cancel);
		message.setText(u.getAndroidUpContent());

		cancel.setText(buttonStr);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type == 0) {

					checkUpdateDialog.dismiss();
				} else {
					finish();
				}

			}
		});

		confirm.setText(button2Str);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String sdPath = Environment.getExternalStorageDirectory()
						+ AppConfig.apkUpdateDir + "/" + AppConfig.apkFileName
						+ AppConfig.FILE_SUFFIX;
				// String url = Url.baseImageUrl + u.getDOWNLOADURL();//
				// "http://file.liqucn.com/upload/2013/shuru/com.baidu.input_5.5.5.9_liqucn.com.apk";
				String url = Url.BASE_URL + AppConfig.apkFileName
						+ AppConfig.FILE_SUFFIX;
				downloadFile(url, sdPath);

			}
		});
		if (!checkUpdateDialog.isShowing()) {
			checkUpdateDialog.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp1 = getSharedPreferences(
				Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
		islogin = sp1.getBoolean("islogin", false);
	}

	/**
	 * 文件下载
	 */
	private void downloadFile(final String url, final String path) {
		RequestParams requestParams = new RequestParams(url);
		requestParams.setSaveFilePath(path);
		x.http().get(requestParams, new Callback.ProgressCallback<File>() {
			@Override
			public void onWaiting() {
			}

			@Override
			public void onStarted() {
				LogUtil.d("正在连接");
				checkUpdateDialog.dismiss();
				Toast.makeText(getApplicationContext(), "正在下载新版本", 0).show();
			}

			@Override
			public void onLoading(long total, long current,
					boolean isDownloading) {

				LogUtil.d("开始下载");
				LogUtil.d("进度：" + current + "/" + total);
				float num = (float) current / total;
				DecimalFormat df = new DecimalFormat("0");// 格式化小数
				String s = df.format(num * 100);// 返回的是String类型
				NotifycationMr.showDownloadNotification(
						getApplicationContext(), "下载通知", "已经下载了" + s + "%");

			}

			@Override
			public void onSuccess(File result) {

				LogUtil.d("结果：" + result.toString());
				NotifycationMr.cancelNotifyDownload(getApplicationContext());
				// String sdPath = Environment
				// .getExternalStorageDirectory()
				// + AppConfig.apkUpdateDir
				// + "/"
				// + AppConfig.apkFileName + AppConfig.FILE_SUFFIX;
				File file = null;
				try {
					file = new File(path);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				AppUtils.AppInstall(SettingActivity.this, file);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				ex.printStackTrace();
				NotifycationMr.cancelNotifyDownload(getApplicationContext());
			}

			@Override
			public void onCancelled(CancelledException cex) {
			}

			@Override
			public void onFinished() {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_my_info:
			if (islogin) {
				startActivity(new Intent().setClass(SettingActivity.this,
						MyInfoActivity.class));
			} else {
				startActivity(new Intent(SettingActivity.this,
						LoginActivity.class));
				ToastShow.shortShowToast("请登录");
			}
			break;
		case R.id.tv_change_pass:
			if (islogin) {
				if (!IsMobile.isMobileNO(MyCookieStore.user.getShouji())) {
					ToastShow.shortShowToast("快捷登录不能设置密码！");
					return;
				} else {
					startActivity(new Intent(SettingActivity.this,
							ChangePasswordActivity.class));

				}
			} else {
				startActivity(new Intent(SettingActivity.this,
						LoginActivity.class));
				ToastShow.shortShowToast("请登录");
			}
			break;

		default:
			break;
		}
	}
}
