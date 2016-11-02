package com.haoweixiu.gaodemap.util;


import android.content.Context;

public class Constants {

	public static final String BASE_URL = "http://www.hweixiu.com/";
	//地图订单接口
	public static final String MAP_ORDER =BASE_URL+ "apporders/map_order";
	//未接订单详情页面
	public static final String NO_RECEIVE_ORDER_DETAIL =BASE_URL+ "Apporders/id_order";
	//已接未完成订单详情
	public static final String RECEIVED_ORDER_DETAIL =BASE_URL+ "Apporders/outid_order";

	public static final int FUNC_SUCCESS = 0;               //成功
	public static final String MAP_KEY = "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA";//成功

	public static final String price = "预估: ¥";
	public static String strFormat(Context context, int id, Object... args) {
		return String.format(context
				.getResources().getString(id), args);
	}
	public static  String setPhone(String s){
		if (s == null || s.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i != 3 && i != 8 && s.charAt(i) == ' ') {
				continue;
			} else {
				sb.append(s.charAt(i));
				if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
					sb.insert(sb.length() - 1, "-");
				}
			}
		}
		return sb.toString();
	}
}
