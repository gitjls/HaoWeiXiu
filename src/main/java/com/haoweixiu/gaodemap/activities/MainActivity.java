package com.haoweixiu.gaodemap.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.MapsInitializer;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.haoweixiu.R;

import java.util.ArrayList;

/**
 * AMapV2地图demo总汇
 */
public final class MainActivity extends Activity implements GeocodeSearch.OnGeocodeSearchListener{
	GeocodeSearch geocoderSearch;
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> positionList = new ArrayList<String>();
//	public void getLatlon(List<String> list) {
//		for(int i=0;i<list.size();i++){
//			GeocodeQuery query = new GeocodeQuery(list.get(i), "北京");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
//			geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
//			geocoderSearch.setOnGeocodeSearchListener(this);
//		}
//	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int i) {
//		if (i == 1000) {
//			if (result != null && result.getGeocodeAddressList() != null
//					&& result.getGeocodeAddressList().size() > 0) {
//				GeocodeAddress address = result.getGeocodeAddressList().get(0);
//				String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//						+ address.getFormatAddress();
//				double lat =address.getLatLonPoint().getLatitude();
//				double lng =address.getLatLonPoint().getLongitude();
//				positionList.add(String.valueOf(lat)+"@#"+String.valueOf(lng)+"@#"+address.getFormatAddress());
//			} else {
//			}
//		} else {
//		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setTitle("地图Demo" + MapsInitializer.getVersion());
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,ImageMarkerAty.class);
//				intent.putStringArrayListExtra("list1",positionList);
				startActivity(intent);
			}
		});
//		geocoderSearch = new GeocodeSearch(this);
//		list.add("沙河北大桥公交站");
//		list.add("朱辛庄公交站");
//		list.add("龙泽地铁站");
//		list.add("沙河地铁站");
//		list.add("霍营地铁站");
//		list.add("通州区新华街道天桥湾社区");
//		getLatlon(list);
//		geocoderSearch.setOnGeocodeSearchListener(this);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

}
