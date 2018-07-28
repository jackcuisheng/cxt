package com.cxt.gps.UI.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cxt.gps.R;

public class HundredMetersSearchActivity extends SuperActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private SearchView sv_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hundred_meters_search);
        initView();
        init();
        initEvents();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        sv_search = findViewById(R.id.sv_query);
    }
    private void init(){
        tv_title.setText(getResources().getString(R.string.hundred_meters));
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
