package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cxt.gps.R;
import com.cxt.gps.presenter.WifiPresenter;
import com.cxt.gps.view.IWifiView;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class WifiActivity extends SuperActivity implements IWifiView,GeocodeSearch.OnGeocodeSearchListener{
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_notice;
    private MapView mapView;
    private AMap aMap;
    private ListView lv_list;
    private SharedPreferences sharedPreferences;
    private String account;
    private GeocodeSearch search;
    private LatLng latlng;
    private WifiPresenter presenter = new WifiPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initView();
        init();
        initEvent();
        mapView.onCreate(savedInstanceState);
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_notice = findViewById(R.id.tv_notice);
        mapView = findViewById(R.id.map);
        lv_list = findViewById(R.id.lv_list);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        String carNum = getIntent().getStringExtra("carNum");
        tv_title.setText(carNum);
        presenter.loadInItData("REQUEST_HISTORICAL_TRACK",account,carNum);
        initMap();
    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        search = new GeocodeSearch(this);
        search.setOnGeocodeSearchListener(this);
    }
    public void initEvent(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void toMainActivity(final List<HashMap<String, String>> list) {
        try {

            if (list == null || (list.size()==1 && list.get(0).get("location").isEmpty())){
                tv_notice.setVisibility(View.VISIBLE);
                lv_list.setVisibility(View.GONE);
            }else {
                tv_notice.setVisibility(View.GONE);
                lv_list.setVisibility(View.VISIBLE);
                SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.wifi_item, new String[]{"dateTime", "location"}, new int[]{R.id.tv_date, R.id.tv_location});
                lv_list.setAdapter(adapter);
                String location = list.get(0).get("location").toString().trim();
                location = location.replaceAll("\\s*", "");
                GeocodeQuery query = new GeocodeQuery(location, "");
                search.getFromLocationNameAsyn(query);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        aMap.clear();
                        String location = list.get(position).get("location").toString().trim();
                        location = location.replaceAll("\\s*", "");
                        GeocodeQuery query = new GeocodeQuery(location, "");
                        search.getFromLocationNameAsyn(query);
                    }
                });
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }

    }

    @Override
    public void showFailedError() {

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
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS){
            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0){
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                latlng = new LatLng(address.getLatLonPoint().getLatitude(),address.getLatLonPoint().getLongitude());
                MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.location))
                        .position(latlng)
                        .draggable(true);
                aMap.addMarker(markerOption);
                CircleOptions options = new CircleOptions().center(latlng)
                        .fillColor(Color.parseColor("#80c9e0e7"))
                        .radius(300)
                        .strokeWidth(1)
                        .strokeColor(getResources().getColor(R.color.white));
                aMap.addCircle(options);
                aMap = mapView.getMap();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
            }
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
}
