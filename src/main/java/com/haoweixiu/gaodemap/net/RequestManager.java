package com.haoweixiu.gaodemap.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装网络请求操作
 */
public class RequestManager {
	static Context context;
	private static RequestQueue mRequestQueue;

	public RequestManager(Context context) {
		super();
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static void post(String url, Object obj, RequestListener listener) {
		postObj(url, obj, null, listener);
	}
	public static void postObj(String url, Object obj, String jsonRequest,
							   RequestListener listener){
		JsonObjectReuqest request = new JsonObjectReuqest(Method.POST, url,
				jsonRequest, responseJsonObjListener(listener),
				responseError(listener)) {
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		request.setRetryPolicy(new RetryPolicy() {

			@Override
			public void retry(VolleyError arg0) throws VolleyError {
				return;
			}

			@Override
			public int getCurrentTimeout() {
				return 0;
			}

			@Override
			public int getCurrentRetryCount() {
				return 0;
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		addRequest(request, obj);
	}
	public static void postArray(String url, Object obj, final String jsonRequest,
			RequestListener listener) {
		JsonArrayReuqest request = new JsonArrayReuqest(Method.POST, url,
				jsonRequest, responseJsonArrayListener(listener),
				responseError(listener)) {
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
		};
		request.setRetryPolicy(new RetryPolicy() {

			@Override
			public void retry(VolleyError arg0) throws VolleyError {
				return;
			}

			@Override
			public int getCurrentTimeout() {
				return 0;
			}

			@Override
			public int getCurrentRetryCount() {
				return 0;
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		addRequest(request, obj);
	}

	public static void addRequest(Request<?> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		mRequestQueue.add(request);
	}

	public static void cancelAll(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	protected static Response.Listener<JSONArray> responseJsonArrayListener(
			final RequestListener l) {
		return new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				l.requestSuccess(response);
			}
		};
	}
	protected static Response.Listener<JSONObject> responseJsonObjListener(
			final RequestListener l) {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				l.requestSuccess(response);
			}
		};
	}
	/**
	 * 返回错误监听
	 * 
	 * @param l
	 * @return
	 */
	protected static Response.ErrorListener responseError(
			final RequestListener l) {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError e) {
				l.requestError(e);
			}
		};
	}
}
