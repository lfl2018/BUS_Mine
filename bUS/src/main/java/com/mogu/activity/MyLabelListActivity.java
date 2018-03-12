package com.mogu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import com.mogu.adapter.MyLabelListAdapter;
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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyLabelListActivity extends Activity implements OnClickListener {
	private static final int ADD_LABEL = 1;
	private View addView;
	private EditText nick;
	private TextView sex;
	private EditText sign;
	private TextView change;
	private ListView lvLable;
	private MyLabelListAdapter myLabelListAdapter;
	private List<String> labelList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_label_list);
		initViews();
		getData();
	}

	private void initViews() {
		addView = findViewById(R.id.ll_add_label);
		lvLable = (ListView) findViewById(R.id.lv_label);

		addView.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("标签列表");
		ImageView pre = (ImageView) findViewById(R.id.pre);
		pre.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pre:
			// 处理被选中的标签
			finish();
			break;

		case R.id.ll_add_label:
			Intent intent = new Intent(this, MyLabelActivity.class);
			startActivityForResult(intent, ADD_LABEL);
			break;

		default:
			break;
		}
	}

	private void handleLabel() {

	}

	private void getData() {
		ArrayList<String> arrayList = getIntent().getStringArrayListExtra(
				Constant.LABEL_VALUE);
		labelList.addAll(arrayList);
		myLabelListAdapter = new MyLabelListAdapter(this, labelList);
		lvLable.setAdapter(myLabelListAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == ADD_LABEL) {
			String strLabel = data.getStringExtra(Constant.LABEL_VALUE);
			if (!TextUtils.isEmpty(strLabel)) {

				labelList.add(strLabel);
				myLabelListAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void finish() {
		ArrayList<String> list = new ArrayList<String>();
		SparseBooleanArray booleanArray = myLabelListAdapter.getIsSelected();

		for (int i = 0; i < booleanArray.size(); i++) {
			if (booleanArray.get(i)) {
				list.add(labelList.get(i));
			}
		}
		if (list.size() > 5) {
			ToastShow.shortShowToast("最多只能添加5个标签！");
			return;
		}
		
		Intent intent=new Intent();
		intent.putStringArrayListExtra(Constant.LABEL_VALUE, list);
		setResult(RESULT_OK, intent);
		super.finish();
	}

}
