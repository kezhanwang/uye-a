package com.bjzt.uye.activity;



import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.MapUtil;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;

public class MapActivity extends BaseActivity {
	@BindView(R.id.baidu_map)
	MapView mMapView;
	@BindView(R.id.header_map)
	YHeaderView mHeader;

	private BaiduMap map;
	private InfoWindow mInfoWindow;
	private Marker marker;

	private double lng;
	private double lat;
	private String sName;
	private String address;
	private LatLng start;

	@Override
	protected int getLayoutID() {
		SDKInitializer.initialize(Global.getContext());
		return R.layout.activity_baidu_map;
	}

	@Override
	protected void initLayout(Bundle bundle) {
		String lat = LBSController.getInstance().getLa();
		String lng = LBSController.getInstance().getLo();
		if(!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lng)){
			start = new LatLng(Double.valueOf(lat), Double
					.valueOf(lng));
		}else{
			//设置默认起点
			start = new LatLng(39.9151120000, 116.4039630000);
		}
		// 初始换header部分
		mHeader.updateType(YHeaderView.TYPE_ABOUT);
		mHeader.setTitle(sName);
		mHeader.setIListener(mHeaderListener);

		// 去除放大缩小按钮
		 mMapView.showZoomControls(true);
		// 去除放大缩小按钮以及百度logo
//		int count = mMapView.getChildCount();
//		for (int i = 0; i < count; i++) {
//			View child = mMapView.getChildAt(i);
//			if (child instanceof ZoomControls || child instanceof ImageView) {
//				child.setVisibility(View.INVISIBLE);
//			}
//		}
		map = mMapView.getMap();
		setUpMap();
	}

	private IHeaderListener mHeaderListener = new IHeaderListener() {
		@Override
		public void onLeftClick() {
			finish();
		}
	};

	@Override
	protected void initExtras(Bundle bundle) {
		lng = getIntent().getDoubleExtra(IntentUtils.LNG, 0);
		lat = getIntent().getDoubleExtra(IntentUtils.LAT, 0);
		sName = getIntent().getStringExtra(IntentUtils.SNAME);
		address=getIntent().getStringExtra(IntentUtils.ADDRESS);
		
		//test code 108.953542,34.22527
//		lat = 34.2212080000;
//		lng = 108.9555180000;
		//34.1295220000,108.8400530000
//		lat = 34.129522;
//		lng = 108.840053;
//		DecimalFormat df = new DecimalFormat(".##########");
//		String strLa = df.format(lat);
//		lat = Double.parseDouble(strLa);
//		String strLo = df.format(lng);
//		lng = Double.parseDouble(strLo);

//		BigDecimal b = new BigDecimal(lat);
//		lat = b.setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue();
//		b = new BigDecimal(lng);
//		lng = b.setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue();
		MyLog.d(TAG,"[initExtras]" + " la:" + lat + " lng:" + lng);
	}

	private void setUpMap() {
//		map.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
//		map.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		LatLng point = new LatLng(lat, lng);
		addMarkersToMap(point);// 往地图上添加marker
	}

	/**
	 * 添加悬浮物
	 * @param point
	 * 要显示悬浮物的坐标
	 */
	private void addMarkersToMap(final LatLng point) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pin_red);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		// 在地图上添加Marker，并显示
		marker = (Marker) map.addOverlay(option);

		MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
		// 移动到某经纬度
		map.animateMapStatus(update);
		update = MapStatusUpdateFactory.zoomBy(5f);
		// 放大
		map.animateMapStatus(update);

		View view=getView(point);
		mInfoWindow = new InfoWindow(view, point, -90);
		// 显示InfoWindow
		map.showInfoWindow(mInfoWindow);

		MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(point);
		map.setMapStatus(statusUpdate);
		MapStatusUpdate zoomTo = MapStatusUpdateFactory.zoomTo(16);
		map.setMapStatus(zoomTo);

//		设置所有Overlay全部显示
//		new OverlayManager(map) {
//			
//			@Override
//			public boolean onMarkerClick(Marker arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public List<OverlayOptions> getOverlayOptions() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		}.zoomToSpan();
	}

	/**
	 * 开始导航
	 */
	public void startNavi(LatLng start, LatLng end) {
		// 构建 导航参数
		NaviParaOption para = new NaviParaOption();
		para.startPoint(start);
		String from=getResources().getString(R.string.map_from);
		String to=getResources().getString(R.string.map_to);
		para.startName(from);
		para.endPoint(end);
		para.endName(to);
		double startLat = start.latitude;
		double startLng = start.longitude;
		double endLat = end.latitude;
		double endLng = end.longitude;

		if (MapUtil.isIntallBaiduMap()) {
			// BaiduMapNavigation.openBaiduMapNavi(para, this);
			MapUtil.openBaiduMap(this, startLat, startLng, endLat, endLng, sName);

		} else if(MapUtil.isIntallGaodeMap()){
			MapUtil.openGaodeMap(this, endLat, endLng, sName);
		}else{
			String str=getResources().getString(R.string.map_noMap);
			showToast(str);
			try{
				BaiduMapNavigation.openWebBaiduMapNavi(para, this);
			}catch(Exception ee){
				MyLog.error(TAG, ee);
			}
		}
	}
	
	private View getView(final LatLng end){
		LayoutInflater li = (LayoutInflater) Global.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.map_layout_marker, null);
		TextView textSname = (TextView) view.findViewById(R.id.text_map_marker_sname);
		// textView.setBackgroundResource(R.drawable.custom_info_bubble);
		textSname.setTextColor(Color.BLACK);
		textSname.setText(sName);
		TextView textAddress=(TextView) view.findViewById(R.id.text_map_marker_address);
		textAddress.setText(address);
		Button btn = (Button) view.findViewById(R.id.btn_map_marker);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// map.hideInfoWindow();
				startNavi(start, end);
			}
		});
		return view;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
}
