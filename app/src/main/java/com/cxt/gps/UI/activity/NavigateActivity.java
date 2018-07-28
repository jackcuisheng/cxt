package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.cxt.gps.R;
import com.cxt.gps.UI.activity.navigate.BusRouteActivity;
import com.cxt.gps.UI.activity.navigate.CustomDriveWayViewActivity;
import com.cxt.gps.UI.activity.navigate.WalkRouteCalculateActivity;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.presenter.NavigatePresenter;
import com.cxt.gps.view.INavigateView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import timber.log.Timber;

public class NavigateActivity extends SuperActivity implements INavigateView,LocationSource,AMapLocationListener,AMap.OnMapLongClickListener , AMap.OnMapScreenShotListener{
    private TextView tv_title;
    private ImageView iv_back;
    private String carNum;
    private MapView mapView;
    private AMap aMap;
    private NavigatePresenter presenter = new NavigatePresenter(this);
    private SharedPreferences sharedPreferences;
    private String account;
    private LatLng endPoint;
    private LatLng startPoint;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器
    private boolean isFirstLoc = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        mapView = findViewById(R.id.map);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_title.setText(carNum);
        initMap();
        presenter.loadData("REQUEST_LAST_LOCATION",account,carNum);

    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        initLocation();
    }
    public void initLocation(){
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置缓存
        mLocationOption.setLocationCacheEnable(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        //mLocationOption.setWifiActiveScan(true);
        mLocationOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    public void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public synchronized void drawPoint(){
        try {
            aMap.clear();
            aMap.addMarker(new MarkerOptions()
                    .position(startPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
            aMap.addMarker(new MarkerOptions()
                    .position(endPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));

            aMap.addPolyline((new PolylineOptions()).add(
                    startPoint, endPoint).color(
                    Color.GREEN));
            ArrayList<LatLng> latlngList = new ArrayList<LatLng>();

            latlngList.add(startPoint);
            latlngList.add(endPoint);
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < latlngList.size(); i++) {
                b.include(latlngList.get(i));
            }
            LatLngBounds bounds = b.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }
    public void onBusClick(View v){
        try{
            resetImage();
            findViewById(R.id.route_bus).setBackgroundResource(R.drawable.route_bus_select);
            Intent intent = new Intent(this, BusRouteActivity.class);
            intent.putExtra("start_lat", startPoint.latitude);
            intent.putExtra("start_lng", startPoint.longitude);
            intent.putExtra("end_lat", endPoint.latitude);
            intent.putExtra("end_lng", endPoint.longitude);
            startActivity(intent);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }
    public void onWalkClick(View v){
        try{
            resetImage();
            findViewById(R.id.route_walk).setBackgroundResource(R.drawable.route_walk_select);
            Intent intent = new Intent(this, WalkRouteCalculateActivity.class);
            intent.putExtra("start_lat", startPoint.latitude);
            intent.putExtra("start_lng", startPoint.longitude);
            intent.putExtra("end_lat", endPoint.latitude);
            intent.putExtra("end_lng", endPoint.longitude);
            startActivity(intent);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
    public void onDriveClick(View v){
        try{
            resetImage();
            findViewById(R.id.route_drive).setBackgroundResource(R.drawable.route_drive_select);
            Intent intent = new Intent(this, CustomDriveWayViewActivity.class);
            intent.putExtra("start_lat", startPoint.latitude);
            intent.putExtra("start_lng", startPoint.longitude);
            intent.putExtra("end_lat", endPoint.latitude);
            intent.putExtra("end_lng", endPoint.longitude);
            startActivity(intent);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
    private void resetImage(){
        findViewById(R.id.route_bus).setBackgroundResource(R.drawable.route_bus_normal);
        findViewById(R.id.route_drive).setBackgroundResource(R.drawable.route_drive_normal);
        findViewById(R.id.route_walk).setBackgroundResource(R.drawable.route_walk_normal);
    }

    @Override
    public synchronized void toShowMapMaker(GpsData data) {
        try {
            endPoint = new LatLng(data.getLatitude(),data.getLongitude());
            onLocationChanged(mLocationClient.getLastKnownLocation());
            drawPoint();
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
        //hideCustomToast();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //hideCustomToast();
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

    @Override
    public  void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation = mLocationClient.getLastKnownLocation();
                    startPoint = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                    if (isFirstLoc) {
                        //设置缩放级别
                        //aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        //将地图移动到定位点
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                        //点击定位按钮 能够将地图的中心移动到定位点
                        mListener.onLocationChanged(amapLocation);
                        //添加图钉
                        //aMap.addMarker(getMarkerOptions(amapLocation));
                        //获取定位信息
                        startPoint = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                        if (startPoint != null){
                            aMap.setMyLocationEnabled(false);
                        }
                        //aMap.setMyLocationEnabled(true);
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    showCustomToast(getApplicationContext(), buffer.toString());
                        isFirstLoc = false;
                    }


                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
//                    showCustomToast(this,"定位失败，可能定位未开启！");
//                    hideCustomToast();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {

    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
}
