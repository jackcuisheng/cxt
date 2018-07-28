package com.cxt.gps.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.MainActivity;
import com.cxt.gps.UI.activity.userTab.CommonProblemActivity;
import com.cxt.gps.entity.User;
import com.cxt.gps.presenter.UserLoginPresenter;
import com.cxt.gps.util.SharePreferenceUtil;
import com.cxt.gps.view.IUserLoginView;


public class UserLoginActivity extends SuperActivity implements IUserLoginView {
    private EditText et_username;
    private EditText et_password;
    private TextView btn_login;
    private TextView tv_forget;
    private CheckBox cb_remember_pwd;
    private TextView tv_common_problem;
    private UserLoginPresenter mUserLoginPrestener = new UserLoginPresenter(this);
    private ContentLoadingProgressBar progressbar;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_pwd);
        btn_login = findViewById(R.id.btn_login);
        tv_forget = findViewById(R.id.tv_forget);
        progressbar = findViewById(R.id.id_progressbar);
        cb_remember_pwd = findViewById(R.id.cb_remember_pwd);
        tv_common_problem = findViewById(R.id.tv_common_problem);
    }
    public void initEvent(){
        sharedPreferences=getSharedPreferences("remember_password", Context.MODE_PRIVATE);
        boolean isRemember=sharedPreferences.getBoolean("remember_password",false);
        if(isRemember){
            String name=sharedPreferences.getString("username","");
            String password=sharedPreferences.getString("password","");
            et_username.setText(name);
            et_password.setText(password);
            cb_remember_pwd.setChecked(true);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              presenter控制逻辑 登录操作
                mUserLoginPrestener.login();
            }
        });
        tv_common_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CommonProblemActivity.class);
            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPasswordActivity.class);
            }
        });
    }

    @Override
    public String getUserName() {
        return et_username.getText().toString();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString();
    }

    @Override
    public void toMainActivity(User user) {
       showCustomToast(this,et_username.getText().toString()+"登录成功");
        editor=sharedPreferences.edit();
        if(cb_remember_pwd.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("username",user.getUsername());
            editor.putString("password",user.getPassword());
        }
        else {
            editor.clear();
        }
        editor.commit();
        SharePreferenceUtil util_pre = new SharePreferenceUtil(this,"first_pref");
        util_pre.setIsFirst(false);
        util_pre.setUserInfo("account",user.getAccount());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showFailedError() {
        showCustomToast(this,"登录失败,用户名或密码错误!");
    }

    @Override
    public void showLoading() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressbar.setVisibility(View.INVISIBLE);
    }
}

