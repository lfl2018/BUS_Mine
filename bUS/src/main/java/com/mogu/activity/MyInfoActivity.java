package com.mogu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import com.mogu.adapter.MyLableAdapter;
import com.mogu.app.Constant;
import com.mogu.app.Url;
import com.mogu.entity.myinfo.MyInfo;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;
import com.mogu.utils.ValueUtils;
import com.tencent.mm.sdk.platformtools.Log;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity implements OnClickListener {
	private static final int CHANGE_LABEL = 1;
	private TextView mobile;
	private EditText nick;
	private TextView sex;
	private EditText sign;
	private TextView change;
	private String strSex;
	private GridView gvLable;
	private View labelView;
	private MyLableAdapter myLableAdapter;
	private List<String> labelList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		initViews();
		GetData();
	}

	private void initViews() {
		gvLable = (GridView) findViewById(R.id.gv_lable);
		labelView = findViewById(R.id.fl_label);

		mobile = (TextView) findViewById(R.id.tv_mobile);
		nick = (EditText) findViewById(R.id.tv_nick);
		sex = (TextView) findViewById(R.id.tv_sex);
		sign = (EditText) findViewById(R.id.tv_sign);
		change = (TextView) findViewById(R.id.right);
		change.setText("修改");
		change.setOnClickListener(this);
		sex.setOnClickListener(this);
		sex.setClickable(false);
		labelView.setOnClickListener(this);
		labelView.setClickable(false);

		nick.setEnabled(false);
		sign.setEnabled(false);
		gvLable.setEnabled(false);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我的资料");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right:
			if (change.getText().toString().equals("修改")) {
				change.setText("保存");
				sex.setClickable(true);
				nick.setEnabled(true);
				sign.setEnabled(true);
				labelView.setClickable(true);

			} else if (change.getText().toString().equals("保存")) {
				Log.e("爸爸", labelList.size() + "");
				ChangeData();
			}
			break;
		case R.id.pre:
			finish();
			break;
		case R.id.tv_sex:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("选择性别");
			builder.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					sex.setText(items[which]);
					strSex = "00" + (which + 1);
				}

			}).create();
			builder.show();

			break;

		case R.id.fl_label:
			Intent intent = new Intent(this, MyLabelListActivity.class);
			intent.putStringArrayListExtra(Constant.LABEL_VALUE,
					(ArrayList<String>) labelList);
			startActivityForResult(intent, CHANGE_LABEL);
			break;

		default:
			break;
		}
	}

	private String items[] = { "男", "女", "未知" };

	public void simpleList(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择性别");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				sex.setText(items[which]);
				strSex = "00" + (which + 1);
			}

		});

		AlertDialog simplelistdialog = builder.create();
		simplelistdialog.show();
	}

	private void GetData() {
		SessionRequestParams params = new SessionRequestParams(Url.MyInfo_URL);
		x.http().post(params, new CommonCallback<String>() {

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
				MyInfo json = GsonUtils.parseJSON(arg0, MyInfo.class);
				if (json.getCode().equals("1")) {
					mobile.setText(ValueUtils.getValue(json.getUserHyb000()
							.getShouji()));
					nick.setText(json.getUserHyb000().getHyming());
					sign.setText(json.getUserHyb000().getGxqm00());
					strSex = json.getUserHyb000().getXbie00();
					if (json.getUserHyb000().getXbie00().equals("001")) {
						sex.setText("男");
					} else if (json.getUserHyb000().getXbie00().equals("002")) {
						sex.setText("女");
					} else {
						sex.setText("未知");
					}
					labelList.addAll(json.getBqlist());
					myLableAdapter = new MyLableAdapter(MyInfoActivity.this,
							labelList, false);
					gvLable.setAdapter(myLableAdapter);
				}
			}
		});
	}

	private void ChangeData() {
		SessionRequestParams params = new SessionRequestParams(Url.UpMyinfo_URL);
		String lables = ValueUtils.getString(labelList, ",");
		params.addBodyParameter("hyming", nick.getText().toString());
		params.addBodyParameter("xbie00", strSex);
		params.addBodyParameter("gxqm00", sign.getText().toString());
		params.addBodyParameter("bqname", lables);

		x.http().post(params, new CommonCallback<String>() {

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
					JSONObject json = new JSONObject(arg0);
					if (json.getString("code").equals("1")) {
						Toast.makeText(MyInfoActivity.this, "保存成功",
								Toast.LENGTH_SHORT).show();
						change.setText("修改");
						sex.setClickable(false);
						nick.setEnabled(false);
						sign.setEnabled(false);
					} else {
						Toast.makeText(MyInfoActivity.this,
								json.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == CHANGE_LABEL) {
			labelList.clear();
			ArrayList<String> arrayList = data.getStringArrayListExtra(Constant.LABEL_VALUE);
			if (arrayList!=null) {
				labelList.addAll(arrayList);
			}
			myLableAdapter.notifyDataSetChanged();
		}
	}

}
