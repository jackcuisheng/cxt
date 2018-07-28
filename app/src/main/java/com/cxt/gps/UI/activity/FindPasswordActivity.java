package com.cxt.gps.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.presenter.FindPasswordPresenter;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.view.IFindPasswordView;

import java.util.Map;

import timber.log.Timber;

public class FindPasswordActivity extends SuperActivity implements IFindPasswordView{
    private TextView tv_title;
    private ImageView iv_back;
    private EditText et_new;
    private EditText et_confirm;
    private EditText et_code;
    private Button btn_code;
    private Button btn_submit;
    private String username,phone,pattern;
    private String password,confirm,code;
    private FindPasswordPresenter presenter = new FindPasswordPresenter(this);
    private boolean flag = false;
    private boolean flag2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        initView();
        init();
        initEvents();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        et_new = findViewById(R.id.et_new);
        et_confirm = findViewById(R.id.et_confirm);
        et_code = findViewById(R.id.et_code);
        btn_code = findViewById(R.id.btn_code);
        btn_submit = findViewById(R.id.btn_submit);
    }
    private void init(){
        tv_title.setText(getResources().getString(R.string.find_password));
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        pattern = "[a-zA-Z0-9]{6,13}";
    }
    private void initEvents(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_new.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){
                    password = et_new.getText().toString().trim();
                    if (password.matches(pattern)){
                        flag = true;
                    }else {
                        flag = false;
                        showCustomToast(getApplicationContext(),"密码格式不正确！");
                    }
                }
            }
        });
        et_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    confirm = et_confirm.getText().toString().trim();
                    if (flag) {
                        if (confirm.equals(password)) {
                            flag2 = true;
                        }else {
                            flag2 = false;
                            showCustomToast(getApplicationContext(),"两次密码输入不一致！");
                        }
                    }else {
                        showCustomToast(getApplicationContext(),"密码格式不正确！");
                    }
                }
            }
        });
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        String response = GetPostUtil.sendPost(Constants.GET_MESSAGE+"mobile="+phone+"&userName="+username);
                    }
                }.start();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_new.clearFocus();
                et_confirm.clearFocus();
                if (flag && flag2){
                    password = et_confirm.getText().toString().trim();
                    code = et_code.getText().toString().trim();
                    if (code.isEmpty()){
                        showCustomToast(getApplicationContext(),"验证码不能为空！");
                    }else {
                        presenter.findPassword("REQUEST_REPLACE_PASSWORD",username,code,password);
                    }
                }
            }
        });
    }

    @Override
    public void toMainActivity(Map<String, String> map) {
        try{
            if (map.get("state").equals("1")){
                showCustomToast(this,"密码找回成功，请用新的账户密码登录！");
                startActivity(UserLoginActivity.class);
                finish();
            }else {
                showCustomToast(this,"密码失败，请重新尝试找回密码！");
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

    }

    @Override
    public void hideLoading() {

    }
}
