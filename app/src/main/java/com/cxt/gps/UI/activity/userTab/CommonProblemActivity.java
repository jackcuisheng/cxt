package com.cxt.gps.UI.activity.userTab;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.SuperActivity;

public class CommonProblemActivity extends SuperActivity {
    private ImageView iv_back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        initView();
        init();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
    }
    private void init(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(getResources().getString(R.string.common_problem));
    }
}
