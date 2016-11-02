package com.haoweixiu.gaodemap.net;

import android.annotation.SuppressLint;
import android.content.Context;

import com.haoweixiu.gaodemap.util.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

@SuppressLint("DefaultLocale")
public class RequestApiImp implements RequestApi {
	private static RequestApiImp instance;
	private String strUrl;
	private static Context context;

	public RequestApiImp(Context context) {
		super();
		RequestApiImp.context = context;
	}

	public RequestApiImp() {
		super();
	}

	public static RequestApiImp getInstance() {
		if (null == instance) {
			instance = new RequestApiImp();
		}
		return instance;
	}

	/**
	 * 拼接网址
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String urlJoint(String url, HashMap<String, String> params) {
		StringBuffer strBuffer = new StringBuffer(url);
		Set<Entry<String, String>> entrySet = params.entrySet();
		int i = 0;
		for (Entry<String, String> entry : entrySet) {
			if (i == 0 && !url.contains("?")) {
				strBuffer.append("?");
			} else {
				strBuffer.append("&");
			}
			strBuffer.append(entry.getKey());
			strBuffer.append("=");
			strBuffer.append(entry.getValue());
			i++;
		}
		return strBuffer.toString();
	}

	/**
	 * 请求网络的基址
	 *
	 * @return
	 */
	public static HashMap<String, Object> getBaseRequestParams() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return map;
	}

	/** 请求字段为一个的 */
	@SuppressLint("DefaultLocale")
	private String getRequestUrl(int urlResId, String value1,
			HashMap<String, Object> map) {
		map = getBaseRequestParams();
		switch (urlResId) {
		case 0:
			map.put("keys", value1);
			break;
		}
		JSONObject jsonUrl = new JSONObject(map);
		return jsonUrl.toString();
	}

	/** 请求字段为两个的 */
	private String getRequestUrl(int urlResId,String value1,
			String value2, HashMap<String, Object> map) {
		map = getBaseRequestParams();
		switch (urlResId) {
		case 0:
			map.put("keys", value1);
			//map.put("did", value2);
			break;
		}
		JSONObject jsonUrl = new JSONObject(map);
		return jsonUrl.toString();
	}


	@Override
	public int map_order(String keys,Object obj, RequestListener listener) {
		strUrl = Constants.MAP_ORDER;
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		String jsonObj = getRequestUrl(0, keys, requestParams);
		RequestManager.postArray(strUrl, obj, jsonObj, listener);
		return Constants.FUNC_SUCCESS;
	}

	@Override
	public int no_receive_order(String keys, String did, Object obj, RequestListener listener) {
		strUrl = Constants.NO_RECEIVE_ORDER_DETAIL;
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		String jsonObj = getRequestUrl(0, keys,did, requestParams);
		RequestManager.postObj(strUrl, obj, jsonObj, listener);
		return Constants.FUNC_SUCCESS;
	}

	@Override
	public int received_order(String keys, String did, Object obj, RequestListener listener) {
		strUrl = Constants.RECEIVED_ORDER_DETAIL;
		HashMap<String, Object> requestParams = new HashMap<String, Object>();
		String jsonObj = getRequestUrl(0, keys,did, requestParams);
		RequestManager.postObj(strUrl, obj, jsonObj, listener);
		return Constants.FUNC_SUCCESS;
	}
}
