package com.cxt.gps.model;

import android.content.Context;

import com.cxt.gps.listener.OnReturnStringListener;

import java.util.LinkedList;

public interface DeviceInstallModel {
    void bindData(String msgType, String account, String vin, String carNum, OnReturnStringListener onReturnStringListener);

    void uploadImage(Context context, LinkedList<String> list,String msgType, String account, String vin, int num, OnReturnStringListener onReturnStringListener);
}
