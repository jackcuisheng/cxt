package com.cxt.gps.model.impl;

import android.text.TextUtils;

import com.cxt.gps.entity.DeviceData;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.listener.OnDeviceDataListener;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.SearchCarModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class SearchCarModelImpl implements SearchCarModel{
    private String response;

    private List<HashMap<String,String>>  list;
    @Override
    public void getAllCarList(final String query, final String account, final OnListCarListener onListCarListener) {
        new Thread() {
            @Override
            public void run() {
                try{
                    response = GetPostUtil.sendPost(Constants.getCarRequest("REQUEST_ALL_CARS_INFO",account,1,query));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    list = ParseJsonUtil.getCarList(response);
                    if (list != null ){
                        if (TextUtils.isEmpty(list.get(0).get("carNum"))){
                            onListCarListener.failed();
                        }else {
                            onListCarListener.successed(list);
                        }

                    }else {
                        onListCarListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void getLastLocationData(final String carNum, final String account, final OnMapLocationListener onMapLocationListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getQueryRequest("REQUEST_LAST_LOCATION",account,carNum));
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

    @Override
    public void getCarDetail(final String msgType, final String account, final String carNum, final OnDeviceDataListener onDeviceDataListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getQueryRequest(msgType,account,carNum));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<DeviceData> list = ParseJsonUtil.getDeviceData(response);
                    if (list != null){
                        onDeviceDataListener.successed(list);

                    }else {
                        onDeviceDataListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }



}
