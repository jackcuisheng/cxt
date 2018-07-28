package com.cxt.gps.UI.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cxt.gps.R;
import com.cxt.gps.UI.activity.CarControlActivity;
import com.cxt.gps.UI.activity.CarDefendActivity;
import com.cxt.gps.UI.activity.CarManageActivity;
import com.cxt.gps.UI.activity.DeviceInstallActivity;
import com.cxt.gps.UI.activity.HundredMetersSearchActivity;
import com.cxt.gps.UI.activity.RiskControlActivity;
import com.cxt.gps.model.DeviceInstallModel;

public class home_fragment extends SuperFragment implements View.OnClickListener{
    private LinearLayout ll_car_manage;
    private LinearLayout ll_car_defend;
    private LinearLayout ll_risk_control;
    private LinearLayout ll_collect_car;
    private LinearLayout ll_device_install;
    private LinearLayout ll_meter_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initViews() {
        ll_car_manage = mView.findViewById(R.id.ll_car_manage);
        ll_car_defend = mView.findViewById(R.id.ll_car_defend);
        ll_risk_control = mView.findViewById(R.id.ll_risk_control);
        ll_collect_car = mView.findViewById(R.id.ll_collect_car);
        ll_device_install = mView.findViewById(R.id.ll_device_install);
        ll_meter_search = mView.findViewById(R.id.ll_meter_search);
    }

    @Override
    protected void initEvents() {
        ll_car_manage.setOnClickListener(this);
        ll_car_defend.setOnClickListener(this);
        ll_risk_control.setOnClickListener(this);
        ll_collect_car.setOnClickListener(this);
        ll_device_install.setOnClickListener(this);
        ll_meter_search.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_car_manage:
                startActivity(this, CarManageActivity.class);
                break;
            case R.id.ll_car_defend:
                startActivity(this, CarDefendActivity.class);
                break;
            case R.id.ll_risk_control:
                startActivity(this, RiskControlActivity.class);
                break;
            case R.id.ll_collect_car:
                startActivity(this, CarControlActivity.class);
                break;
            case R.id.ll_device_install:
                startActivity(this, DeviceInstallActivity.class);
                break;
            case R.id.ll_meter_search:
                startActivity(this,HundredMetersSearchActivity.class);
                break;
        }
    }
}
