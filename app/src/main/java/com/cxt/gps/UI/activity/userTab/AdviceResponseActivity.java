package com.cxt.gps.UI.activity.userTab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.SuperActivity;

public class AdviceResponseActivity extends SuperActivity{
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_content;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_response);
        initView();
        init();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        btn_submit = findViewById(R.id.btn_submit);
        et_content = findViewById(R.id.et_content);
    }
    private void init(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(getResources().getString(R.string.advice_response));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomToast(getApplicationContext(),getResources().getString(R.string.success_advice));
            }
        });
    }
}
