package com.mogu.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;

import com.mogu.app.Url;
import com.mogu.utils.SessionRequestParams;
import com.mogu.utils.ToastShow;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends Activity{
	private TextView btn_ss;
	private EditText nrong;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_feed_back);
	TextView title = (TextView) findViewById(R.id.title);
	title.setText("意见反馈");
	ImageView pre = (ImageView) findViewById(R.id.pre);
	pre.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	});
	nrong = (EditText) findViewById(R.id.edt_nrong);
	btn_ss = (TextView) findViewById(R.id.tv_submit);
	btn_ss.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			Builder builder = new Builder(FeedBackActivity.this).setTitle("提示").setMessage("反馈成功，谢谢").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			builder.show();
			MyFeedBakc();
		}
	});
	
	}
	
	private void MyFeedBakc(){
		String strNrong = nrong.getText().toString();
		if ("".equals(strNrong)) {
		ToastShow.shortShowToast("反馈内容不能为空，谢谢");	
		return;
		}
		SessionRequestParams params = new SessionRequestParams(Url.FeedB_URL);
		params.addBodyParameter("nrong0", strNrong);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				   Toast.makeText(x.app(), arg0.getMessage(), Toast.LENGTH_LONG).show();

			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String arg0) {
				
				try {
					JSONObject json = new JSONObject(arg0);
					Log.e("a", arg0);
					if (json.get("code").equals("1")) {
						
						Toast.makeText(FeedBackActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
						nrong.setText("");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	}
}
