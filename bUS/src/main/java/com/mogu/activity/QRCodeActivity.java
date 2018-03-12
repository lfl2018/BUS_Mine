package com.mogu.activity;

import java.util.Hashtable;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mogu.app.Constant;
import com.mogu.app.MyCookieStore;
import com.mogu.entity.login.Login;
import com.mogu.utils.TitleUtils;

public class QRCodeActivity extends AppCompatActivity {

	@ViewInject(R.id.iv_qr)
	ImageView qrimg;

	protected ProgressDialog dialog;
	private Login user;
	private String attentionUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_attention);
		x.view().inject(this);
		TitleUtils.setTitle(this, R.id.top_bar, "我的二维码");
		user = MyCookieStore.user;

		setImage();
		// qrimg.setImageBitmap(createQRImage("http://www.baidu.com",200,200));
	}

	private void setImage() {

		dialog = new ProgressDialog(QRCodeActivity.this);
		dialog.setMessage("正在生成二维码...");
		dialog.setCancelable(false);
		dialog.show();

		attentionUrl = user.getHybh00() + Constant.SEPARATOR + user.getTxiang()
				+ Constant.SEPARATOR + user.getHyming();
		handleBack(attentionUrl);
	}

	public Bitmap createQRImage(String url, final int width, final int height) {
		try {
			// 判断URL合法性
			// if (url == null || "".equals(url) || url.length() < 1) {
			// return null;
			// }
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void handleBack(String attentionUrl) {

		if (attentionUrl != null) {

			qrimg.setImageBitmap(createQRImage(attentionUrl, 200, 200));

			dialog.dismiss();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Snackbar.make(qrimg, "您当前网络不稳定，请稍后重试！", 0).show();
				dialog.dismiss();
				break;
			case 1:
				handleBack(attentionUrl);
				break;
			default:
				break;
			}
		};
	};
}