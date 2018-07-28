package com.cxt.gps.UI.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cxt.gps.R;
import com.cxt.gps.navigate.util.mapUtil.ToastUtil;

import timber.log.Timber;

public class ResidentPointItemActivity extends SuperActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener{
    private TextView tv_title;
    private ImageView iv_back;
    private MapView mapView;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private double lat,lng;
    private String count;
    private String addressName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initView();
        init();
        mapView.onCreate(savedInstanceState);
    }
    public void initView(){
       tv_title = findViewById(R.id.tv_title);
       iv_back = findViewById(R.id.iv_back);
       mapView = findViewById(R.id.map);

    }
    public void init(){
        try{
            tv_title.setText(getIntent().getStringExtra("carNum"));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            initMap();
            count = getIntent().getStringExtra("count");
            lat = Double.parseDouble(getIntent().getStringExtra("latitude"));
            lng = Double.parseDouble(getIntent().getStringExtra("longitude"));
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat,lng), 200,
                    GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }
    public void initMap(){
        if(aMap == null){
            aMap = mapView.getMap();
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    protected  void onPause() {

        super.onPause();
        mapView.onPause();
    }
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        mapView.onSaveInstanceState(bundle);
    }
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                LatLonPoint point = result.getRegeocodeQuery().getPoint();
                LatLng targetPos = new LatLng(point.getLatitude(), point.getLongitude());
                CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
                aMap.moveCamera(cu);
                // aMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.mycar)));
                //regeoMarker.setPosition(latLng);
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat,lng), 15));
                addMarkersToMap(new LatLng(lat,lng),count);
                aMap.setInfoWindowAdapter(this);//AMap类中
                aMap.setOnInfoWindowClickListener(this);



            } else {
                ToastUtil.show(getApplicationContext(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng,String msg) {
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_normal))
                //.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        Marker marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(this).inflate(R.layout.info_window_layout,null);//custom_info_window为自定义的layout文件
        TextView name = infoWindow.findViewById(R.id.content);
        TextView tv_count = infoWindow.findViewById(R.id.count);
        name.setText(addressName);
        tv_count.setText(count+"次");
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
