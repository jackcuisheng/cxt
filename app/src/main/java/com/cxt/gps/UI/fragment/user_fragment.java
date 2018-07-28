package com.cxt.gps.UI.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.UserLoginActivity;
import com.cxt.gps.UI.activity.userTab.About_cxt_Activity;
import com.cxt.gps.UI.activity.userTab.AdviceResponseActivity;
import com.cxt.gps.UI.activity.userTab.CommonProblemActivity;
import com.cxt.gps.UI.activity.userTab.SettingActivity;
import com.cxt.gps.UI.activity.userTab.UpdatePasswordActivity;
import com.cxt.gps.util.SharePreferenceUtil;

public class user_fragment extends SuperFragment implements View.OnClickListener{
    private LinearLayout ll_about_cxt;
    private LinearLayout ll_common_problem;
    private LinearLayout ll_advice_response;
    private LinearLayout ll_check_update;
    private LinearLayout ll_setting;
    private LinearLayout ll_update_pwd;
    private RelativeLayout rl_logout;
    private SharedPreferences sharedPreferences;
    private TextView tv_username;
    private Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initViews() {
        ll_about_cxt = mView.findViewById(R.id.ll_about_cxt);
        ll_common_problem = mView.findViewById(R.id.ll_common_problem);
        ll_advice_response = mView.findViewById(R.id.ll_advice_response);
        ll_check_update = mView.findViewById(R.id.ll_check_update);
        ll_setting = mView.findViewById(R.id.ll_setting);
        ll_update_pwd = mView.findViewById(R.id.ll_update_pwd);
        rl_logout = mView.findViewById(R.id.rl_logout);
        tv_username = mView.findViewById(R.id.tv_username);
    }

    @Override
    protected void initEvents() {
        tv_username.setText(sharedPreferences.getString("username",""));
        ll_about_cxt.setOnClickListener(this);
        ll_common_problem.setOnClickListener(this);
        ll_advice_response.setOnClickListener(this);
        ll_check_update.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_update_pwd.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
    }

    @Override
    protected void init() {
        sharedPreferences = getActivity().getSharedPreferences("remember_password",Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_about_cxt:
                intent = new Intent(getContext(), About_cxt_Activity.class);
                startActivity(intent);
                break;
            case R.id.ll_common_problem:
                intent = new Intent(getContext(), CommonProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_advice_response:
                intent = new Intent(getContext(), AdviceResponseActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_check_update:
                showCustomToast(getContext(),"当前已是最新版本！");
                break;
            case R.id.ll_setting:
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_update_pwd:
                intent = new Intent(getContext(), UpdatePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_logout:
                new AlertDialog.Builder(getActivity())
                        .setMessage("确定要退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharePreferenceUtil util = new SharePreferenceUtil(getActivity(),"first_pref");
                                util.removeUserInfo("account");
                                util.removeUserInfo("username");
                                util.removeUserInfo("password");
                                sharedPreferences = getActivity().getSharedPreferences("remember_password", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("username");
                                editor.remove("password");
                                editor.commit();
                                Intent intent = new Intent(getActivity().getApplicationContext(),UserLoginActivity.class);
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                break;
        }
    }
}
