package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.ResidentPointPresenter;
import com.cxt.gps.view.IResidentPointView;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class ResidentPointActivity extends SuperActivity implements IResidentPointView, View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_week;
    private TextView tv_month;
    private TextView tv_season;
    private RadioGroup rg_group;
    private RadioButton rb_sate;
    private RadioButton rb_base;
    private RadioButton rb_wifi;
    private TextView tv_notice;
    private ListView lv_list;
    private SharedPreferences sharedPreferences;
    private String account;
    private ResidentPointPresenter presenter = new ResidentPointPresenter(this);
    private String timeType = "week";
    private String locationType = "1";
    private SimpleAdapter adapter;
    private String carNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_point);
        initView();
        init();
        initEvents();
    }
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        tv_week = findViewById(R.id.tv_week);
        tv_month = findViewById(R.id.tv_month);
        tv_season = findViewById(R.id.tv_season);
        rg_group = findViewById(R.id.rg_group);
        rb_sate = findViewById(R.id.rb_sate);
        rb_base = findViewById(R.id.rb_base);
        rb_wifi = findViewById(R.id.rb_wifi);
        tv_notice = findViewById(R.id.tv_notice);
        lv_list = findViewById(R.id.lv_list);
    }
    public void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        carNum = getIntent().getStringExtra("carNum");
        tv_title.setText(carNum);
        account = sharedPreferences.getString("account","");
        tv_week.setBackgroundColor(getResources().getColor(R.color.top_layout));
        tv_week.setTextColor(getResources().getColor(R.color.white));
        presenter.loadData("REQUEST_CZDLIST",account,carNum,timeType,locationType);
    }
    public void initEvents(){
        iv_back.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_season.setOnClickListener(this);
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sate:
                        locationType = "1";
                        presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                        break;
                    case R.id.rb_base:
                        locationType = "0";
                        presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                        break;
                    case R.id.rb_wifi:
                        locationType = "2";
                        presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                        break;
                }
            }
        });

    }
    @Override
    public void toMainActivity(final List<HashMap<String, String>> list) {
        try{
            if (list.size()==1 && list.get(0).get("count").equals("0")){
                tv_notice.setVisibility(View.VISIBLE);
                lv_list.setVisibility(View.GONE);
            }else {
                tv_notice.setVisibility(View.GONE);
                lv_list.setVisibility(View.VISIBLE);
                adapter = new SimpleAdapter(this, list, R.layout.resident_point_list_item, new String[]{"lastLocation", "count"}, new int[]{R.id.tv_location, R.id.tv_count});
                lv_list.setAdapter(adapter);
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),ResidentPointItemActivity.class);
                        intent.putExtra("carNum",carNum);
                        intent.putExtra("count",list.get(position).get("count"));
                        intent.putExtra("latitude",list.get(position).get("latitude"));
                        intent.putExtra("longitude",list.get(position).get("longitude"));
                        startActivity(intent);
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
                presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                break;
            case R.id.tv_month:
                resetBackground();
                tv_month.setTextColor(getResources().getColor(R.color.white));
                tv_month.setBackgroundColor(getResources().getColor(R.color.top_layout));
                timeType = "month";
                presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                break;
            case R.id.tv_season:
                resetBackground();
                tv_season.setTextColor(getResources().getColor(R.color.white));
                tv_season.setBackgroundColor(getResources().getColor(R.color.top_layout));
                timeType = "season";
                presenter.loadData("REQUEST_CZDLIST",account,getIntent().getStringExtra("carNum"),timeType,locationType);
                break;
        }
    }
    public void resetBackground(){
        tv_week.setBackground(getResources().getDrawable(R.drawable.bg_blue));
        tv_month.setBackground(getResources().getDrawable(R.drawable.view_no_left_border));
        tv_season.setBackground(getResources().getDrawable(R.drawable.view_no_left_border));
        tv_week.setTextColor(getResources().getColor(R.color.top_layout));
        tv_month.setTextColor(getResources().getColor(R.color.top_layout));
        tv_season.setTextColor(getResources().getColor(R.color.top_layout));
    }
}
