package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cxt.gps.R;
import com.cxt.gps.presenter.DetailPresenter;
import com.cxt.gps.util.BitmapCache;
import com.cxt.gps.view.IDetailView;

import java.util.Map;

import timber.log.Timber;

public class DetailActivity extends SuperActivity implements IDetailView ,View.OnClickListener{
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_vin;
    private TextView tv_carNum;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_money;
    private TextView tv_months;
    private TextView tv_repay;
    private TextView tv_location;
    private TextView tv_alarm_state;
    private CheckBox cb_defend;
    private CheckBox cb_control;
    private NetworkImageView iv_image1;
    private NetworkImageView iv_image2;
    private Button btn_defend;
    private Button btn_control;
    private Button btn_track;
    private Button btn_search;
    private SharedPreferences sharedPreferences;
    private String account;
    private String carNum;
    private String vin;
    private DetailPresenter presenter = new DetailPresenter(this);
    private NetworkImageView expandedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_vin = findViewById(R.id.tv_vin);
        tv_carNum = findViewById(R.id.tv_carNum);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phoneNum);
        tv_money = findViewById(R.id.tv_loanMoney);
        tv_months = findViewById(R.id.tv_loanMonth);
        tv_repay = findViewById(R.id.tv_repayMonth);
        tv_location = findViewById(R.id.tv_location);
        tv_alarm_state = findViewById(R.id.tv_alarm_state);
        cb_defend = findViewById(R.id.cb_defend);
        cb_control = findViewById(R.id.cb_control);
        iv_image1 = findViewById(R.id.iv_image1);
        iv_image2 = findViewById(R.id.iv_image2);
        btn_defend = findViewById(R.id.btn_defend);
        btn_control = findViewById(R.id.btn_control);
        btn_track = findViewById(R.id.btn_track);
        btn_search = findViewById(R.id.btn_search);
        expandedImageView = findViewById(R.id.expanded_image);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref",Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        carNum = getIntent().getStringExtra("carNum");
        vin = getIntent().getStringExtra("vin");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(getResources().getString(R.string.car_info));
        tv_vin.setText(vin);
        tv_carNum.setText(carNum);
        presenter.setLoadData("REQUEST_SINGLE_CAR_INFO",account,carNum);
    }
    public void initEvents(){
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedImageView.setVisibility(View.INVISIBLE);
            }
        });
        // 系统默认的短动画执行时间 200
        btn_control.setOnClickListener(this);
        btn_defend.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_track.setOnClickListener(this);
    }

    @Override
    public void toMainActivity(Map<String, String> map) {
        try{
            tv_name.setText(map.get("name"));
            tv_phone.setText(map.get("phone"));
            tv_money.setText(map.get("money"));
            tv_months.setText(map.get("months"));
            tv_repay.setText(map.get("repay"));
            tv_location.setText(map.get("location"));
            tv_alarm_state.setText(map.get("alarmState"));
            final String image1_url = map.get("image1");
            final String image2_url = map.get("image2");
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //实例化ImageLoader
            final ImageLoader loader = new ImageLoader(requestQueue, new BitmapCache());
            //设置默认图片
            iv_image1.setDefaultImageResId(R.mipmap.load);
            //设置错误图片
            iv_image1.setErrorImageResId(R.mipmap.load);
            //设置图片url和ImageLoader
            iv_image1.setImageUrl(image1_url,loader);
            //设置默认图片
            iv_image2.setDefaultImageResId(R.mipmap.load);
            //设置错误图片
            iv_image2.setErrorImageResId(R.mipmap.load);
            //设置图片url和ImageLoader
            iv_image2.setImageUrl(image2_url,loader);
            iv_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandedImageView.setVisibility(View.VISIBLE);
                    expandedImageView.setDefaultImageResId(R.mipmap.load);
                    //设置错误图片
                    expandedImageView.setErrorImageResId(R.mipmap.load);
                    //设置图片url和ImageLoader
                    expandedImageView.setImageUrl(image1_url,loader);

                }
            });
            iv_image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandedImageView.setVisibility(View.VISIBLE);
                    expandedImageView.setDefaultImageResId(R.mipmap.load);
                    //设置错误图片
                    expandedImageView.setErrorImageResId(R.mipmap.load);
                    //设置图片url和ImageLoader
                    expandedImageView.setImageUrl(image2_url,loader);
                }
            });
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void showFailedError() {

    }

    @Override
    public void showDenfendResult(Map<String, String> map) {
        try{
            if (map.get("result").equals("1")){
                showCustomToast(this,"设防成功！");
                startActivity(CarDefendActivity.class);
            }else if (map.get("result").equals("2")){
                showCustomToast(this,"该车已设防！");
            }else if (map.get("result").equals("0")){
                showCustomToast(this,"查无此车辆！");
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void shoControlResult(Map<String, String> map) {
        try{
            if (map.get("result").equals("1")){
                showCustomToast(this,"布控成功！");
                startActivity(CarControlActivity.class);
            }else if (map.get("result").equals("2")){
                showCustomToast(this,"该车已布控！");
            }else if (map.get("result").equals("0")){
                showCustomToast(this,"查无此车辆！");
            }
        }catch (Exception e){
            Timber.d(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_defend:
                presenter.setDefend("REQUEST_CAR_SET_DEFEND",account,carNum,"");
                break;
            case R.id.btn_control:
                presenter.setControl("REQUEST_SET_CAR_CONTROL",account,carNum);
                break;
            case R.id.btn_track:
                intent = new Intent(this,TrackActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
                break;
            case R.id.btn_search:
                intent = new Intent(this,MapActivity.class);
                intent.putExtra("carNum",carNum);
                startActivity(intent);
                break;
        }
    }
}
