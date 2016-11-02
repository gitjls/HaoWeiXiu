package com.haoweixiu.gaodemap.net;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RequestListener {
	public void requestSuccess(JSONArray json);
	public void requestSuccess(JSONObject json);
	public void requestError(VolleyError e);
}
