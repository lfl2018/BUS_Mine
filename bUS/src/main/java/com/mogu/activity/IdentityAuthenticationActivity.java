package com.mogu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore2d.el;
import com.google.gson.Gson;
import com.mogu.adapter.LableAdapter;
import com.mogu.app.Constant;
import com.mogu.app.MrLiUrl;
import com.mogu.app.MyCookieStore;
import com.mogu.app.Url;
import com.mogu.entity.lable.Child;
import com.mogu.entity.lable.Iconlist;
import com.mogu.entity.lable.UserIcon;
import com.mogu.entity.login.GetUserJson;
import com.mogu.entity.login.Login;
import com.mogu.utils.FileUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.ImageUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;

public class IdentityAuthenticationActivity extends AppCompatActivity {

	private List<Iconlist> iconlist = new ArrayList<Iconlist>();
	private AlertDialog aDalog;
	private GridView gv_icon;
	private LableAdapter lableAdapter;
	private TextView tvLable;
	private TextView tvLableOld;
	private MyLableListener myLableListener;
	private String iccode;

	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;

	private ImageView[] imageViews;
	private static final int[] imgRes = { R.id.iv_head, R.id.iv_head1,
			R.id.iv_head2, R.id.iv_head3, R.id.iv_head4, R.id.iv_head5 };
	public int position;

	private static final int count = 6;// 图片数量
	private MyOnClickListener myOnClickListener;
	/**
	 * 存图片路径的list
	 */
	private List<String> picList = new ArrayList<String>();
	// MAC
	private String wifiMac;
	// 文件路径
	private String mPath = "";

	private StringBuffer path;
	private AlertOnClickListener alertOnClickListener;
	private static final int PHOTO_ALBUM = 1; // 相册
	private static final int IMAGE_RESCUT = 2;// 裁剪
	private static final int PHOTO_TAKE = 3;// 拍照

	private Bitmap bm = null;// 图片
	private static final String PHOTO_TYPE = "image/*";

	private ProgressDialog dialog;
	private String storeName;

	@ViewInject(R.id.edt_sjname)
	private EditText edtStoreName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identity_authebtucation);
		x.view().inject(this);
		initViews();

		TitleUtils.setTitle(this, R.id.top_bar, "身份认证");
		alertOnClickListener = new AlertOnClickListener();
		getShopLable();
		imageViews = new ImageView[count];
		ImageView imageView;
		myOnClickListener = new MyOnClickListener();
		for (int i = 0; i < count; i++) {
			imageView = (ImageView) findViewById(imgRes[i]);
			imageView.setOnClickListener(myOnClickListener);
			imageViews[i] = imageView;
		}
	}

	private void initViews() {
		tv1 = (TextView) findViewById(R.id.tv_clothing_type);
		tv2 = (TextView) findViewById(R.id.tv_food_type);
		tv3 = (TextView) findViewById(R.id.tv_hotel_type);
		tv4 = (TextView) findViewById(R.id.tv_traffic_type);
		tv5 = (TextView) findViewById(R.id.tv_entertainment_type);
		myLableListener = new MyLableListener();
		tv1.setOnClickListener(myLableListener);
		tv2.setOnClickListener(myLableListener);
		tv3.setOnClickListener(myLableListener);
		tv4.setOnClickListener(myLableListener);
		tv5.setOnClickListener(myLableListener);

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			for (int i = 0; i < count; i++) {
				if (imgRes[i] == id) {
					position = i;
					break;
				}
			}

			if (picList.size() <= position) {
				addImage(v);
			} else {
				deleteImage();
			}

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

	public void deleteImage() {
		AlertDialog dialog = new AlertDialog.Builder(
				IdentityAuthenticationActivity.this).setMessage("是否删除图片？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						picList.remove(position);
						imageViews[picList.size()]
								.setImageResource(R.drawable.ic_add_pic);
						for (int i = 0; i < picList.size(); i++) {
							x.image().bind(imageViews[i], picList.get(i));
						}
					}
				}).setNegativeButton("取消", null).create();
		dialog.show();
	}

	public void addImage(View v) {
		uploadImage(v);
	}

	@Event(R.id.tv_head)
	private void uploadImage(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				IdentityAuthenticationActivity.this);
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
		String currentTimeMillis = System.currentTimeMillis() + "";
		path = new StringBuffer();
		path.append(Environment.getExternalStorageDirectory().getPath())
				.append("/" + currentTimeMillis + ".jpg");

		//
		// currentTimeMillis =
		// currentTimeMillis.substring(currentTimeMillis.length()-6,
		// currentTimeMillis.length());
		// path.append(Environment.getExternalStorageDirectory().getPath())
		// .append(currentTimeMillis+);
		File file = new File(path.toString());
		Uri uri = Uri.fromFile(file);
		Intent itent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		itent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(itent, PHOTO_TAKE);
	}

	public void uploadMethod() {
		storeName = edtStoreName.getText().toString();
		if (TextUtils.isEmpty(storeName)) {
			ToastShow.shortShowToast("请填写商家名称！");
			return;
		}
		if (picList.size() == 0) {
			ToastShow.shortShowToast("请添加认证图片！");
			return;
		}
		WifiManager mWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (mWifi.isWifiEnabled()) {
			WifiInfo wifiInfo = mWifi.getConnectionInfo();
			wifiMac = wifiInfo.getBSSID(); // 获取被连接网络的mac地址
			SessionRequestParams params = new SessionRequestParams(
					MrLiUrl.URL_UP_IDENTITY);
			for (int i = 0; i < picList.size(); i++) {

				params.addBodyParameter("picfiles[" + i + "]",
						new File(picList.get(i)));
				params.addBodyParameter("picfilesFileName[" + i + "]",
						picList.get(i));
			}
			Log.e("baba", iccode);
			params.addBodyParameter("icbh00", iccode);
			params.addBodyParameter("mac000", wifiMac);
			params.addBodyParameter("sjmc00", storeName);
			dialog = new ProgressDialog(IdentityAuthenticationActivity.this);
			dialog.setMessage("正在上传照片...");
			dialog.show();
			x.http().post(params, new CommonCallback<String>() {

				@Override
				public void onCancelled(CancelledException arg0) {

				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {
					Toast.makeText(getApplicationContext(), "上传图片失败！", Toast.LENGTH_SHORT)
							.show();
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
						LogUtil.e(jsonObject.toString() + "ggg" + msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if ("1".equals(statusCode)) {

						Toast.makeText(IdentityAuthenticationActivity.this,
								"保存成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else {

						AlertDialog dialog = new AlertDialog.Builder(
								IdentityAuthenticationActivity.this)
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
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			handleImageWithoutCut(requestCode, data);
			// handleImageWithCut(requestCode, data);
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
	 * 通过uri 获取bitmap
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap getBitmapFromUri(Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					this.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 没有裁剪的图片处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	public void handleImageWithoutCut(int requestCode, Intent data) {
		if (requestCode == PHOTO_TAKE) {
			File file = new File(path.toString());
			// headImageView.setImageURI(Uri.fromFile(file));
			// Bitmap bm = null;
			// Bitmap bm = getBitmapFromUri(Uri.fromFile(file));
			// FileUtils.saveBitmap(bm, "avatar");
			// headImageView.setImageBitmap(bm);
			// mPath = FileUtils.SDPATH + "avatar.JPEG";
			mPath = path.toString();
			x.image().bind(imageViews[picList.size()], mPath);
			picList.add(mPath);
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
			imagePath = getPicByUri(uri);
			LogUtil.e(imagePath);
			mPath = imagePath;
			x.image().bind(imageViews[picList.size()], mPath);
			picList.add(mPath);
		}
	}

	protected String getPicByUri(Uri selectedImage) {
		if (selectedImage == null)
			return "";
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(
						IdentityAuthenticationActivity.this,
						R.string.cant_find_pictures, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return "";
			}
			return picturePath;
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(
						IdentityAuthenticationActivity.this,
						R.string.cant_find_pictures, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return "";

			}
			return file.getAbsolutePath();
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

	// 获取用户标签
	private void getShopLable() {
		SessionRequestParams params = new SessionRequestParams(
				MrLiUrl.URL_GET_LABLE);
		x.http().post(params, new HttpCallBack() {

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void success(String result) throws JSONException {
				Log.e("baba", result);
				UserIcon json = GsonUtils.parseJSON(result, UserIcon.class);
				iconlist.addAll(json.getList());
			}
		});
	}

	class MyLableListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof TextView) {

				tvLable = (TextView) v;
			}
			switch (v.getId()) {
			case R.id.tv_clothing_type:
			    if(iconlist.size()==0||iconlist.size()<3){
                    return;
                }else{
                    showMyDialog(iconlist.get(2).getChildren());
                }

				break;
			case R.id.tv_food_type:
                if(iconlist.size()==0||iconlist.size()<2){
                    return;
                }else{
                    showMyDialog(iconlist.get(1).getChildren());
                }
				break;
			case R.id.tv_hotel_type:
                if(iconlist.size()==0||iconlist.size()<1){
                    return;
                }else{
                    showMyDialog(iconlist.get(0).getChildren());
                }

				break;
			case R.id.tv_traffic_type:
                if(iconlist.size()==0||iconlist.size()<4){
                    return;
                }else{
                    showMyDialog(iconlist.get(3).getChildren());
                }
				break;
			case R.id.tv_entertainment_type:
                if(iconlist.size()==0||iconlist.size()<5){
                    return;
                }else{
                    showMyDialog(iconlist.get(4).getChildren());
                };
				break;

			default:
				break;
			}
		}

	}

	private void showMyDialog(List<Child> list) {
		if (aDalog == null) {
			View view = getLayoutInflater().from(
					IdentityAuthenticationActivity.this).inflate(
					R.layout.dialog_choose_lable, null);
			aDalog = new AlertDialog.Builder(
					IdentityAuthenticationActivity.this).setView(view).create();
			gv_icon = (GridView) view.findViewById(R.id.gv_lable);
			lableAdapter = new LableAdapter(
					IdentityAuthenticationActivity.this, list);
			gv_icon.setAdapter(lableAdapter);
			gv_icon.setOnItemClickListener(new MyLableItemClickListener());
			aDalog.show();
		} else {
			lableAdapter = new LableAdapter(
					IdentityAuthenticationActivity.this, list);
			gv_icon.setAdapter(lableAdapter);
			aDalog.show();
		}
	}

	class MyLableItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView tv = (TextView) view.findViewById(R.id.tv_name);
			TextView tv1 = (TextView) view.findViewById(R.id.tv_code);
			if (tvLableOld != null) {
				tvLableOld.setText("");
				tvLableOld.setHint("请选择类型");
			}
			tvLable.setText(tv.getText().toString());
			tvLableOld = tvLable;
			iccode = tv1.getText().toString();
			aDalog.dismiss();
		}

	}
}
