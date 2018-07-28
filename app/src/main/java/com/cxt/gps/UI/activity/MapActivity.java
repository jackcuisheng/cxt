package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cxt.gps.R;
import com.cxt.gps.entity.DeviceData;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.presenter.SearchCarPresenter;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.ShowUtils;
import com.cxt.gps.util.UnicodeUtils;
import com.cxt.gps.view.ISearchCarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class MapActivity extends SuperActivity implements ISearchCarView,GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener{
    private MapView mapView;
    private AMap aMap;
    private GeocodeSearch geocodeSearch;
    private SearchView sv_query;
    private ListView lv_list;
    private SearchCarPresenter presenter = new SearchCarPresenter(this);
    private SharedPreferences sharePreference;
    private String addressName;
    private SimpleAdapter adapter;
    private String queryText;
    private String carNum;
    private TextView tv_title;
    private ImageView iv_back;
    private String account;
    private String vin;

    private LinearLayout ll_wifi;
    private LinearLayout ll_detail;
    private LinearLayout ll_navigate;
    private LinearLayout ll_device_list;
    private LinearLayout ll_hotpoint;
    private LinearLayout ll_hot_image;
    private LinearLayout ll_history_router;
    private LinearLayout ll_fence;
    private ViewPager viewPage;
    private MyPagerAdapter pagerAdapter;
    private LayoutInflater mInflater;

    private TextView tv_time;
    private TextView tv_speed;
    private TextView tv_battery;
    private TextView deviceID;
    private TextView tv_location;
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }
    public void initView(){
        mapView = findViewById(R.id.map);
        sv_query = findViewById(R.id.sv_query);
        lv_list = findViewById(R.id.lv_list);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        ll_wifi = findViewById(R.id.ll_wifi);
        ll_detail = findViewById(R.id.ll_detail);
        ll_navigate = findViewById(R.id.ll_navigate);
        ll_device_list = findViewById(R.id.ll_device_list);
        ll_hotpoint = findViewById(R.id.ll_hotpoint);
        ll_hot_image = findViewById(R.id.ll_hot_image);
        ll_history_router = findViewById(R.id.ll_history_router);
        ll_fence = findViewById(R.id.ll_fence);
        viewPage = findViewById(R.id.viewPage);

    }
    public void init(){
        sharePreference = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharePreference.getString("account","");
        iv_back.setVisibility(View.VISIBLE);
        carNum = getIntent().getStringExtra("carNum");
        if (!carNum.isEmpty()){
            tv_title.setText(carNum);
            new Thread(){
              public void run(){
                  try {
                      String response = GetPostUtil.sendPost(Constants.getQueryRequest("REQUEST_LAST_LOCATION", account, carNum));
                      response = response.trim();
                      response = UnicodeUtils.unicode2String(response);
                      GpsData data = ParseJsonUtil.getLastLocation(response);
                      vin = data.getVin();
                      toShowMapMaker(data);
                  }catch (Exception e){
                      Timber.d(e.getMessage());
                  }
              }
            }.start();
            presenter.getCarDetail("REQUEST_CAR_DETAIL_INFO",account,carNum);
        }

    }
    public void initEvents(){
        initMap();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                aMap.clear();
                new Thread(){
                    public void run(){
                        try {
                            String response = GetPostUtil.sendPost(Constants.getQueryRequest("REQUEST_LAST_LOCATION", account, carNum));
                            response = response.trim();
                            response = UnicodeUtils.unicode2String(response);
                            GpsData data = ParseJsonUtil.getLastLocation(response);
                            vin = data.getVin();
                            toShowMapMaker(data);
                        }catch (Exception e){
                            Timber.d(e.getMessage());
                        }
                    }
                }.start();
                presenter.getCarDetail("REQUEST_CAR_DETAIL_INFO",account,carNum);
            }
        };
        timer.schedule(task,30000,1000*30);
        ll_wifi.setOnClickListener(this);
        ll_detail.setOnClickListener(this);
        ll_navigate.setOnClickListener(this);
        ll_device_list.setOnClickListener(this);
        ll_hotpoint.setOnClickListener(this);
        ll_hot_image.setOnClickListener(this);
        ll_history_router.setOnClickListener(this);
        ll_fence.setOnClickListener(this);
        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                presenter.queryData();
                presenter.getCarDetail("REQUEST_CAR_DETAIL_INFO",account,carNum);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    lv_list.setVisibility(View.GONE);
                }else {
                    queryText = newText;
                    presenter.queryData();
                }
                return true;
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

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



    @Override
    public String getQuery() {
        return queryText;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public void toMainActivity(final List<HashMap<String, String>> list) {
        try {
            if (list!=null) {
                lv_list.setVisibility(View.VISIBLE);
                adapter = new SimpleAdapter(this, list, R.layout.map_car_list, new String[]{"carNum"}, new int[]{R.id.tv_item});
                lv_list.setAdapter(adapter);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        aMap.clear();
                        carNum = list.get(position).get("carNum").toString();
                        presenter.getLastLocation(carNum,account);
                        tv_title.setText(list.get(position).get("carNum").toString());
                        sv_query.setQuery(carNum,false);
                        sv_query.clearFocus();
                        presenter.getCarDetail("REQUEST_CAR_DETAIL_INFO",account,carNum);
                        lv_list.setVisibility(View.GONE);
                    }
                });

            }else {
                lv_list.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void toShowMapMaker(GpsData data) {
        LatLng latlng = new LatLng(data.getLatitude(),data.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(data.getLatitude(),data.getLongitude()), 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        if(data.getSetDenfendStatus().equals("1")){
            aMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_defend)));
        }
        else if(data.getControlStatus().equals("1")){
            aMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_control)));
        }else {
            aMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_normal)));
        }
    }

    @Override
    public void showFailedError() {
        ShowUtils.showCustomToast(getApplicationContext(),"请输入有效车牌！");
        //Toast.makeText(getApplicationContext(),"请输入有效车牌！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        showLoadingDialog("加载中...");
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showCarDetail(List<DeviceData> list) {
        try{
            List<View> mViewList = new ArrayList<View>();
            mInflater = getLayoutInflater();
            for (int i = 0; i < list.size(); i++) {
                View view = mInflater.inflate(R.layout.car_detail_popwindow, null);
                tv_time = view.findViewById(R.id.tv_time);
                tv_battery = view.findViewById(R.id.tv_battery);
                tv_location = view.findViewById(R.id.tv_location);
                tv_speed = view.findViewById(R.id.tv_speed);
                deviceID = view.findViewById(R.id.device_id);
                tv_time.setText(list.get(i).getLocateTime());
                tv_location.setText(list.get(i).getCurrentLocation());
                tv_battery.setText(list.get(i).getDeviceBatteryLevel());
                tv_speed.setText(list.get(i).getCarSpeed());
                deviceID.setText(list.get(i).getDeviceID());
                mViewList.add(view);
            }
            pagerAdapter = new MyPagerAdapter(mViewList);
            viewPage.setAdapter(pagerAdapter);
            viewPage.setCurrentItem(0);//设置当前pager
            pagerAdapter.notifyDataSetChanged();

        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
            LatLonPoint point = regeocodeResult.getRegeocodeQuery().getPoint();
            LatLng targetPos = new LatLng(point.getLatitude(), point.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetPos, 17));
            addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                    + "附近";
        }
    }
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ll_wifi:
                intent = new Intent(this,WifiActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
                break;
            case R.id.ll_detail:
                intent = new Intent(this,DetailActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_navigate:
                intent = new Intent(this,NavigateActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_device_list:
                intent = new Intent(this,DeviceListActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_hotpoint:
                intent = new Intent(this,ResidentPointActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_hot_image:
                intent = new Intent(this,HotPointActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_history_router:
                intent = new Intent(this,TrackActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.ll_fence:
                intent = new Intent(this,FenceActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
        }
    }
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        public MyPagerAdapter(List<View> mViewList){
            this.mViewList = mViewList;
        }
        @Override
        public int getCount() {//返回view数量
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position), 0);
            return mViewList.get(position);
        }

    }
}
