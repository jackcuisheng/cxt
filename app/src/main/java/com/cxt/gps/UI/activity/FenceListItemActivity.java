package com.cxt.gps.UI.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cxt.gps.R;
import com.cxt.gps.entity.Gps;
import com.cxt.gps.util.PositionUtil;


public class FenceListItemActivity extends SuperActivity{
    private TextView tv_title;
    private ImageView iv_back;
    private MapView mapView;
    private AMap aMap;
    private String lat,lng,radius;
    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }
    private void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        mapView = findViewById(R.id.map);
    }
    private void init(){
        tv_title.setText(getResources().getString(R.string.fence));
        lat = getIntent().getStringExtra("latitude");
        lng = getIntent().getStringExtra("longitude");
        Gps gps = PositionUtil.gps84_To_Gcj02(Double.parseDouble(lat),Double.parseDouble(lng));
        radius = getIntent().getStringExtra("radius");
        //latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        latLng = new LatLng(gps.getWgLat(),gps.getWgLon());
        initMap();
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        aMap.addCircle(new CircleOptions().center(latLng).fillColor(Color.parseColor("#40c9e0e7")).radius(Double.parseDouble(radius)).strokeWidth(1).strokeColor(0xff000000));
    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
