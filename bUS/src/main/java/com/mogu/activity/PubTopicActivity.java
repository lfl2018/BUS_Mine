package com.mogu.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.HttpMethod;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogu.app.Url;
import com.mogu.utils.FileUtils;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.HttpCallBack;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.TitleUtils;
import com.mogu.utils.ToastShow;

public class PubTopicActivity extends BaseActivity {

	private TextView rightTextView;

	@ViewInject(R.id.et_content)
	private EditText contentEditText;
	@ViewInject(R.id.et_title)
	private EditText titleEditText;

	@ViewInject(R.id.tv_time)
	private TextView timeTextView;

	private String contentString;
	private String titleString;

	private StringBuffer path;
	private static final int PHOTO_ZOOM = 1; // 相册
	private static final String PHOTO_TYPE = "image/*";
	private static final int PHOTO_GRAPH = 3;// 拍照
	// 文件路径
	private String mPath = "";
	
	// 图片名字
	private String picName = "share";

	@ViewInject(R.id.iv_img)
	private ImageView imageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_topic);
		x.view().inject(PubTopicActivity.this);
		TitleUtils.setTitle(PubTopicActivity.this, R.id.topbar, "我要分享");
		View topbarView = findViewById(R.id.topbar);
		rightTextView = (TextView) topbarView.findViewById(R.id.right);
		rightTextView.setText("发布");
		rightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				publishShare();
			}
		});
	}
	
	
	public void publishShare() {
		titleString = titleEditText.getText().toString().trim();
		contentString = contentEditText.getText().toString().trim();
		if ("".equals(titleString)) {
			ToastShow.shortShowToast("标题不能为空！");
			return;
		}
		if ("".equals(contentString)) {
			ToastShow.shortShowToast("内容不能为空！");
			return;
		}
		SessionRequestParams params = new SessionRequestParams(Url.PUB_TOPIC);
		params.addBodyParameter("xxlxin", "001");
		params.addBodyParameter("biaoti", titleString);
		params.addBodyParameter("nrong0", contentString);
//		if (!"".equals(mPath)) {
//			params.addBodyParameter("picfiles", new File(mPath));
//			params.addBodyParameter("picfilesFileName", mPath);
//		}
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
				JSONObject json = new JSONObject(result);
				String code = json.getString("code");
				String msg = json.getString("msg");
				if ("1".equals(code)) {
					ToastShow.shortShowToast("发布分享成功！");
					finish();

				} else {
					ToastShow.shortShowToast(msg);
				}
			}
		});
		
	}

	@Event(R.id.iv_add)
	private void addImage(View view) {
		final AlertDialog builder = new AlertDialog.Builder(PubTopicActivity.this,
				R.style.Dialog_Fullscreen).create();
		builder.show();
		builder.getWindow().setContentView(
				R.layout.dialog_change_group_fragment_img);// 设置弹出框加载的布局
		Button backButton = (Button) builder.findViewById(R.id.btn_back);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		Button upButton = (Button) builder.findViewById(R.id.btn_up);
		upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 进入到相册 只取相册中所有的图片文件
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						PHOTO_TYPE);

				startActivityForResult(intent1, PHOTO_ZOOM);
				builder.dismiss();
			}
		});
		Button takePicButton = (Button) builder.findViewById(R.id.btn_take_pic);
		takePicButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePic();
				builder.dismiss();
			}
		});
	}

	public void takePic() {
		path = new StringBuffer();

		path.append(Environment.getExternalStorageDirectory().getPath())
				.append("/123.jpg");
		File file = new File(path.toString());
		Uri uri = Uri.fromFile(file);
		Intent itent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// itent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(itent, PHOTO_GRAPH);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			imageView.setVisibility(View.VISIBLE);
			if (requestCode == PHOTO_GRAPH) {
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, picName);
				imageView.setImageBitmap(bm);
				mPath = FileUtils.SDPATH + picName + ".JPEG";
			} else if (requestCode == PHOTO_ZOOM) {
				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = PubTopicActivity.this.getContentResolver().query(
						selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				x.image().bind(imageView, picturePath);
//				BitmapUtil bitmapUtils = new BitmapUtils(getActivity());
//				bitmapUtils.display(imageView, picturePath,
//						new BitmapLoadCallBack<View>() {
//
//							@Override
//							public void onLoadCompleted(View arg0, String arg1,
//									Bitmap arg2, BitmapDisplayConfig arg3,
//									BitmapLoadFrom arg4) {
//								((ImageView) arg0).setImageBitmap(arg2);
//								FileUtils.saveBitmap(arg2, picName);
//								mPath = FileUtils.SDPATH + picName + ".JPEG";
//							}
//
//							@Override
//							public void onLoadFailed(View arg0, String arg1,
//									Drawable arg2) {
//
//							}
//						});
			}
		}
	}

}
