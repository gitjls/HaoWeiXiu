package com.haoweixiu.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HttpQuery {
//	public static final String SDS = "api.huixueyuan.cn/ifdood_dev01/v2/jiazhang";
//	public static final String SDS2 = "http://api.huixueyuan.cn/ifdood_dev01/v2/jiazhangduan";
//	public static final String SDS3 = "http://api.huixueyuan.cn/ifdood/messages";
//    public static final String SDS4 = "http://api.huixueyuan.cn/ifdood_dev01/xitongshezhi";


	private static final int REQUEST_TIMEOUT = 10 * 1000;
	private static final int SO_TIMEOUT = 10 * 1000;

	public static String httpGetQuery(String url, String path, Map map) {
		BufferedReader in = null;
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			httpclient.getParams().setParameter("utf8", "utf-8");

			List qparams = new ArrayList();
			if (map == null) {
				map = new HashMap();
			}
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				qparams.add(new BasicNameValuePair(name, (String) map.get(name)));
			}
			// if(path!=null){
			URI uri = URIUtils.createURI("http", url, -1, "/" + path,
					URLEncodedUtils.format(qparams, HTTP.UTF_8), null);
			// }else{
			// URI uri = URIUtils.createURI("http" , url , -1 , null ,
			// URLEncodedUtils.format(qparams , HTTP.UTF_8) , null);
			// }

			HttpGet httpget = new HttpGet(uri);
			// httpget.setParams(httpParams);
			HttpResponse response = httpclient.execute(httpget);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			in.close();
			String page = sb.toString();
			System.out.println("info:" + page);
			if (page.startsWith("\ufeff")) {
				return page.substring(1);
			} else {
				return page;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String httpPostQuest111(String url, String path, Map map) {
		BufferedReader in = null;
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			httpclient.getParams().setParameter("utf8", "utf-8");
			List qparams = new ArrayList();
			if (map == null) {
				map = new HashMap();
			}
			Iterator iter = map.keySet().iterator();

			while (iter.hasNext()) {
				String name = (String) iter.next();
				qparams.add(new BasicNameValuePair(name, (String) map.get(name)));
			}
			URI uri = URIUtils.createURI("http", url, -1, "/" + path,
                    URLEncodedUtils.format(qparams, HTTP.UTF_8), null);
			System.out.println("uri:" + uri);
			HttpPost httpget = new HttpPost(uri);
			HttpResponse response = httpclient.execute(httpget);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			String page = sb.toString();
			if (page.startsWith("\ufeff")) {
				return page.substring(1);
			} else {
				return page;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/** 老类型接口获取数据 */
	public static String httpPostQuest(String url, String path, Map map) {

		BufferedReader in = null;
        String uriAPI = url + "/" + path;
        Log.i("httpPostQuest","url = "+uriAPI+" ; map= "+map.toString());

		/* 建立HTTP Post连线 */
		HttpPost httpRequest = new HttpPost(uriAPI);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			JSONObject params2 = new JSONObject();
			if (map.isEmpty()) {
				map = new HashMap();
			}
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String name =  iter.next().toString();
                if(name.equals("ids")&&!map.get(name).equals(""))
                {
                    JSONObject value = (JSONObject)map.get(name);
                    params2.put(name, value);
                }
                else
                {
                    String value = map.get(name).toString();

                    params2.put(name, value);
                }

			}
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            params2.put("mk",MD5Util.md5(Const.MD5String));
			params.add(new BasicNameValuePair("json", params2.toString()));
			System.out.println(uriAPI + params);
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 设置超时时间生效
            httpRequest.setParams(httpParams);

			// 取得HTTP response
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				in = new BufferedReader(new InputStreamReader(httpResponse
						.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				in.close();
				String page = sb.toString();
				System.out.println("info:" + page);
				return page;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
    /** 新英语语言知识取题类型接口获取数据 */
    public static String httpPostQuest89(String url, String path, Map map) {

        BufferedReader in = null;
        String uriAPI = url + "/" + path+"?rid="+(new Random().nextInt(65534)+1);

		/* 建立HTTP Post连线 */
        HttpPost httpRequest = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject params2 = new JSONObject();
            if (map.isEmpty()) {
                map = new HashMap();
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String name =  iter.next().toString();
                if(name.equals("ids"))
                {
                    JSONObject value = (JSONObject)map.get(name);

                    params2.put(name, value);
                }
                else
                {
                    String value = map.get(name).toString();

                    params2.put(name, value);
                }

            }
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            params2.put("mk",MD5Util.md5(Const.MD5String));
            params.add(new BasicNameValuePair("json", params2.toString()));
            System.out.println(uriAPI + params);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            // 发出HTTP request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 设置超时时间生效
            httpRequest.setParams(httpParams);

            // 取得HTTP response
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            // 若状态码为200 ok
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                in = new BufferedReader(new InputStreamReader(httpResponse
                        .getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                String page = sb.toString();
                System.out.println("info:" + page);
                return page;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /** 新英语类型接口获取数据 */
    public static String httpPostQuest2(String url, String path, Map map) {

        BufferedReader in = null;
        String uriAPI = url + "/" + path;

		/* 建立HTTP Post连线 */
        HttpPost httpRequest = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject params2 = new JSONObject();
            if (map.isEmpty()) {
                map = new HashMap();
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String name =  iter.next().toString();
                if(name.equals("ids"))
                {
                    JSONObject value = (JSONObject)map.get(name);

                    params2.put(name, value);
                }
                else
                {
                    params2.put(name, map.get(name));

                }
            }
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            params2.put("mk",MD5Util.md5(Const.MD5String));
            params.add(new BasicNameValuePair("json", params2.toString()));
            System.out.println("wsljun" + uriAPI + params);
            Log.i("yiguan_yuyanyingyong", "httpPostQuest2 uriAPI=" + uriAPI + ",params =" + params);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            // 发出HTTP request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 设置超时时间生效
            httpRequest.setParams(httpParams);

            // 取得HTTP response
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            // 若状态码为200 ok
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                in = new BufferedReader(new InputStreamReader(httpResponse
                        .getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                String page = sb.toString();
                System.out.println("info:" + page);
                return page;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /** 新类型接口获取数据 传递的时list数组*/
    public static String httpPostListQuest(String url, String path, Map map) {

        BufferedReader in = null;
        String uriAPI = url + "/" + path;

		/* 建立HTTP Post连线 */
        HttpPost httpRequest = new HttpPost(uriAPI);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject params2 = new JSONObject();
            if (map.isEmpty()) {
                map = new HashMap();
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                if(name.equals("list")||name.equals("results")||name.equals("resultExerciseDetail"))
                {
                    JSONArray jsonArray = (JSONArray) map.get(name);
                    params2.put(name, jsonArray);
                }
                else
                {
                    String value = (String) map.get(name);
                    params2.put(name, value);
                }

            }
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            params2.put("mk", MD5Util.md5(Const.MD5String));
            params.add(new BasicNameValuePair("json", params2.toString()));
            System.out.println(uriAPI + params);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            // 发出HTTP request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 设置超时时间生效
            httpRequest.setParams(httpParams);

            // 取得HTTP response
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            // 若状态码为200 ok
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                in = new BufferedReader(new InputStreamReader(httpResponse
                        .getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                String page = sb.toString();
                System.out.println("info:" + page);
                return page;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
