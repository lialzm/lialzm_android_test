package com.lialzm.android.third.location;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.lialzm.android.R;
import com.lialzm.android.ui.activity.base.BaseActivity;
import com.lialzm.android.util.LogUtil;

/**
 * 高德地图
 * Created by lcy on 2016/4/14.
 */
public class GaodeMapAty extends BaseActivity implements MapWrapper, LocationSource,
        GaodeLocation.OnLocationFinish {
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private LatLonPoint latLonPoint = new LatLonPoint(30.276159, 110.119014);
    private Marker regeoMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationsource_activity);
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
            //设置缩放大小
            aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            setUpMap();
        }
    }

    public void addData() {

    }

    @Override
    public void addMarkersToMap() {
//        LatLng southwest = new LatLng(latitude, longitude);
//        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(southwest).draggable(true));
    }

    @Override
    public void searchRouteResult(int routeType) {

    }

    LocationWrapper locationWrapper;

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        locationWrapper = GaodeLocation.getSingleton(this);
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                LocationUtils.convertToLatLng(latLonPoint), 15));
//        regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//        regeoMarker.setPosition(LocationUtils.convertToLatLng(latLonPoint));
        locationWrapper.startLocation();
        locationWrapper.setOnLocationFinishListener(this);
        /*regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        regeoMarker.setPosition(LocationUtils.convertToLatLng(latLonPoint));*/

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
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        locationWrapper.stopLocation();
    }

    @Override
    public void locationStart() {

    }

    @Override
    public void locationFinish(LocationInfo location) {
        LogUtil.d("locationFinish==" + location.getAddress());
        if (mListener != null && location != null) {
            if (location != null
                    && location.getErrorCode() == 0) {
                LogUtil.d("onLocationChanged==" + location.getLatitude() + "," + location.getLongitude());
                mListener.onLocationChanged(location);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
        locationWrapper.stopLocation();
    }

    @Override
    public void locationStop() {

    }
}
