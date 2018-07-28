package com.cxt.gps.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.bluetooth.ComminuteActivity;

public class WhitelistActivity extends SuperActivity{
    private TextView tv_title;
    private ImageView iv_back;
    private Button btn_setting;
    private ListView lv_list;
    private String carNum,vin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        initView();
        init();
        initEvents();
    }
    private void initView(){
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        lv_list = findViewById(R.id.lv_list);
        btn_setting = findViewById(R.id.btn_setting);
    }
    private void init(){
        carNum = getIntent().getStringExtra("carNum");
        vin = getIntent().getStringExtra("vin");
        tv_title.setText(getResources().getString(R.string.whitelist));
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(),MeterSearchActivity.class);
                Intent intent = new Intent(getApplicationContext(),ComminuteActivity.class);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
            }
        });
    }
}
