package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.cxt.gps.R;
import com.cxt.gps.entity.Gps;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.presenter.FencePresenter;
import com.cxt.gps.util.PositionUtil;
import com.cxt.gps.view.IFenceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class FenceActivity extends SuperActivity implements View.OnClickListener ,IFenceView{
    private TextView tv_title;
    private ImageView iv_back;
    private MapView mapView;
    private AMap aMap;
    private TextView tv_my_location;
    private TextView tv_device_location;
    private TextView tv_one_key;
    private TextView tv_more;
    private SharedPreferences sharedPreferences;
    private String carNum,account;
    private FencePresenter presenter = new FencePresenter(this);
    private LatLng latLng;
    private String address;
    private String fenceID;
    private LinearLayout ll_more;
    private LinearLayout ll_all;
    private TextView tv_track_back;
    private TextView tv_fence_list;
    private TextView tv_new_fence;
    private TextView tv_search_navigate;
    private boolean isHasOnekey = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ll_more.setVisibility(View.GONE);
            }
        });
        tv_my_location.setOnClickListener(this);
        tv_device_location.setOnClickListener(this);
        tv_one_key.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        tv_one_key.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.deleteFence("REQUEST_DELETE_FENCE_BY_ID",account,fenceID);
                return true;
            }
        });
    }

    private void init() {
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_title.setText(carNum);
        initMap();
        initLoadData();
        tv_device_location.setBackgroundColor(getResources().getColor(R.color.top_layout));
    }
    private synchronized void initLoadData(){
        presenter.loadFenceList("REQUEST_ALL_FENCE",account,carNum,"","");
        presenter.loadDeviceData("REQUEST_LAST_LOCATION",account,carNum);
    }
    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        mapView = findViewById(R.id.map);
        tv_my_location = findViewById(R.id.tv_my_location);
        tv_device_location = findViewById(R.id.tv_device_location);
        tv_one_key = findViewById(R.id.tv_one_key);
        tv_more = findViewById(R.id.tv_more);
        ll_more = findViewById(R.id.ll_more);
        tv_track_back = findViewById(R.id.tv_track_back);
        tv_fence_list = findViewById(R.id.tv_fence_list);
        tv_search_navigate = findViewById(R.id.tv_search_navigate);
        tv_new_fence = findViewById(R.id.tv_new_fence);
        ll_all = findViewById(R.id.ll_all);
    }

    @Override
    public void onClick(View v) {
        resetBackground(v);
        switch (v.getId()){
            case R.id.tv_my_location:
                ll_more.setVisibility(View.GONE);
                getMyLocation();
                break;
            case R.id.tv_device_location:
                ll_more.setVisibility(View.GONE);
                presenter.loadDeviceData("REQUEST_LAST_LOCATION",account,carNum);
                break;
            case R.id.tv_one_key:
                try {
                    ll_more.setVisibility(View.GONE);
                    Gps gps = PositionUtil.gcj_To_Gps84(latLng.latitude,latLng.longitude);
                    //presenter.saveFence("REQUEST_SAVE_FENCE",account,carNum,"",address,200,"4",latLng.latitude,latLng.longitude,2);
                    presenter.saveFence("REQUEST_SAVE_FENCE",account,carNum,"",address,200,"4",gps.getWgLat(),gps.getWgLon(),2);
                }catch (NullPointerException e){
                    Timber.d(e.getMessage());
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
                break;
            case R.id.tv_more:
                ll_more.setVisibility(View.VISIBLE);
                setOnClickListener();
                break;
        }
    }
    private void setOnClickListener(){
        tv_track_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
            }
        });
        tv_fence_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FenceListActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
            }
        });
        tv_new_fence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(getApplicationContext(), NewFenceActivity.class);
                    intent.putExtra("carNum",carNum);
                    intent.putExtra("latitude",latLng.latitude);
                    intent.putExtra("longitude",latLng.longitude);
                    startActivity(intent);
                }catch (NullPointerException e){
                    showCustomToast(getApplicationContext(),"地理数据未获取！");
                    Timber.d(e.getMessage());
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }

            }
        });
        tv_search_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NavigateActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
            }
        });
    }
    public void getMyLocation(){
        try{
            aMap.clear();
            aMap.setMyLocationEnabled(true);
            aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void toShowSaveFenceResult(Map<String, String> map) {
        try{
            if (map.get("state").equals("1")){
                showCustomToast(this,"一键围栏，保存成功！");
                fenceID = map.get("fenceId");
                aMap.clear();
                aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_normal)));
                aMap.addCircle(new CircleOptions().center(latLng).center(latLng).fillColor(Color.parseColor("#40c9e0e7")).radius(200).strokeWidth(1).strokeColor(0xff000000));
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            }else if(map.get("state").equals("0")){
                showCustomToast(this,map.get("msg"));
            }else {
                showCustomToast(this,map.get("msg"));
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void toShowMapMaker(GpsData data) {
        try{
            aMap.clear();
            aMap.setMyLocationEnabled(false);
            latLng = new LatLng(data.getLatitude(),data.getLongitude());
            address = data.getCurrentLocation();
            if(isHasOnekey){
                aMap.addCircle(new CircleOptions().center(latLng).center(latLng).fillColor(Color.parseColor("#40c9e0e7")).radius(200).strokeWidth(1).strokeColor(0xff000000));
            }
            aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_normal)));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
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

    @Override
    public void toshowDeleteResult(Map<String, String> map) {
        try{
            if (map.get("state").equals("1")){
                showCustomToast(getApplicationContext(),"删除围栏成功！");
                aMap.clear();
                aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_normal)));
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            }else if(map.get("state").equals("0")){
                showCustomToast(getApplicationContext(),map.get("msg"));
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showAllFence(List<HashMap<String, String>> list) {
        try{
            for (int i = 0;i < list.size();i++){
                if (list.get(i).get("fenceType").equals("一键围栏")){
                    isHasOnekey = true;
                    break;
                }
            }

        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }

    private void resetBackground(View view){
        tv_my_location.setBackground(getResources().getDrawable(R.drawable.view_noborder));
        tv_device_location.setBackground(getResources().getDrawable(R.drawable.view_no_left_border));
        tv_one_key.setBackground(getResources().getDrawable(R.drawable.view_no_left_border));
        tv_more.setBackground(getResources().getDrawable(R.drawable.view_no_left_border));
        view.setBackgroundColor(getResources().getColor(R.color.top_layout));
    }
}
