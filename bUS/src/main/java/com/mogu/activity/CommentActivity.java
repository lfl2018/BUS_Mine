package com.mogu.activity;

import org.xutils.x;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.entity.login.Login;
import com.mogu.utils.GsonUtils;
import com.mogu.utils.ToastShow;
/**
 * 评论
 * @author Administrator
 *
 */
public class CommentActivity extends BaseActivity {

	@ViewInject(R.id.et_comment)
	private EditText commentEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_send_comment);
		x.view().inject(this);
	}

	@Event(R.id.tv_cancel)
	private void cancelSend(View view) {
		finish();
	}

	@Event(R.id.tv_send)
	private void sendComment(View view) {
		String commentString = commentEditText.getText().toString().trim();
		if (TextUtils.isEmpty(commentString)) {
			ToastShow.shortShowToast("评论不能为空！");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(Constant.COMMENT_VALUE, commentString);
		setResult(RESULT_OK, intent);
		finish();
	}

}
