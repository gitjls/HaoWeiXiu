package com.haoweixiu.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.haoweixiu.util.Const;

import java.io.File;

public class MainReceiver extends BroadcastReceiver {

	private static final String TAG = MainReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 手机启动完成
			Intent intent1 = new Intent(context, MainReceiver.class);
			intent1.setAction(Const.REBOOT_SERVICE_BROADCAST);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent1, 0);
			long firstime = SystemClock.elapsedRealtime();
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			// 60秒一个周期，不停的发送广播
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
					60 * 1000, sender);
		} else if (intent.getAction().equals(
				ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.e(TAG, "网络状态改变 ");
			boolean success = false;
			// 获得网络连接服务
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取WIFI网络连接状态
			State wifiState = connManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState();
			// 判断是否正在使用WIFI网络
			if (State.CONNECTED == wifiState) {
				System.out.println("-------->wifi连接成功<--------");
				success = true;
			}
			// 获取GPRS网络连接状态getChapter
//			State gprsState = connManager.getNetworkInfo(
//					ConnectivityManager.TYPE_MOBILE).getState();
//			// 判断是否正在使用GPRS网络
//			if (State.CONNECTED == gprsState) {
//				System.out.println("-------->GPRS连接成功<--------");
//				success = true;
//			}
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            int state = tm.getSimState();
            switch (state) {
                case TelephonyManager.SIM_STATE_READY :
                    State gprsState = connManager.getNetworkInfo(
                            ConnectivityManager.TYPE_MOBILE).getState();
                    // 判断是否正在使用GPRS网络
                    if (State.CONNECTED == gprsState) {
                        System.out.println("-------->GPRS连接成功<--------");
                        success = true;
                    }
                    break;

                default:

                    break;
            }
            // 判断无任何网络
			if (!success) {
				System.out.println("-------->网络异常<--------");
				// Toast.makeText(context, "无法连接到服务器，请检查网络", Toast.LENGTH_SHORT)
				// .show();
			}
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)
				|| intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
			File apkFile = new File(Const.savePath + Const.apkName);
			if (apkFile.exists()) {
				apkFile.delete();
				Log.i(TAG, "apkFile was deleted!");
			}
		} else if (intent.getAction().equals(Const.REBOOT_SERVICE_BROADCAST)) {
			// Service的检查与启动
			boolean isServiceRunning = false;
			ActivityManager manager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager
					.getRunningServices(Integer.MAX_VALUE)) {
				if ("com.haoguanjia.service.MainService.service.MainService".equals(service.service
						.getClassName())) {
					isServiceRunning = true;
				}
			}
			if (!isServiceRunning) {
				// 开启服务
				Intent mIntent = new Intent();
				mIntent.setAction(Const.MAIN_SERVICE);//你定义的service的action
				mIntent.setPackage(context.getPackageName());//这里你需要设置你应用的包名
				context.startService(mIntent);
			}
		} else if (intent.getAction().equals(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long reference = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			DownloadManager downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			Query query = new Query();
			query.setFilterById(reference);
			Cursor c = downloadManager.query(query);
			String fileName = null;
			if (c.moveToFirst()) {
				int fileNameIdx = c
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
				fileName = c.getString(fileNameIdx);
				Log.d(TAG, "fileName: " + fileName);
			}
			c.close();
			// 下载完成通知安装
			if (fileName != null) {
				File apkFile = new File(fileName);
				if (apkFile.exists()) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
							"application/vnd.android.package-archive");
					context.startActivity(i);
				} else {
					return;
				}
			} else {
				System.out.println("文件下载失败");
			}

		}

	}

}
