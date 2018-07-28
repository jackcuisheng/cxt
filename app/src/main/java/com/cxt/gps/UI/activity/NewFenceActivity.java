package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.cxt.gps.R;
import com.cxt.gps.entity.Gps;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.navigate.util.mapUtil.ToastUtil;
import com.cxt.gps.presenter.NewFencePresenter;
import com.cxt.gps.util.PositionUtil;
import com.cxt.gps.view.IFenceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class NewFenceActivity  extends SuperActivity implements IFenceView,AMap.OnCameraChangeListener,GeocodeSearch.OnGeocodeSearchListener,Inputtips.InputtipsListener{
    private TextView tv_carNum;
    private TextView tv_title;
    private ImageView iv_back;
    private MapView mapView;
    private AMap aMap;
    private SearchView sv_query;
    private ListView lv_list;
    private TextView tv_address;
    private SeekBar sb_seekbar;
    private EditText et_fenceName;
    private RadioGroup rg_group;
    private RadioButton rb_in;
    private RadioButton rb_out;
    private RadioButton rb_stop;
    private Button btn_save;
    private String carNum,account;
    private SharedPreferences sharedPreferences;
    private NewFencePresenter presenter = new NewFencePresenter(this);
    private double radius;
    private int condition = 2;
    private LatLng target = null;
    private GeocodeSearch geocoderSearch;
    private SimpleAdapter adapter;
    private List<Map<String,String>> addresses = new ArrayList<>();
    private UiSettings mUiSettings;
    private InputtipsQuery inputquery;
    private Inputtips inputTips;
    private TextView tv_now;
    private LatLng latLng;
    private String addressName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fence);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_carNum = findViewById(R.id.tv_carNum);
        iv_back = findViewById(R.id.iv_back);
        mapView = findViewById(R.id.map);
        sv_query = findViewById(R.id.sv_query);
        lv_list = findViewById(R.id.lv_list);
        tv_address = findViewById(R.id.tv_address);
        sb_seekbar = findViewById(R.id.sb_seekbar);
        et_fenceName = findViewById(R.id.et_fenceName);
        rg_group = findViewById(R.id.rg_group);
        rb_in = findViewById(R.id.rb_in);
        rb_out = findViewById(R.id.rb_out);
        rb_stop = findViewById(R.id.rb_stop);
        btn_save = findViewById(R.id.btn_save);
        tv_now = findViewById(R.id.tv_progress_now);

    }
    private void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_carNum.setText(carNum);
        tv_title.setText(getResources().getString(R.string.new_fence));
        initMap();
        radius = sb_seekbar.getProgress();
        tv_now.setText(radius+"米");
        latLng = new LatLng(getIntent().getDoubleExtra("latitude",0.00),getIntent().getDoubleExtra("longitude",0.00));
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude), 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);
    }
    public void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.animateCamera(CameraUpdateFactory.zoomTo(17 ));
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.setOnCameraChangeListener(this);
    }
    private void initEvents(){
        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lv_list.setVisibility(View.VISIBLE);
                inputquery = new InputtipsQuery(query, null);
                inputquery.setCityLimit(true);
                inputTips = new Inputtips(getApplicationContext(), inputquery);
                inputTips.setInputtipsListener(NewFenceActivity.this);
                inputTips.requestInputtipsAsyn();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                lv_list.setVisibility(View.VISIBLE);
                inputquery = new InputtipsQuery(newText, null);
                inputquery.setCityLimit(true);
                inputTips = new Inputtips(getApplicationContext(), inputquery);
                inputTips.setInputtipsListener(NewFenceActivity.this);
                inputTips.requestInputtipsAsyn();
                return true;
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_in:
                        condition = 1;
                        break;
                    case R.id.rb_out:
                        condition = 2;
                        break;
                    case R.id.rb_stop:
                        condition = 3;
                        break;
                }
            }
        });
        sb_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                aMap.clear();
                radius = progress;
                paintCircle(target,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radius = seekBar.getProgress();
                tv_now.setText(radius+"米");
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_fenceName.getText().toString().isEmpty()) {
                    Gps gps = PositionUtil.gcj_To_Gps84(target.latitude,target.longitude);
                    //presenter.saveFence("REQUEST_SAVE_FENCE", account, carNum, et_fenceName.getText().toString(),addressName , radius, "1", latLng.latitude, latLng.longitude, condition);
                    presenter.saveFence("REQUEST_SAVE_FENCE", account, carNum, et_fenceName.getText().toString(),addressName , radius, "1", gps.getWgLat(), gps.getWgLon(), condition);
                }else {
                    showCustomToast(getApplicationContext(),"围栏名字不能为空！");
                }
            }
        });

    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        target = cameraPosition.target;
        aMap.clear();
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(target.latitude,target.longitude), 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int i) {
        try {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                LatLonPoint point = result.getRegeocodeQuery().getPoint();
                LatLng targetPos = new LatLng(point.getLatitude(), point.getLongitude());
                CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
                aMap.moveCamera(cu);
                aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(targetPos.latitude, targetPos.longitude)));
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                tv_address.setText(addressName);
                paintCircle(new LatLng(targetPos.latitude, targetPos.longitude),200);

            } else {
                ToastUtil.show(getApplicationContext(), R.string.no_result);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onGetInputtips(final List<Tip> list, int a) {
        addresses.clear();
        if (list!=null) {
            Map<String,String> map = null;
            for (int i = 0; i < list.size(); i++) {
                map = new HashMap<>();
                if (list.get(i).getName() == null) {
                    map.put("name", "");
                } else {
                    map.put("name", list.get(i).getName());
                }
                if (list.get(i).getAddress() == null) {
                    map.put("address", "");
                } else {
                    map.put("address", list.get(i).getAddress());
                }
                if (list.get(i).getDistrict() == null) {
                    map.put("district", "");
                } else {
                    map.put("district", list.get(i).getDistrict());
                }
                addresses.add(map);
            }
            if (adapter == null) {
                adapter = new SimpleAdapter(getApplicationContext(), addresses, R.layout.activity_newfence_list_address, new String[]{"name", "address", "district"}, new int[]{R.id.tv_name, R.id.tv_address, R.id.tv_district});
                lv_list.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LatLonPoint point = list.get(position).getPoint();
                    LatLng latlng = new LatLng(point.getLatitude(),point.getLongitude());
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latlng.latitude,latlng.longitude), 200,
                            GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    geocoderSearch.getFromLocationAsyn(query);
                    lv_list.setVisibility(View.GONE);

                }
            });

        }
    }
    private void paintCircle(LatLng latLng, double radio){
        CircleOptions options=new CircleOptions();
        Marker maker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        maker.showInfoWindow();
        options.center(latLng).fillColor(Color.parseColor("#40c9e0e7")).radius(radio).strokeWidth(1).strokeColor(0xff000000);
        aMap.addCircle(options);
        aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
                showCustomToast(this,"围栏保存成功！");
                Intent intent = new Intent(this,FenceListActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
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

    }

    @Override
    public void showAllFence(List<HashMap<String, String>> list) {

    }
}
