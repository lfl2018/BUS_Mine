package com.mogu.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

/**
 * app应用工具类
 * 
 */
public class AppUtils {
	private final static String TAG = AppUtils.class.getSimpleName();
	public static Set<Integer> install_list = new HashSet<Integer>();
	private static HashSet<Integer> downloadedResourceList = new HashSet<Integer>();

	public static HashMap<Integer, String> appDelRecordMap = null;

	/**
	 * 
	 * @param packname
	 */
	public static void addInstalledApplication(String packname) {
		install_list.add(packname.hashCode());
	}

	/**
	 * 
	 * @param packname
	 */
	public static void removeInstalledApplication(String packname) {
		install_list.remove(packname.hashCode());
	}

	/** 卸载系统应用 */
	// public static void removeSystemInstallApp(String packname)
	// {
	// system_installList.remove(packname.hashCode());
	// }

	/**
	 * 卸载指定程序
	 */
	public static Intent AppUnInstall(String packName) {
		Uri packURI = Uri.fromParts("package", packName, null);
		Intent packIntent = new Intent(Intent.ACTION_DELETE, packURI);
		return packIntent;
	}

	/**
	 * 打开指定程序
	 * 
	 * @param packName
	 * @param ACName
	 * @return
	 */
	public static Intent OpenInstall(String packName, String ACName) {
		if (packName == null || packName.equals(""))
			return null;
		if (ACName == null || ACName.equals("")) {
			return null;
		}
		Intent packIntent = new Intent(Intent.ACTION_MAIN);
		packIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		packIntent.setClassName(packName, ACName);
		return packIntent;
	}

	/**
	 * 传进来本地文件名 安装软件
	 */
	public static void AppInstall(Context context, File file) {
		if (file.exists() && file.length() > 0) {
			Intent apkintent = new Intent(Intent.ACTION_VIEW);
			final Uri puri = Uri.fromFile(file);
			apkintent.setDataAndType(puri,
					"application/vnd.android.package-archive");
			apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(apkintent);
		} else {
			Toast.makeText(context, "无法安装", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 传进来本地文件名 安装软件
	 */
	public static Intent AppInstall(File file) {
		Intent apkintent = new Intent(Intent.ACTION_VIEW);
		final Uri puri = Uri.fromFile(file);
		apkintent.setDataAndType(puri,
				"application/vnd.android.package-archive");
		return apkintent;
	}

	/**
	 * 获得包里启动Activity的信息
	 * 
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static String getMainActivity(Context context, String packagename) {
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packagename);

		List<ResolveInfo> apps = context.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);

		if (null != apps && apps.size() > 0) {
			ResolveInfo ri = apps.iterator().next();
			if (ri != null)
				return ri.activityInfo.name;
		}
		return "";
	}

	/** 检查系统已经安装的包名 */
	public synchronized static List<PackageInfo> getAppActivityNameByPackageInfo(
			Context context) {
		List<PackageInfo> pkginfolist = null;
		PackageManager pm = context.getApplicationContext().getPackageManager();
		pkginfolist = pm.getInstalledPackages(0);
		return pkginfolist;
	}

	/** 获取当前应用的版本号 */
	public static int getVersionCode(Context context)
			throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(
				context.getPackageName(), 0).versionCode;
	}

	/** 获取已经安装应用的版本号 */
	public static int getVersionCode(Context context, String packagename)
			throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(packagename, 0).versionCode;
	}

	/** 获取已经安装应用的版本 */
	public static String getVersionName(Context context, String packagename)
			throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(packagename, 0).versionName;
	}

	/**
	 * 根据包名获取系统应用icon
	 * 
	 * @throws NameNotFoundException
	 */
	public static Drawable getLocalApplicationIcon(Context context,
			String packagename) throws NameNotFoundException {
		Drawable drawable = null;
		PackageManager pm = context.getPackageManager();
		drawable = pm.getApplicationIcon(packagename);
		return drawable;
	}

	public static List<ResolveInfo> getInstalledList(PackageManager pm) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// intent.addCategory(Intent.CATEGORY_DEFAULT);
		return pm.queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
	}

	/** 获取已下载的APK文件的packInfo信息 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}

	/** 获取APK文件中的应用图标 */
	public static Drawable getApkIcon(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		if (apkInfo != null) {
			ApplicationInfo appInfo = apkInfo.applicationInfo;
			appInfo.sourceDir = archiveFilePath;
			appInfo.publicSourceDir = archiveFilePath;
			return pm.getApplicationIcon(appInfo);
		}
		return null;
	}

	/** 新增文件处理 */
	public static void addResourceFile(String filePath) {
		downloadedResourceList.add(filePath.hashCode());
	}

	/** 删除文件处理 */
	public static void removeResourceFile(String filePath) {
		downloadedResourceList.remove(filePath.hashCode());
	}

	private static float _DENSITY = 0.0F;

	public static String getUninstallAppViersionName(Context context, File file) {
		String versionname = "";
		PackageInfo info = AppUtils.getApkInfo(context, file.getAbsolutePath());
		if (info != null && info.versionName != null) {
			versionname = info.versionName;
		}
		return versionname;
	}

	public static int getUninstallAppViersionCode(Context context, File file) {
		int versionCode = 0;
		PackageInfo info = AppUtils.getApkInfo(context, file.getAbsolutePath());
		if (info != null) {
			versionCode = info.versionCode;
		}
		return versionCode;
	}

	public static String byteToMb(double filesize) {
		double fileMbSize = filesize / 1024 / 1024;
		// DecimalFormat df = new DecimalFormat("#.00");
		DecimalFormat df = new DecimalFormat("#");
		return df.format(fileMbSize);
	}

	public static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * 获取下载目录
	 * 
	 * @throws Exception
	 */
	// public static String getdownloaderdirectory() throws Exception {
	// File saveDir = null;
	// saveDir = new File(Environment.getExternalStorageDirectory(),
	// AppConfig.apkUpdateDir);
	// if (!saveDir.exists()) {
	// saveDir.mkdirs();
	// }
	// return saveDir.getAbsolutePath();
	// }
}
