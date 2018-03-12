package com.mogu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mogu.activity.R;

public class WifiPswDialog extends Dialog{
	private Button cancelButton;
	private Button okButton;
	private EditText pswEdit;
	private OnCustomDialogListener customDialogListener;
	public WifiPswDialog(Context context,OnCustomDialogListener customListener) {
			//OnCancelListener cancelListener) {
		super(context);
		// TODO Auto-generated constructor stub
		customDialogListener = customListener;
		
	}
	//定义dialog的回调事件
	public interface OnCustomDialogListener{
		void back(String str);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_config_dialog);
		setTitle("请输入密码");
		pswEdit = (EditText)findViewById(R.id.wifiDialogPsw);
		cancelButton = (Button)findViewById(R.id.wifiDialogCancel);
		okButton = (Button)findViewById(R.id.wifiDialogCertain);
		cancelButton.setOnClickListener(buttonDialogListener);
		okButton.setOnClickListener(buttonDialogListener);
		
	}
	
	private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if(view.getId() == R.id.wifiDialogCancel){
				pswEdit = null;
				customDialogListener.back(null);
				cancel();//自动调用dismiss();
			}
			else{
				customDialogListener.back(pswEdit.getText().toString());
				dismiss();
			}
		}
	};
	
}
