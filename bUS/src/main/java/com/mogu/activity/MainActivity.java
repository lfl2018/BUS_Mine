package com.mogu.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mogu.app.AppConfig;
import com.mogu.app.Constant;
import com.mogu.app.Url;
import com.mogu.entity.update.Version;
import com.mogu.fragment.ContactListFragment;
import com.mogu.fragment.ConversationListFragment;
import com.mogu.fragment.IndexFragment;
import com.mogu.fragment.MeMeFragment;
import com.mogu.fragment.NewsCenterFragement;
import com.mogu.fragment.ShopFragment;
import com.mogu.fragment.TopicFragment;
import com.mogu.utils.AppUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.NetworkUtil;
import com.mogu.utils.NotifycationMr;
import com.mogu.utils.SessionRequestParams;

/**
 * 主页面
 * 
 * @author Administrator
 *
 */
@SuppressLint("InflateParams")
public class MainActivity extends FragmentActivity {
	private long firstTime;
	private FragmentTabHost mTabHost;
	private TextView apolloText;
	private View tabLayout;

	private boolean mCheckUpdateToastLock = false;// 更新提示锁
	private AlertDialog checkUpdateDialog = null;

	// 环信相关
	private boolean isExceptionDialogShow = false;
	private android.app.AlertDialog.Builder exceptionBuilder;
	public boolean isConflict = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		// 初始化TabHost
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		addTab(R.string.appllo_index, R.drawable.ic_menu_index_selector,
				IndexFragment.class);
		addTab(R.string.appllo_shop, R.drawable.ic_menu_topic_selector,
				ShopFragment.class);
		addTab(R.string.appllo_conversation,
				R.drawable.ic_menu_conversation_selector,
				ConversationListFragment.class);
		addTab(R.string.appllo_contact, R.drawable.ic_menu_contact_selector,
				ContactListFragment.class);
		addTab(R.string.appllo_me, R.drawable.ic_menu_me_selector,
				MeMeFragment.class);
		checkUpdateOrInstall();
	}

	@Override
	protected void onResume() {
		super.onResume();

		EMClient.getInstance().chatManager()
				.addMessageListener(messageListener);
	}

	@Override
	protected void onStop() {
		EMClient.getInstance().chatManager()
				.removeMessageListener(messageListener);

		super.onStop();
	}

	EMMessageListener messageListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			refreshUIWithMessage();
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
		}

		@Override
		public void onMessageRead(List<EMMessage> messages) {
		}

		@Override
		public void onMessageDelivered(List<EMMessage> message) {
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change) {
		}
	};

	private void refreshUIWithMessage() {
		runOnUiThread(new Runnable() {
			public void run() {
				// refresh unread count
				if (mTabHost.getCurrentTab() == 1) {
					// refresh conversation list
					ConversationListFragment conversationListFragment = (ConversationListFragment) getSupportFragmentManager()
							.findFragmentByTag(
									""
											+ R.drawable.ic_menu_conversation_selector);
					if (conversationListFragment != null) {
						conversationListFragment.refresh();
					}
				}
			}
		});
	}

	private void addTab(int strRes, int imgRes, Class class1) {
		tabLayout = getLayoutInflater().inflate(R.layout.tab_apollo_layout,
				null);
		apolloText = (TextView) tabLayout.findViewById(R.id.apollo_text);
		apolloText.setText(strRes);
		Drawable top = getResources().getDrawable(imgRes);
		top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
		apolloText.setCompoundDrawables(null, top, null, null);
		mTabHost.addTab(mTabHost.newTabSpec("" + imgRes)
				.setIndicator(tabLayout), class1, null);
	}

	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) {
			Snackbar snackbar = Snackbar.make(apolloText, "再按一次退出",
					Snackbar.LENGTH_SHORT);
			View view = snackbar.getView();
			((TextView) view.findViewById(R.id.snackbar_text))
					.setTextColor(Color.WHITE);
			snackbar.show();
			firstTime = secondTime;
		} else {
			finish();
		}
	}

	/**
	 * 升级版本
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
								// ToastUtil.showToast(mContext, "正在检测新版本!");
								checkUpdate();
							isResetFlag = false;
						} else {
							// if (!checkUpdateToastLock)
							// ToastUtil.show_short(mContext, "当前无网络请检查");
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
									.getVersionCode(MainActivity.this)) {
								showCheckUpdateDialog(json, updateLevel);
							} else {
							}
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (!mCheckUpdateToastLock)
							Toast.makeText(MainActivity.this, "当前无网络请检查", 0)
									.show();
					}
				}

			}
		});

	}

	public void showCheckUpdateDialog(final Version u, final int type) {
		String buttonStr = null;
		String button2Str = null;

		if (checkUpdateDialog == null) {
			checkUpdateDialog = new AlertDialog.Builder(MainActivity.this)
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
				AppUtils.AppInstall(MainActivity.this, file);

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
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		super.onSaveInstanceState(outState);
	}

	private int getExceptionMessageId(String exceptionType) {
		if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
			return R.string.connect_conflict;
		} else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
			return R.string.em_user_remove;
		} else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
			return R.string.user_forbidden;
		}
		return R.string.Network_error;
	}

	/**
	 * show the dialog when user met some exception: such as login on another
	 * device, user removed or user forbidden
	 */
	private void showExceptionDialog(String exceptionType) {
		isExceptionDialogShow = true;
		EMClient.getInstance().logout(true);
		SharedPreferences sp1 = getSharedPreferences(
				Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);

		sp1.edit().clear().commit();

		SessionRequestParams params = new SessionRequestParams(Url.LOGOUT_URL);
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

		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (exceptionBuilder == null)
					exceptionBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				exceptionBuilder.setTitle(st);
				exceptionBuilder
						.setMessage(getExceptionMessageId(exceptionType));
				exceptionBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								exceptionBuilder = null;
								isExceptionDialogShow = false;
								finish();
								Intent intent = new Intent(MainActivity.this,
										LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
				exceptionBuilder.setCancelable(false);
				exceptionBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				LogUtil.e("---------color conflictBuilder error"
						+ e.getMessage());
			}
		}
	}

	private void showExceptionDialogFromIntent(Intent intent) {
		LogUtil.e("showExceptionDialogFromIntent");
		if (!isExceptionDialogShow
				&& intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
			showExceptionDialog(Constant.ACCOUNT_CONFLICT);
		} else if (!isExceptionDialogShow
				&& intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
			showExceptionDialog(Constant.ACCOUNT_REMOVED);
		} else if (!isExceptionDialogShow
				&& intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
			showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		showExceptionDialogFromIntent(intent);
	}

}
