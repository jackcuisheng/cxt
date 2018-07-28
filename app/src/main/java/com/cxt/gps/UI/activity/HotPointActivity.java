package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Gradient;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.TileOverlayOptions;
import com.cxt.gps.R;
import com.cxt.gps.UI.view.DoubleSeekBar;
import com.cxt.gps.presenter.HotPointPresenter;
import com.cxt.gps.view.IResidentPointView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HotPointActivity extends SuperActivity implements IResidentPointView, View.OnClickListener {
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),
            Color.argb(255 / 3 * 2, 0, 255, 0),
            Color.rgb(125, 191, 0),
            Color.rgb(185, 71, 0),
            Color.rgb(255, 0, 0)
    };
    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = { 0.0f,
            0.10f, 0.20f, 0.60f, 1.0f };
    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(
            ALT_HEATMAP_GRADIENT_COLORS, ALT_HEATMAP_GRADIENT_START_POINTS);
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_carNum;
    private MapView mapView;
    private AMap aMap;
    private DoubleSeekBar seekBar;
    private TextView tv_week;
    private TextView tv_month;
    private TextView tv_season;
    private RadioGroup rg_group;
    private RadioButton rb_sate;
    private RadioButton rb_base;
    private RadioButton rb_wifi;
    private Button btn_query;
    private String carNum;
    private HotPointPresenter presenter = new HotPointPresenter(this);
    private SharedPreferences sharedPreferences;
    private String account;
    private String timeType = "week";
    private String locationType = "010";
    private int start;
    private String startTime;
    private int end;
    private String endTime;
    private LatLng[] latLngs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_point);
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
        tv_week.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_season.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sate:
                        locationType = "010";
                        break;
                    case R.id.rb_base:
                        locationType = "100";
                        break;
                    case R.id.rb_wifi:
                        locationType = "001";
                        break;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new DoubleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(DoubleSeekBar seekBar, double progressLow, double progressHigh) {
                start = (int) progressLow;
                end = (int) progressHigh;
                startTime = start+":00:00";
                if (end>1){
                    endTime = end-1+":59:59";
                    SimpleDateFormat sdf = new SimpleDateFormat("HH" +
                            ":mm:ss");
                    try {
                        startTime = sdf.format(sdf.parse(startTime));
                        endTime = sdf.format(sdf.parse(endTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    showCustomToast(getApplicationContext(),"请选取大于1小时的时间段！");
                }

            }
        });
    }

    private void init() {
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_carNum.setText(carNum);
        tv_title.setText(getResources().getString(R.string.hot_point));
        tv_week.setBackgroundColor(getResources().getColor(R.color.top_layout));
        tv_week.setTextColor(getResources().getColor(R.color.white));
        initMap();
    }
    public void initMap(){
        if(aMap == null){
            aMap = mapView.getMap();
        }

    }
    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_carNum = findViewById(R.id.tv_carNum);
        mapView = findViewById(R.id.map);
        seekBar = findViewById(R.id.sb_seek);
        tv_week = findViewById(R.id.tv_week);
        tv_month = findViewById(R.id.tv_month);
        tv_season = findViewById(R.id.tv_season);
        rg_group = findViewById(R.id.rg_group);
        rb_sate = findViewById(R.id.rb_sate);
        rb_base = findViewById(R.id.rb_base);
        rb_wifi = findViewById(R.id.rb_wifi);
        btn_query = findViewById(R.id.btn_query);
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
    public void toMainActivity(List<HashMap<String, String>> list) {
        try {
            aMap.clear();
            latLngs = new LatLng[list.size()];
            for (int i = 0; i < list.size();i++){
                latLngs[i] = new LatLng(Double.parseDouble(list.get(i).get("latitude")),Double.parseDouble(list.get(i).get("longitude")));
            }
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < latLngs.length; i++) {
                b.include(latLngs[i]);
            }
            LatLngBounds bounds = b.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

            HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
            builder.data(Arrays.asList(latLngs)) // 设置热力图绘制的数据
                    .gradient(ALT_HEATMAP_GRADIENT); // 设置热力图渐变，有默认值 DEFAULT_GRADIENT，可不设置该接口
            // Gradient 的设置可见参考手册
            // 构造热力图对象
            HeatmapTileProvider heatmapTileProvider = builder.build();
            // 初始化 TileOverlayOptions
            TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
            tileOverlayOptions.tileProvider(heatmapTileProvider); // 设置瓦片图层的提供者
            // 向地图上添加 TileOverlayOptions 类对象
            aMap.addTileOverlay(tileOverlayOptions);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showFailedError() {
        showCustomToast(this,"此条件下查无数据，请换条件再次查询！");
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_week:
                resetBackground();
                tv_week.setBackgroundColor(getResources().getColor(R.color.top_layout));
                tv_week.setTextColor(getResources().getColor(R.color.white));
                timeType = "week";
                break;
            case R.id.tv_month:
                resetBackground();
                tv_month.setTextColor(getResources().getColor(R.color.white));
                tv_month.setBackgroundColor(getResources().getColor(R.color.top_layout));
                timeType = "month";
                break;
            case R.id.tv_season:
                resetBackground();
                tv_season.setTextColor(getResources().getColor(R.color.white));
                tv_season.setBackgroundColor(getResources().getColor(R.color.top_layout));
                timeType = "season";
                break;
            case R.id.btn_query:
                presenter.loadData("REQUEST_CZD",account,carNum,startTime,endTime,timeType,locationType);
        }
    }
    public void resetBackground(){
        tv_week.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_month.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_season.setBackground(getResources().getDrawable(R.drawable.view_bg));
        tv_week.setTextColor(getResources().getColor(R.color.top_layout));
        tv_month.setTextColor(getResources().getColor(R.color.top_layout));
        tv_season.setTextColor(getResources().getColor(R.color.top_layout));
    }
}
