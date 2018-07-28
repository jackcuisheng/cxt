package com.cxt.gps.model.impl;

import com.cxt.gps.entity.GpsData;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.model.NavigateModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import timber.log.Timber;

public class NavigateModelImpl implements NavigateModel {
    @Override
    public void loadData(final String msgType, final String account, final String carNum, final OnMapLocationListener onMapLocationListener) {
        new Thread(){
            public void run(){
                try {
                    String response = GetPostUtil.sendPost(Constants.getQueryRequest(msgType,account,carNum));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    GpsData data = ParseJsonUtil.getLastLocation(response);
                    if (data != null){
                        onMapLocationListener.successed(data);
                    }else {
                        onMapLocationListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }

            }
        }.start();
    }
}
