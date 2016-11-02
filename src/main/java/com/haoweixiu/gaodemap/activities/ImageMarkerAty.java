package com.haoweixiu.gaodemap.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.VolleyError;
import com.haoweixiu.R;
import com.haoweixiu.activity.UnReciveOrderDetailActivity;
import com.haoweixiu.gaodemap.bean.MapBean;
import com.haoweixiu.gaodemap.net.RequestApiImp;
import com.haoweixiu.gaodemap.net.RequestListener;
import com.haoweixiu.gaodemap.net.RequestManager;
import com.haoweixiu.gaodemap.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageMarkerAty extends Activity implements OnMarkerClickListener,LocationSource,
		AMapLocationListener,AMap.OnMapClickListener,
		AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter , GeocodeSearch.OnGeocodeSearchListener{
	/**
	 * 基础地图
	 */
	private MapView mapView;
	private AMap aMap;
	private ArrayList<String> arrayList;
	private ArrayList<String> poList = new ArrayList<String>() ;
	private static final int MESSAGE_ONE = 0x001;
	private List<MapBean> mapBeanList;
	private List<MapBean> mapList = new ArrayList<MapBean>();
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	GeocodeSearch geocoderSearch;
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> cityList = new ArrayList<String>();
	/**
	 * 点击标记物弹出popWindow信息
	 */
	/**
	 * 展示popWindow布局
	 */
	private RelativeLayout mpop;
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_ONE:
					JSONArray json = (JSONArray) msg.obj;
					mapBeanList= JSON.parseArray(json.toString(),MapBean.class);
					int count = mapBeanList.size();
					for(int i=0;i<count;i++){
						if(!TextUtils.isEmpty(mapBeanList.get(i).getCityid())){
							mapList.add(mapBeanList.get(i));
							String address = mapBeanList.get(i).getCityname()+mapBeanList.get(i).getAddress();
							list.add(address);
							cityList.add(mapBeanList.get(i).getCityid());
						}
					}

					getLatlon(list,cityList);
					break;
			}
			super.handleMessage(msg);
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		String comeform="";
		comeform=getIntent().getStringExtra("activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_marker);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		new RequestManager(ImageMarkerAty.this);

		if(comeform.equals("MAINACTIVITY")){
			getMapOrder();
		}
		//从未接订单、已接订单跳转过来的，点击Map直接显示路线
		else{
			arrayList=new ArrayList<>();
			arrayList=getIntent().getStringArrayListExtra("list");
			//获取地址cityname+cityaddress
			String address = arrayList.get(2)+arrayList.get(3);
			//为maplist添加对象
			MapBean mapBean=new MapBean();
			mapBean.setCityid(arrayList.get(1));
			mapBean.setCityname(arrayList.get(2));
			mapBean.setDid(arrayList.get(0));
			mapBean.setAddress(arrayList.get(3));
			mapList.add(mapBean);
			list.add(address);
			cityList.add(arrayList.get(1));
			getLatlon(list,cityList);
		}
		mapView = (MapView) findViewById(R.id.map_image_marker);
		mpop = (RelativeLayout) findViewById(R.id.image_marker_pop);
		mapView.onCreate(savedInstanceState);// 必须要写
		aMap = mapView.getMap();
		setUpMap();
		initEvent();
	}

	private void initEvent() {
		aMap.setOnMarkerClickListener(this);
	}

	protected void getMapOrder() {
		RequestApiImp.getInstance().map_order(Constants.MAP_KEY, this, new RequestListener() {

			@Override
			public void requestSuccess(JSONArray json) {
				Message message = new Message();
				message.what = MESSAGE_ONE;
				message.obj = json;
				mHandler.sendMessage(message);
			}

			@Override
			public void requestSuccess(JSONObject json) {
			}

			@Override
			public void requestError(VolleyError e) {
			}

				});
	}

	private void addMarkers(double p1,double p2,String str_address) {
				final MarkerOptions mark = new MarkerOptions();
				BitmapDescriptor markerIcon = BitmapDescriptorFactory
						.fromResource(R.drawable.marker);
				LatLng latLng = new LatLng(p1,
						p2);
				mark.position(latLng).icon(markerIcon).title(str_address);

				//这里很简单就加了一个TextView，根据需求可以加载复杂的View
				aMap.addMarker(mark);
	}
	@Override
	public boolean onMarkerClick(Marker marker) {
		if(poList!=null){
			int size = poList.size();
			for (int i = 0; i <size ; i++) {
				if(poList.get(i).contains(marker.getTitle())){
//					int mapBeanListSize = mapBeanList.size();
					String did = mapList.get(i).getDid();
//					String title = marker.getTitle().substring(marker.getTitle().indexOf("市")+1,marker.getTitle().length());
//					for(int j = 0;j<mapBeanListSize;j++){
//						if(mapBeanList.get(j).getAddress().contains(title)||title.contains(mapBeanList.get(j).getAddress())){
//							did = mapBeanList.get(j).getDid();
//						}
//					}
					Log.i("did",""+did);
					Intent intent = new Intent(ImageMarkerAty.this, UnReciveOrderDetailActivity.class);
					intent.putExtra("info",poList.get(i));
					intent.putExtra("did",did);
					intent.putExtra("activity","ImageMarkerAty");
					startActivity(intent);
				}
			}
		}
		return true;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}


	public void getLatlon(List<String> list,List<String> cityList) {
		int i=0;
		do {
			try {
				Thread.sleep(200);
				String city = cityList.get(i);
				String address=list.get(i);
//			null西三旗桥南鲲鹏物业（原西三旗旅馆）一号楼1210 通州区颐瑞东里加州小镇C区161-1-401
//
				//GeocodeQuery query = new GeocodeQuery("通州区颐瑞东里加州小镇C区161-1-401", "北京");
				GeocodeQuery query = new GeocodeQuery(list.get(i), city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
				geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while (i<list.size());
//		for(int i=0;i<list.size();i++){
//
//			geocoderSearch.setOnGeocodeSearchListener(this);
//		}
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int i) {

		if (i == 1000) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				double lat =address.getLatLonPoint().getLatitude();
				double lng =address.getLatLonPoint().getLongitude();
				addMarkers(lat,lng,address.getFormatAddress());
				poList.add(String.valueOf(lat)+"@#"+String.valueOf(lng)+"@#"+address.getFormatAddress());
			} else {
			}
		} else {
		}
	}
	private void setUpMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}
	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (mListener != null && aMapLocation != null) {
			if (aMapLocation != null
					&& aMapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
				aMapLocation.getLatitude();
			}
		}
	}
	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		mListener = onLocationChangedListener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public View getInfoWindow(Marker marker) {

		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

	}

	@Override
	public void onMapClick(LatLng latLng) {

	}
}
