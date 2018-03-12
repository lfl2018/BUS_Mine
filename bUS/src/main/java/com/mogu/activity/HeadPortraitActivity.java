package com.mogu.activity;

import java.io.BufferedOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.login.GetUserJson;
import com.mogu.entity.login.Login;
import com.mogu.utils.FileUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;

public class HeadPortraitActivity extends AppCompatActivity {

	@ViewInject(value = R.id.iv_head)
	private ImageView headImageView;

	@ViewInject(R.id.title)
	private TextView mTitle;

	// 文件路径
	private String mPath = "";

	@Event(R.id.tv_cancel)
	private void back(View v) {
		finish();
	}

	private StringBuffer path;
	private AlertOnClickListener alertOnClickListener;
	private static final int PHOTO_ALBUM = 1; // 相册
	private static final int IMAGE_RESCUT = 2;// 裁剪
	private static final int PHOTO_TAKE = 3;// 拍照

	private Bitmap bm = null;// 图片
	private static final String PHOTO_TYPE = "image/*";

	private Login user;

	private ImageOptions options;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head_portrait);
		x.view().inject(this);
		TitleUtils.setTitle(this, R.id.top_bar, "更换头像");
		alertOnClickListener = new AlertOnClickListener();

		user = MyCookieStore.user;
		if (user.getTxiang() == null || user.getTxiang().equals("null")
				|| user.getTxiang().equals("")) {
			headImageView.setImageResource(R.drawable.head_off);
		} else {
			options = new ImageOptions.Builder().setCircular(true)
			// 是否忽略GIF格式的图片
					.setIgnoreGif(false)
					// 图片缩放模式
					.setImageScaleType(ScaleType.CENTER_CROP)
					// 下载中显示的图片
					.setLoadingDrawableId(R.drawable.ic_head)
					// 下载失败显示的图片
					.setFailureDrawableId(R.drawable.ic_head)
					// 得到ImageOptions对象
					.build();
			x.image().bind(headImageView,
					ImageUtils.getImageUrl(user.getTxiang()), options);
		}

	}

	@Event(R.id.tv_save)
	private void toSubmitVerificationCode(View view) {
		if ("".equals(mPath)) {
			Snackbar.make(view, "请添加图片!", 0).show();
		} else {
			uploadMethod();
		}
	}

	@Event(R.id.tv_head)
	private void uploadImage(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				HeadPortraitActivity.this);
		builder.setTitle("请选择");
		builder.setNegativeButton("取消", alertOnClickListener);
		builder.setNeutralButton("相册", alertOnClickListener);
		builder.setPositiveButton("拍照", alertOnClickListener);
		builder.create();
		builder.show();
	}

	private class AlertOnClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

			switch (which) {
			// 取消
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			// 相册
			case DialogInterface.BUTTON_NEUTRAL:
				// 进入到相册 只取相册中所有的图片文件
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						PHOTO_TYPE);

				startActivityForResult(intent1, PHOTO_ALBUM);
				break;
			// 拍照
			case DialogInterface.BUTTON_POSITIVE:
				takepic();
				break;
			}
		}

	}

	public void takepic() {
		path = new StringBuffer();

		path.append(Environment.getExternalStorageDirectory().getPath())
				.append("/123.jpg");
		File file = new File(path.toString());
		Uri uri = Uri.fromFile(file);
		Intent itent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		itent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(itent, PHOTO_TAKE);
	}

	public void uploadMethod() {
		SessionRequestParams params = new SessionRequestParams(
				Url.UPDATE_AVATAR);
		params.addBodyParameter("picfiles", new File(mPath));
		params.addBodyParameter("picfilesFileName", mPath);
		dialog = new ProgressDialog(HeadPortraitActivity.this);
		dialog.setMessage("正在上传照片...");
		dialog.show();
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Toast.makeText(getApplicationContext(), "上传图片失败！", 0).show();
			}

			@Override
			public void onFinished() {
				dialog.dismiss();

			}

			@Override
			public void onSuccess(String result) {
				String statusCode = null;
				String msg = null;

				JSONObject jsonObject = null;
				try {
					LogUtil.i("result:" + result);
					jsonObject = new JSONObject(result);
					statusCode = jsonObject.getString("code");
					msg = jsonObject.getString("msg");
					LogUtil.i(jsonObject.toString() + "ggg" + statusCode);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if ("1".equals(statusCode)) {

					// 获取会员信息
					getUserInfo();

					Toast.makeText(HeadPortraitActivity.this, "保存成功！", 0)
							.show();
					// finish();
				} else {

					AlertDialog dialog = new AlertDialog.Builder(
							HeadPortraitActivity.this)
							.setMessage(msg)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();

				}
			}
		});

	}

	protected void getUserInfo() {
		SessionRequestParams params = new SessionRequestParams(
				Url.GET_USER_INFO);
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
				GetUserJson json = GsonUtils.parseJSON(arg0, GetUserJson.class);
				MyCookieStore.user = json.getUserHyb000();

				SharedPreferences sp1 = getSharedPreferences(
						Constant.MAIN_SHARE_FILE_NAME, Context.MODE_PRIVATE);
				Editor edit1 = sp1.edit();

				String jsonString = new Gson().toJson(MyCookieStore.user);
				LogUtil.i("json:" + jsonString);
				edit1.putBoolean("islogin", true);
				edit1.putString(Constant.SESSION, jsonString);
				edit1.commit();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			// handleImageWithoutCut(requestCode, data);
			handleImageWithCut(requestCode, data);
		}
	}

	/**
	 * 带裁剪的图片处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	public void handleImageWithCut(int requestCode, Intent data) {
		switch (requestCode) {
		case PHOTO_TAKE:
			File file = new File(path.toString());
			startPhotoZoom(Uri.fromFile(file));
			break;
		case PHOTO_ALBUM:
			// 取到选择的相片
			Uri uri = data.getData();
			// 截图处理
			startPhotoZoom(uri);
			break;
		case IMAGE_RESCUT:
			Bundle bundle = data.getExtras();
			bm = bundle.getParcelable("data");

			String currentTimeMillis = System.currentTimeMillis() + "";
			FileUtils.saveBitmap(bm, currentTimeMillis);
			mPath = FileUtils.SDPATH + currentTimeMillis + ".JPEG";
			x.image().bind(headImageView, mPath, options);
			LogUtil.i(mPath);
			break;
		}

	}

	/**
	 * 将获得的图片进行裁剪
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, PHOTO_TYPE);
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 300);// 返回数据的时候的 X 像素大小。
		intent.putExtra("outputY", 300);

		intent.putExtra("return-data", true);
		// 截图完成后 进行跳转
		startActivityForResult(intent, IMAGE_RESCUT);
	}

	/**
	 * 结束时 释放图片资源
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bm != null)
			// 释放图片资源
			bm.recycle();
		bm = null;
	}

	/**
	 * 没有裁剪的图片处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	public void handleImageWithoutCut(int requestCode, Intent data) {
		if (requestCode == PHOTO_TAKE) {
			Bitmap bm = (Bitmap) data.getExtras().get("data");
			FileUtils.saveBitmap(bm, "avatar");
			// headImageView.setImageBitmap(bm);
			mPath = FileUtils.SDPATH + "avatar.JPEG";
			x.image().bind(headImageView, mPath, options);
			LogUtil.i(mPath);
		} else if (requestCode == PHOTO_ALBUM) {

			String imagePath = null;
			Uri uri = data.getData();

			// if (DocumentsContract.isDocumentUri(this, uri)) {
			// // 如果是document类型的uri 则通过id进行解析处理
			// String docId = DocumentsContract.getDocumentId(uri);
			// if ("com.android.providers.media.documents".equals(uri
			// .getAuthority())) {
			// // 解析出数字格式id
			// String id = docId.split(":")[1];
			// String selection = MediaStore.Images.Media._ID + "="
			// + id;
			// imagePath = getImagePath(
			// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			// selection);
			// } else if ("com.android.providers.downloads.documents"
			// .equals(uri.getAuthority())) {
			// Uri contentUri = ContentUris
			// .withAppendedId(
			// Uri.parse(""
			// + "content://downloads/public_downloads"),
			// Long.valueOf(docId));
			// imagePath = getImagePath(contentUri, null);
			// }
			// } else if ("content".equals(uri.getScheme())) {
			// // 如果不是document类型的uri，则使用普通的方式处理
			// imagePath = getImagePath(uri, null);
			// }

			// Uri selectedImage = data.getData();
			// String[] filePathColumns = { MediaStore.Images.Media.DATA };
			// Cursor c = getContentResolver().query(selectedImage,
			// filePathColumns, null, null, null);
			// c.moveToFirst();
			// int columnIndex = c.getColumnIndex(filePathColumns[0]);
			// String picturePath = c.getString(columnIndex);
			// c.close();
			imagePath = uri.getPath();
			LogUtil.i(imagePath);
			Bitmap bm = BitmapFactory.decodeFile(imagePath.toString());
			headImageView.setImageBitmap(bm);
			x.image().bind(headImageView, imagePath);
			mPath = imagePath;

		}
	}

	/**
	 * 通过 uri seletion选择来获取图片的真实uri
	 * 
	 * @param uri
	 * @param seletion
	 * @return
	 */
	private String getImagePath(Uri uri, String seletion) {
		String path = null;
		String[] filePathColumns = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, filePathColumns,
				seletion, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

}
