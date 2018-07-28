package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.cxt.gps.R;
import com.cxt.gps.presenter.TrackPresenter;
import com.cxt.gps.util.ScreenUtils;
import com.cxt.gps.view.ITrackView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

import static android.view.View.*;

public class TrackActivity extends SuperActivity implements ITrackView,OnClickListener {
    private SharedPreferences sharedPreferences ;
    private String account;
    private TrackPresenter presenter = new TrackPresenter(this);
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_carNum;
    private MapView mapView;
    private AMap aMap;
    private int speed_status = 1100;
    private int speed_add_s = 500;
    private int ScreenWidth = 1;
    private int ScreenHeight = 1;
    private int StatusHeight=0;
    private double mileage = 0.00;
    Point p ;
    Projection projection;
    private MarkerOptions option;
    // 存放所有坐标的数组
    private ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
    private ArrayList<LatLng> latlngList_path = new ArrayList<LatLng>();

    Marker run_marker = null,s_marker = null,e_marker = null;
    BitmapDescriptor bitmap;

    private RelativeLayout rl_play_rewind_fast;
    private ImageView iv_fast_forward;
    private ImageView iv_rewind;
    private ImageView iv_play_pause;
    private SeekBar processbar;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private TextView tv_speed,tv_mileage,tv_oil;

    private int pause =0;

    private LinearLayout ll_today;
    private LinearLayout ll_yesterday;
    private LinearLayout ll_three_days_ago;
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private int index;
    // 当前fragment的index
    private int currentIndex=0;
    SimpleDateFormat sdf1;
    SimpleDateFormat sdf2;
    private String sTime;
    private String eTime;
    private String stopSecond;
    private String distance;
    private TextView notice;
    public Handler timer = new Handler();// 定时器
    public Runnable runnable = null;
    private String speed;
    private String carNum;
    private List<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try{
                    int curpro = processbar.getProgress();
                    if (curpro != processbar.getMax()) {
                        speed = list1.get(curpro).get("speed").toString();
                        tv_speed.setText(speed);
                        processbar.setProgress(curpro + 1);
                        timer.postDelayed(runnable, speed_status);// 延迟0.5秒后继续执行
                    } else {
                        //Toast.makeText(getApplicationContext(),"播放完毕", 0).show();
                        showCustomToast(getApplicationContext(), "播放完毕");
                        rl_play_rewind_fast.setBackgroundResource(0);
                        timer.removeCallbacks(runnable);
                        iv_fast_forward.setVisibility(GONE);
                        iv_rewind.setVisibility(GONE);
                        iv_play_pause.setBackgroundResource(0);
                        iv_play_pause.setBackgroundResource(R.drawable.vehicle_play_normal);
                        pause = 0;
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initView();
        init();
        initEvents();
        mapView.onCreate(savedInstanceState);
    }

    private void initEvents() {
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_fast_forward.setOnClickListener(this);
        iv_rewind.setOnClickListener(this);
        iv_play_pause.setOnClickListener(this);
        processbar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        // 进度条拖动时 执行相应事件
        processbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 复写OnSeeBarChangeListener的三个方法
            // 第一个时OnStartTrackingTouch,在进度开始改变时执行
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            // 第二个方法onProgressChanged是当进度发生改变时执行
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                latlngList_path.clear();
                if (progress != 0) {

                    if(latlngList.size() >= 2){
                        drawLine(latlngList_path,list1,progress);
                    }
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // 第三个是onStopTrackingTouch,在停止拖动时执行
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 要做的事情
                handler.sendMessage(Message.obtain(handler, 1));
            }
        };
        ll_today.setOnClickListener(this);
        ll_yesterday.setOnClickListener(this);
        ll_three_days_ago.setOnClickListener(this);
        ScreenWidth = ScreenUtils.getScreenWidth(this);
        ScreenHeight = ScreenUtils.getScreenHeight(this);
        StatusHeight = ScreenUtils.getStatusHeight(this);
    }
    private void initMap(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
    }
    private void init() {
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        tv_carNum.setText(carNum);
        tv_title.setText(getResources().getString(R.string.track));
        initMap();
        imagebuttons = new ImageView[3];
        imagebuttons[0]= (ImageView) findViewById(R.id.iv_today);
        imagebuttons[1]= (ImageView) findViewById(R.id.iv_yesterday);
        imagebuttons[2]= (ImageView) findViewById(R.id.iv_three_days_ago);
        //imagebuttons[3]= (ImageView) findViewById(R.id.iv_custom);
        textviews = new TextView[3];
        textviews[0]= (TextView) findViewById(R.id.tv_today);
        textviews[1]= (TextView) findViewById(R.id.tv_yesterday);
        textviews[2]= (TextView) findViewById(R.id.tv_three_days_ago);
        processbar.setSelected(false);
    }

    private void initView() {
        tv_carNum = findViewById(R.id.tv_carNum);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        mapView = findViewById(R.id.map);
        rl_play_rewind_fast = (RelativeLayout) findViewById(R.id.rl_play_rewind_fast);
        iv_fast_forward = (ImageView) findViewById(R.id.iv_fast_forward);
        iv_rewind = (ImageView) findViewById(R.id.iv_rewind);
        iv_play_pause = (ImageView) findViewById(R.id.iv_play_pause);
        notice = findViewById(R.id.tv_notice);
        processbar = (SeekBar) findViewById(R.id.skBarTrackPlay);
        tv_start_time= (TextView) findViewById(R.id.tv_start_time);
        tv_end_time= (TextView) findViewById(R.id.tv_end_time);
        tv_speed= (TextView) findViewById(R.id.tv_speed);
        tv_mileage= (TextView) findViewById(R.id.tv_mileage);
        tv_oil= (TextView) findViewById(R.id.tv_oil);
        ll_today= (LinearLayout) findViewById(R.id.ll_today);
        ll_yesterday= (LinearLayout) findViewById(R.id.ll_yesterday);
        ll_three_days_ago= (LinearLayout) findViewById(R.id.ll_three_days_ago);

    }
    public void searchCtro(int n) {
        index = n;
        imagebuttons[currentIndex].setBackgroundResource(0);
        textviews[currentIndex].setTextColor(this.getResources().getColor(R.color.vehicle_color_a0a9b8));
        imagebuttons[n].setBackgroundResource(R.drawable.vehicle_down_arrow);
        textviews[n].setTextColor(this.getResources().getColor(R.color.color_discovery_default_yellow));
        currentIndex=n;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            aMap.clear();
            timer.removeCallbacks(runnable);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fast_forward:
                try {
                    if(speed_status>100)
                    {
                        speed_status = speed_status-speed_add_s;
                        showCustomToast(getApplicationContext(),"速度加一");
                    }else{
                        showCustomToast(getApplicationContext(),"已经最大值");
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
                break;
            case R.id.iv_rewind:
                try{
                    if(speed_status<5100)
                    {
                        speed_status = speed_status+speed_add_s;
                        showCustomToast(getApplicationContext(),"速度减一");
                    }else{
                        showCustomToast(getApplicationContext(),"已经最小值");
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
                break;
            case R.id.iv_play_pause:
                try {
                    if(pause==0){
                        if (latlngList.size() > 0) {
                            rl_play_rewind_fast.setBackgroundResource(R.drawable.view_bg);
                            iv_fast_forward.setVisibility(VISIBLE);
                            iv_rewind.setVisibility(VISIBLE);
                            iv_play_pause.setBackgroundResource(0);
                            iv_play_pause.setBackgroundResource(R.drawable.vehicle_pause_normal);
                            pause=1;
                            // 假如当前已经回放到最后一点 置0
                            if (processbar.getProgress() == processbar.getMax()) {
                                processbar.setProgress(0);
                            }
                            timer.postDelayed(runnable, 100);
                        }

                    }else{
                        timer.removeCallbacks(runnable);
                        rl_play_rewind_fast.setBackgroundResource(0);
                        iv_fast_forward.setVisibility(GONE);
                        iv_rewind.setVisibility(GONE);
                        iv_play_pause.setBackgroundResource(0);
                        iv_play_pause.setBackgroundResource(R.drawable.vehicle_play_normal);
                        pause=0;
                    }
                    break;
                }catch (Exception e){
                   Timber.d(e.getMessage());
                }

            case R.id.ll_today:
                searchCtro(0);
                mileage = 0;
                sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sTime=sdf1.format((Calendar.getInstance()).getTime());
                eTime=sdf2.format((Calendar.getInstance()).getTime());
                stopSecond="180";
                distance="0.1";
                presenter.loadData("REQUEST_HISTORICAL_TRACK",account,carNum,sTime,eTime,"","1");
                break;
            case R.id.ll_yesterday:
                searchCtro(1);
                mileage = 0;
                Calendar   cal   =   Calendar.getInstance();
                cal.add(Calendar.DATE,   -1);
                sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                sTime=sdf1.format(cal.getTime());
                eTime=sdf2.format(cal.getTime());
                stopSecond="180";
                distance="0.1";
                presenter.loadData("REQUEST_HISTORICAL_TRACK",account,carNum,sTime,eTime,"","1");
                break;
            case R.id.ll_three_days_ago:
                searchCtro(2);
                mileage = 0;
                sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                eTime=sdf2.format((Calendar.getInstance()).getTime());
                Calendar   cal2   =   Calendar.getInstance();
                cal2.add(Calendar.DATE,   -2);
                sTime=sdf1.format(cal2.getTime());
                stopSecond="180";
                distance="0.1";
                presenter.loadData("REQUEST_HISTORICAL_TRACK",account,carNum,sTime,eTime,"","1");
                break;

            default:
                break;
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
    private void drawLine(ArrayList<LatLng> listlatlng,List<HashMap<String,String >> list,int current) {
        // TODO Auto-generated method stub
        try{
            LatLng replayPoint = latlngList.get(current - 1);

            projection = aMap.getProjection();
            p =projection.toScreenLocation(replayPoint);
            //showLoadingDialog("正在"+p.y+"-"+ScreenHeight+"-"+StatusHeight);
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.car_normal);
            option = new MarkerOptions().position(replayPoint).icon(bitmap);
            if (run_marker == null) {
                run_marker =  aMap.addMarker(option);
            }else{
                run_marker.setPosition(replayPoint);
            }
            if((0 >= p.x || p.x >= ScreenWidth) || (0 >= p.y || p.y >=  (ScreenHeight-StatusHeight*11))){
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(run_marker.getPosition(),15),1000,null);
            }

            run_marker.setAnchor(0.5f, 0.5f);
            int run_dir = Integer.parseInt(list.get(processbar.getProgress()).get("direction").toString());
            run_marker.setRotateAngle(360-run_dir);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }



    }

    @Override
    public void toMainActivity(List<HashMap<String, String>> list) {
        try{
            list1 = list;
            setUpMap(list);
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }
    private List<LatLng> readLatLngs(List<HashMap<String, String>> list) {
        List<LatLng> points = new ArrayList<LatLng>();
        try {
            for (int i = 0;i < list.size(); i++){
                if (list.get(i).get("latitude")!=null && list.get(i).get("longitude")!=null){
                    LatLng latLng = new LatLng(Double.parseDouble(list.get(i).get("latitude").toString()),Double.parseDouble(list.get(i).get("longitude").toString()));
                    points.add(latLng);
                }
            }
        } catch (Exception e){
            Timber.d(e.getMessage());
        }
        return points;
    }
    private void setUpMap(List<HashMap<String, String>> list) {
        // TODO Auto-generated method stub
        //speed_add_s = (datas.size())/100;
        speed_status = 1100;
        // 加入坐标
        latlngList = (ArrayList<LatLng>) readLatLngs(list);
        for (int i = 0;i < latlngList.size();i++){
            if (i+1<=latlngList.size()-1){
                mileage += AMapUtils.calculateLineDistance(latlngList.get(i),latlngList.get(i+1));
            }
        }
        tv_mileage.setText(String.format("%.2f",Double.valueOf(mileage/1000)));
        if (latlngList.size() >= 1) {
            processbar.setMax(latlngList.size()-1);
            bitmap= BitmapDescriptorFactory.fromResource(R.drawable.start);
            option = new MarkerOptions().position(latlngList.get(0)).icon(bitmap);
            s_marker = (Marker) (aMap.addMarker(option));

            bitmap=BitmapDescriptorFactory.fromResource(R.drawable.end);
            option = new MarkerOptions().position(latlngList.get(latlngList.size() - 1)).icon(bitmap);
            e_marker = (Marker) (aMap.addMarker(option));
            if(latlngList.size() >= 2){
                PolylineOptions polygonOption = new PolylineOptions().width(10)
                        .color(Color.RED).addAll(latlngList);
                aMap.addPolyline(polygonOption);

                LatLngBounds.Builder b = LatLngBounds.builder();
                for (int i = 0; i < latlngList.size(); i++) {
                    b.include(latlngList.get(i));
                }
                LatLngBounds bounds = b.build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
            if (latlngList.size() > 0) {
                // 假如当前已经回放到最后一点 置0
                if (processbar.getProgress() == processbar.getMax()) {
                    processbar.setProgress(0);
                }
                //replayButton.setText(" 停止 ");
            }

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
}
