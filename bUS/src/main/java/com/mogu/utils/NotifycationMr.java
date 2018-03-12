package com.mogu.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mogu.activity.MainActivity;
import com.mogu.activity.R;

import java.util.Calendar;

/**
 * 通知栏显示工具类
 * 
 */
public class NotifycationMr {
	private static final int NOTIFI_DOWNLOAD_ID = 0x1001;

	/**
	 * 显示通知
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 */
	public static void showDownloadNotification(Context context, String title,
			String content) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Calendar calendar = Calendar.getInstance();
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context,
				calendar.get(Calendar.HOUR) * 60 * 60 * 1000
						+ calendar.get(Calendar.MINUTE) * 60 * 1000
						+ calendar.get(Calendar.SECOND) * 1000
						+ calendar.get(Calendar.MILLISECOND), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		Notification notif = new Notification();
		notif.icon = R.mipmap.logo;
		notif.tickerText = content;
		notif.when = System.currentTimeMillis();
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		notif.contentIntent = contentIntent;
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notify_view);
		remoteViews.setTextViewText(R.id.content_title, title);
		remoteViews.setTextViewText(R.id.content_content, content);
		notif.contentView = remoteViews;

		nm.notify(NOTIFI_DOWNLOAD_ID, notif);
	}

	/**
	 * 取消通知
	 * 
	 * @param context
	 */
	public static void cancelNotifyDownload(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFI_DOWNLOAD_ID);
	}
}
