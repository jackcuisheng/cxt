package com.cxt.gps.model.impl;


import android.content.Context;
import android.text.TextUtils;

import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.model.DeviceInstallModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;
import com.cxt.gps.util.UploadFile;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class DeviceInstallModelImpl implements DeviceInstallModel{
    private String response;
    @Override
    public void bindData(final String msgType, final String account, final String vin, final String carNum, final OnReturnStringListener onReturnStringListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getBindCarRequest(msgType,account,vin,carNum));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    String result = ParseJsonUtil.getResult(response);
                    if (!TextUtils.isEmpty(result)){
                        onReturnStringListener.successed(result);

                    }else {
                        onReturnStringListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void uploadImage(final Context context, final LinkedList<String> list, final String msgType, final String account, final String vin, final int num, final OnReturnStringListener onReturnStringListener) {
        for (int i = 0;i < list.size();i++){
            final int finalI = i;
            new Thread(){
                public void run(){
                    try{
                        String request = Constants.getUploadImageRequest(msgType,account,vin,num);
                        response = UploadFile.httpPost(context, Constants.GET_DATA_URL + "?rawparam=" + request, list.get(finalI), "");
                        response = response.trim();
                        response = UnicodeUtils.unicode2String(response);
                        String result = ParseJsonUtil.getResult(response);
                        if (!TextUtils.isEmpty(result)){
                            onReturnStringListener.successed(result);

                        }else {
                            onReturnStringListener.failed();
                        }
                    }catch (Exception e){
                        Timber.d(e.getMessage());
                    }
                }
            }.start();
        }

    }
}
