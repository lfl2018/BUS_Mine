package com.mogu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.mogu.entity.nearwf.WifiList1;

/**
 * AMapV1地图中介绍如何显示世界图
 */
public class BasicMapActivity extends Activity implements OnClickListener,
		LocationSource, AMapLocationListener {

	private MapView mapView;
	private AMap aMap;
	private Button basicmap;
	private Button rsmap;
	private Marker marker;

	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private RadioGroup mGPSModeGroup;

	private TextView mLocationErrText;
	private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
	// 标记物
	private MarkerOptions markerOption;
	private ArrayList<WifiList1> list = new ArrayList<WifiList1>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basicmap);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写

		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			addMarkersToMap();
			setUpMap();

		}
		basicmap = (Button) findViewById(R.id.basicmap);
		basicmap.setOnClickListener(this);
		rsmap = (Button) findViewById(R.id.rsmap);
		rsmap.setOnClickListener(this);

		// mLocationErrText =
		// (TextView)findViewById(R.id.location_errInfo_text);
		// mLocationErrText.setVisibility(View.GONE);

	}

	@SuppressWarnings("unchecked")
	private void addMarkersToMap() {
		String NO = getIntent().getStringExtra("NO");
		String la = getIntent().getStringExtra("la");
		String lg = getIntent().getStringExtra("lg");
		String name = getIntent().getStringExtra("name");
		list = (ArrayList<WifiList1>) getIntent().getSerializableExtra("list");
		if (NO.equals("1")) {
			LatLng latlng = new LatLng(Double.valueOf(la), Double.valueOf(lg));
			markerOption = new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
					.position(latlng).title(name).draggable(true);
			aMap.addMarker(markerOption);
		} else if (NO.equals("2")){
			for (int i = 0; i < list.size(); i++) {
				LatLng latlng = new LatLng(Double.valueOf(list.get(i).getWeidu0()),
						Double.valueOf(list.get(i).getJingdu()));
				markerOption = new MarkerOptions()
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
						.position(latlng).title(list.get(i).getFenxmc()).draggable(true);
				aMap.addMarker(markerOption);
			}

		}

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		// aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		setupLocationStyle();
		aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

	}

	private void setupLocationStyle() {
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.gps_point));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(STROKE_COLOR);
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(5);
		// 设置圆形的填充颜色
		myLocationStyle.radiusFillColor(FILL_COLOR);
		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
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
		deactivate();
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
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.basicmap:
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
			break;
		case R.id.rsmap:
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
			break;
		}

	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				Log.e("爸爸", amapLocation.getLatitude() + "      "
						+ amapLocation.getLongitude());
				// mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				// mLocationErrText.setVisibility(View.VISIBLE);
				// mLocationErrText.setText(errText);
			}
		}

	}

}
