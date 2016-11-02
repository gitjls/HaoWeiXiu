package com.haoweixiu.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haoweixiu.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

public class NetUtil
{
	/** 天气预报 */
	
	public static String encodeUrl(Bundle parameters)
	{
		if (parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet())
		{
			if (first)
				first = false;
			else
				sb.append("&");
			sb.append(key + "=" + parameters.getString(key));
		}
		return sb.toString();
	}
	
	public static byte[] getByteContent(InputStream is)
	{
		if (is == null)
		{
			return null;
		}
		try
		{
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1)
			{
				bos.write(buffer , 0 , len);
			}
			byte[] content = bos.toByteArray();
			bos.close();
			return content;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void downWebResources(String webUrl, String filePath, String fileName, String method)
	{
		try
		{
			URL url = new URL(webUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (null == method)
			{
				conn.setRequestMethod("GET");// 网络请求方式 ：GET ， POST
			}
			else
			{
				conn.setRequestMethod(method);
			}
			conn.setConnectTimeout(6 * 1000);
			conn.setReadTimeout(6 * 1000);
			conn.setRequestProperty("User-Agent" , System.getProperties().getProperty("http.agent"));
			if (conn.getResponseCode() != 200)
			{ // 200是请求成功
				return;
			}
			// int size = conn.getContentLength();
			InputStream ips = conn.getInputStream();
			if (null == ips)
			{
				return;
			}
			createSDDir(filePath);
			createSDFile(fileName);
			byte buffer[] = new byte[1024];
			int bufferLength = 0;
			FileOutputStream fos = new FileOutputStream(fileName);
			while ((bufferLength = ips.read(buffer)) > 0)
			{
				fos.write(buffer , 0 , bufferLength);
			}
			ips.close();
			fos.close();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void downWebResources(String webUrl, String fileName)
	{
		try
		{
			URL url = new URL(webUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// 网络请求方式 ：GET ， POST
			conn.setConnectTimeout(6 * 1000);
			conn.setReadTimeout(6 * 1000);
			conn.setRequestProperty("User-Agent" , System.getProperties().getProperty("http.agent"));
			if (conn.getResponseCode() != 200)
			{ // 200是请求成功
				return;
			}
			// int size = conn.getContentLength();
			InputStream ips = conn.getInputStream();
			if (null == ips)
			{
				return;
			}
			File file = new File(Environment.getExternalStorageDirectory() , fileName);
			// createSDDir(filePath);
			// createSDFile(fileName);
			byte buffer[] = new byte[1024];
			int bufferLength = 0;
			FileOutputStream fos = new FileOutputStream(file);
			while ((bufferLength = ips.read(buffer)) > 0)
			{
				fos.write(buffer , 0 , bufferLength);
			}
			fos.flush();
			ips.close();
			fos.close();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void createSDDir(String filePath)
	{
		File file = new File(filePath);
		if (!file.exists())
		{
			file.mkdir();
			System.out.println("创建文件夹:" + file);
		}
	}
	
	public static void createSDFile(String fileName) throws IOException
	{
		File file = new File(fileName);
		if (!file.exists())
		{
			file.createNewFile();
			System.out.println("创建文件:" + file);
		}
	}
	
	public static boolean saveToFile(String path, String fileName, byte[] content)
	{
		if (content == null || content.length == 0 || fileName == null)
		{
			return false;
		}
		File pathFile = new File(path);
		if (!pathFile.exists())
		{
			pathFile.mkdirs();
		}
		try
		{
			FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
			fos.write(content);
			fos.close();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static boolean isNet(Context context) {  
	    ConnectivityManager con = (ConnectivityManager) context  
	        .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo networkinfo = con.getActiveNetworkInfo();  
	    if (networkinfo == null || !networkinfo.isAvailable()) {  
	    // 当前网络不可用   
	        Toast.makeText(context.getApplicationContext(), "网络异常,请检查网络！", Toast.LENGTH_SHORT).show();  
	        return false;  
	    }  
	    boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)  
	        .isConnectedOrConnecting();  
	    if (!wifi) { // 提示使用wifi   
	        Toast.makeText(context.getApplicationContext(), "建议您使用WIFI以减少流量！",  
	        Toast.LENGTH_SHORT).show();  
	    }  
	    return true;  
	  
	}  
	
	/***
	 * 网络连接检查
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context)
	{// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try
		{
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null)
			{
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected())
				{
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}
	
	/**
	 * 是否联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworking(Context context)
	{
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean isGprs(Activity activity)
	{
		ConnectivityManager mConnectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting())
		{
			return false;
		}
		
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		
		if (netType == ConnectivityManager.TYPE_MOBILE && (netSubtype == TelephonyManager.NETWORK_TYPE_GPRS || netSubtype == TelephonyManager.NETWORK_TYPE_EDGE))
		{
			return true;
		}
		
		return false;
		
	}
	
	/***
	 * 判断SD卡能否读写
	 * 
	 * @return
	 */
	public static boolean IsCanUseSdCard()
	{
		try
		{
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/***
	 * WIFI 是否开启
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isWIFI(Activity activity)
	{
		try
		{
			Context context = activity.getApplicationContext();// 获取应用上下文
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);// 获取系统的连接服务
			NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况
			if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				return true;
				// 判断WIFI网
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		
	}
	
	/***
	 * 获得手机唯一编码 GSM手机的 IMEI 和 CDMA手机的 MEID.
	 * 
	 * @param activity
	 * @return
	 */
	public static String getIMEI(Activity activity)
	{
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
		
		return tm.getDeviceId();
	}
	
	/***
	 * 获得手机序列号
	 * 
	 * @param activity
	 * @return
	 */
	public static String getSimSerialNumber(Activity activity)
	{
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
		
		return tm.getSimSerialNumber();// String
	}
	
	/******************************************************* 判断选项 ****************************************/
	public static void getbuilder(final TextView text, int i, String[] str, Builder builder, final String xuanze1, final String xuanze2)
	{
		if (text.getText().toString().trim().equals("") && text.getText().toString().trim().equals(xuanze1))
		{
			i = 0;
		}
		else if (text.getText().toString().trim().equals(xuanze2))
		{
			i = 1;
		}
		else
		{
			i = 0;
		}
		// 判断所选的性别，并赋值
		builder.setSingleChoiceItems(str , i , new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case 0:
						text.setText(xuanze1);
						break;
					case 1:
						text.setText(xuanze2);
						break;
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
		
	}
	
	/******************************************************* 照相功能方法 ****************************************/
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
		int initialSize = computeInitialSampleSize(options , minSideLength , maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8)
		{
			roundedSize = 1;
			while (roundedSize < initialSize)
			{
				roundedSize <<= 1;
			}
		}
		else
		{
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	
	public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 200 : (int) Math.min(Math.floor(w / minSideLength) , Math.floor(h / minSideLength));
		if (upperBound < lowerBound)
		{
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1))
		{
			return 1;
		}
		else if (minSideLength == -1)
		{
			
			return lowerBound;
			
		}
		else
		{
			return upperBound;
		}
	}
	
	public static void getImage(Bitmap photo, String uri, ImageView image)
	{
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(uri , opts);
		opts.inSampleSize = NetUtil.computeSampleSize(opts , -1 , 1200 * 1200);
		opts.inJustDecodeBounds = false;
		photo = BitmapFactory.decodeFile(uri , opts);
		image.setImageBitmap(photo);
	}
	
	public static boolean isOnline(Context context)
	{
		boolean isOnline = false;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (wifi.isAvailable())
		{
			isOnline = true;
		}
		else if (mobile.isAvailable())
		{
			isOnline = true;
		}
		if (info != null)
		{
			if (info.isAvailable())
			{
				isOnline = true;
			}
		}
		return isOnline;
		
	}
	
	
	
	private static Map<String , String> minDisMap;
	
	public static Map<String , String> getMinDisMap()
	{
		return minDisMap;
	}
	
	public static ArrayList<Map<String , String>> getSortList(ArrayList<Map<String , String>> list)
	{
		if (list == null)
		{
			return null;
		}
		if (list.size() < 2)
		{
			return list;
		}
		Map<String , String> tempMap;
		for (int i = 1; i < list.size(); i++)
		{
			for (int j = 0; j < list.size() - i; j++)
			{
				if (Double.parseDouble(list.get(j).get("dis")) > Double.parseDouble(list.get(j + 1).get("dis")))
				{
					tempMap = list.get(j + 1);
					list.set(j + 1 , list.get(j));
					list.set(j , tempMap);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据经纬度获取地址字符串
	 * 
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 * @return
	 */
	public static String getAdd(String lat, String lon)
	{
		String add = "";
		if ("".equals(lat))
		{
			return add;
		}
		Double dlat = Double.parseDouble(lat) - 0.00625f;
		Double dlon = Double.parseDouble(lon) - 0.00625f;
		String url = String.format("http://ditu.google.cn/maps/geo?output=csv&key=abc&q=%s,%s" , dlat , dlon);
		URL myURL = null;
		URLConnection httpsConn;
		try
		{
			myURL = new URL(url);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
		try
		{
			httpsConn = myURL.openConnection();
			if (httpsConn != null)
			{
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream() , "UTF-8");
				BufferedReader br = new BufferedReader(insr);
				String data = null;
				if ((data = br.readLine()) != null)
				{
					String[] list = data.split(",");
					if (list.length > 2 && "200".equals(list[0]))
					{
						add = list[2];
						add = add.replace("\"" , "");
						add = add.substring(2);
						int a = add.indexOf(" ");
						if (a > 0)
						{
							add = add.substring(0 , a);
						}
					}
					else
					{
						add = null;
					}
				}
				insr.close();
			}
		}
		catch (IOException e)
		{
			return null;
		}
		return add;
	}
	
	/**** 布局方法 ****/
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
		// 获取ListView对应的Adapter
		int totalHeight = 0;
		ListAdapter listAdapter = listView.getAdapter();
		// listAdapter.notifyDataSetChanged();
		for (int i = 0, len = listAdapter.getCount(); i < len; i++)
		{ // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i , null , listView);
			listItem.measure(0 , 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	
	/***************** 获取当前客户端版本号等信息 ********************************/
	public static int getVerCode(String packPath, Context context)
	{
		int verCode = -1;
		try
		{
			verCode = context.getPackageManager().getPackageInfo(packPath , 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return verCode;
	}
	
	public static String getVerName(String packPath, Context context)
	{
		String verName = "";
		try
		{
			verName = context.getPackageManager().getPackageInfo(packPath , 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return verName;
		
	}
	
	public static String getAppName(String packPath, Context context)
	{
		String verName = context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
	

	
}
