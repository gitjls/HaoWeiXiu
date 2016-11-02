package com.haoweixiu.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haoweixiu.R;
import com.haoweixiu.dto.UpdateApkInfoDto;
import com.haoweixiu.view.UpdateDialog;
import com.haoweixiu.view.UpdateDialogSimple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

	private Context mContext;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	/* APK更新的详细信息 */
	private UpdateApkInfoDto apkInfo;
	/* 发起版本检测的url */
	private String path = "http://10.0.2.2:8080/";
	/* 下载包安装路径 */
	private static final String savePath = Environment
			.getExternalStorageDirectory() + "/student_for_androidhd/update/";
	/* 本地APK文件路径 */
	private String apkFile;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private Dialog noticeDialog;
	private Dialog downloadDialog;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				if (downloadDialog != null)
					downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 检查更新外部接口让主Activity调用
	 */
	public void checkUpdateInfo() {
		if (isNetworkAvailable(mContext)) {
			checkToUpdate();
		} else {
			return;
		}
	}

	/**
	 * 检查版本是否需要更新
	 * 
	 * @throws NameNotFoundException
	 */
	private void checkToUpdate() {
		// TODO Auto-generated method stub
		getServerVersion();
		int currentCode = getVerCode(mContext);
//		 if (apkInfo.getApkVerCode() > currentCode) {
//		 // 弹出更新提示对话框
//		 showNoticeDialog();
//		 } else {
//		 Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
//		 }
	}

	/**
	 * 获取升级APK详细信息 {apkVersion:'1.10',apkVerCode:2,apkName:'1.1.apk',
	 * apkDownloadUrl:'http://localhost:8080/myapp/1.1.apk'}
	 * 
	 * @return
	 */
	private void getServerVersion() {
		// TODO Auto-generated method stub
		try {
			String newVerJSON = GetUpdateInfo.getUpdataVerJSON(path);
			try {
				JSONArray jsonArray = new JSONArray(newVerJSON);
				if (jsonArray.length() > 0) {
					JSONObject obj = jsonArray.getJSONObject(0);
					String apkVersion = obj.getString("apkVersion");
					int apkVerCode = obj.getInt("apkVerCode");
					String apkName = obj.getString("apkName");
					String apkDownloadUrl = obj.getString("apkDownloadUrl");
					// apkInfo = new UpdateApkInfoDto(apkVersion, apkVerCode,
					// apkName, apkDownloadUrl);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = null;
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("verName:" + verName);
		return verName;
	}

	/**
	 * 发现新版本提示框
	 */
	public void showNoticeDialog() {
		String updateMsg = "有最新的软件包哦，亲快下载吧~";
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("立即下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
		// 设置弹出对话框大小属性
		WindowManager.LayoutParams lp = noticeDialog.getWindow()
				.getAttributes();
		lp.width = 400;
		lp.height = 500;
		noticeDialog.getWindow().setAttributes(lp);
	}

	/**
	 * 下载进度框
	 */
	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("正在下载");
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		downloadApk();
	}

	/**
	 * 下载apk
	 */
	public void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					URL url = new URL(apkInfo.getUrl());

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					apkFile = savePath + "ifdoo.apk";
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWN_UPDATE);
						if (numread <= 0) {
							// 下载完成通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);// 点击取消就停止下载.

					fos.close();
					is.close();
				} else {
					Toast.makeText(mContext, "检测到手机没有存储卡,请安装了内存卡后再升级。",
							Toast.LENGTH_SHORT).show();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(apkFile);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
		try {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
			return (netWorkInfo != null && netWorkInfo.isAvailable());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新框
	 * 
	 * @param dto
	 */
	public void updateDialog(final UpdateApkInfoDto dto) {
		final UpdateDialog my = new UpdateDialog(mContext);
		my.setMessage1(dto.getVersion());
		my.setMessage2(dto.getDate().substring(0, 10));
		my.setMessage3(dto.getCaption());
		// 点击屏幕不消失
		my.setCanceledOnTouchOutside(false);
		my.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startDownload(dto);
			}

		});

		my.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				my.dismiss();
			}

		});
		my.show();
	}

    /**
     * 主页的更新框
     *
     * @param dto
     */
    public void updateDialogInMain(final UpdateApkInfoDto dto) {
        final UpdateDialogSimple my = new UpdateDialogSimple(mContext);

        // 点击屏幕不消失
        my.setCanceledOnTouchOutside(false);
		my.setCancelable(false);
        my.setPositiveButton("", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDownload(dto);
				ToastUtil.showMessage(mContext, "提分王正在后台下载", Toast.LENGTH_SHORT);
            }

        });

        my.setNegativeButton("", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                my.dismiss();
				System.exit(0);
            }

        });
        my.show();
    }



	/**
	 * 开始下载更新包
	 */
	@SuppressWarnings("deprecation")
	private void startDownload(UpdateApkInfoDto dto) {
		// 获取下载服务
		DownloadManager downloadManager = (DownloadManager) mContext
				.getSystemService(Context.DOWNLOAD_SERVICE);
		// 创建下载请求
		Request request = new Request(
				Uri.parse(dto.getUrl()));
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以8/000000
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		// 设置文件类型
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
				.getFileExtensionFromUrl(dto.getUrl()));
		request.setMimeType(mimeString);
		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		// 设置标题
		// request.setTitle("提分王家长版");
		// 在通知栏中显示
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
            String path = "/parent_for_android/update/";
			//File updateDir = new File(Const.savePath);
            File updateDir = Environment.getExternalStoragePublicDirectory(path);


            if (!(updateDir.exists()&&updateDir.isDirectory())) {
				updateDir.mkdirs();

			}
			File apkFile = new File(Const.savePath + Const.apkName);
			if (apkFile.exists()) {
				apkFile.delete();
			}
			request.setDestinationInExternalPublicDir("/parent_for_android/update/",
					Const.apkName);

//            request.setDestinationInExternalPublicDir(Const.savePath,
//                    Const.apkName);
			// 将下载请求放入队列
			downloadManager.enqueue(request);
		} else {
			// 将下载请求放入队列
			downloadManager.enqueue(request);
		}

	}

}
