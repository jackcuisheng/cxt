package com.cxt.gps.UI.activity.userTab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.SuperActivity;
import com.cxt.gps.UI.activity.UserLoginActivity;
import com.cxt.gps.presenter.UpdatePasswordPresenter;
import com.cxt.gps.view.IUpdateFenceView;
import com.cxt.gps.view.IUpdatePasswordView;

import java.util.Map;

import timber.log.Timber;

public class UpdatePasswordActivity extends SuperActivity implements IUpdatePasswordView{
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_old;
    private EditText et_new;
    private EditText et_confirm;
    private Button btn_submit;
    private String old_pwd,new_pwd,confirm_pwd;
    private String pattern;
    private boolean flag = false;
    private boolean flag1 = false;
    private UpdatePasswordPresenter presenter = new UpdatePasswordPresenter(this);
    private SharedPreferences sharedPreferences;
    private String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        initView();
        init();
        initEvents();
    }
    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        et_old = findViewById(R.id.et_old_pwd);
        et_new = findViewById(R.id.et_new_pwd);
        et_confirm = findViewById(R.id.et_confirm_pwd);
        btn_submit = findViewById(R.id.btn_submit);
        tv_title = findViewById(R.id.tv_title);
    }
    private void init(){
        sharedPreferences = getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        tv_title.setText(getResources().getString(R.string.update_pwd));
        pattern =  "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
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
                    new_pwd = et_new.getText().toString().trim();
                    if(new_pwd.matches(pattern)){
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
                    if (flag) {
                        confirm_pwd = et_confirm.getText().toString().trim();
                        if (new_pwd.equals(confirm_pwd)) {
                            flag1 = true;
                        } else {
                            flag1 = false;
                            showCustomToast(getApplicationContext(), "两次密码输入不一致！");
                        }
                    }else {
                        showCustomToast(getApplicationContext(),"密码格式不正确！");
                    }
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_confirm.clearFocus();
                et_new.clearFocus();
                et_old.clearFocus();
                if (flag && flag1){
                    old_pwd = et_old.getText().toString().trim();
                    new_pwd = et_new.getText().toString().trim();
                    presenter.updatePassword("REQUEST_CHANGE_PASSWORD",account,old_pwd,new_pwd);
                }else {
                    showCustomToast(getApplicationContext(),"密码输入有误！");
                }
            }
        });
    }

    @Override
    public void toMainActivity(Map<String, String> map) {
        try {
            if (map.get("state").equals("1")){
                showCustomToast(this,"密码修改成功，请重新登录账户！");
                sharedPreferences.edit().remove("password");
                startActivity(UserLoginActivity.class);
                finish();
            }else {
                showCustomToast(this,"密码失败，请稍后重试！");
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
