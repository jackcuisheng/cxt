package com.cxt.gps.model.impl;

import android.text.TextUtils;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.model.AddDeviceModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.Map;

import timber.log.Timber;

public class AddDeviceModelImpl implements AddDeviceModel {
    private String response;
    @Override
    public void checkDeviceState(final String msgType, final String account, final String deviceID, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getCheckDeviceRequest(msgType,account,deviceID));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getDeviceState(response);
                    if (map != null){
                        onReturnMapListener.successed(map);

                    }else {
                        onReturnMapListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void bindDevice(final String msgType, final String account, final String vin, final String carNum, final String deviceID, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getBIndDeviceRequest(msgType,account,vin,carNum,deviceID));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getBindDeviceResult(response);
                    if (map != null){
                        onReturnMapListener.successed(map);

                    }else {
                        onReturnMapListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();

    }
}
