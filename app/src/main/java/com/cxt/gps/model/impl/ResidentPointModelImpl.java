package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.ResidentPointModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class ResidentPointModelImpl implements ResidentPointModel {
    private String response;
    @Override
    public void loadData(final String msgType, final String account, final String carNum, final String timeType, final String loactionType, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getResidentPointRequest(msgType,account,carNum,timeType,loactionType));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>> list = ParseJsonUtil.getResidentPointData(response);
                    if (list != null){
                        onListCarListener.successed(list);

                    }else {
                        onListCarListener.failed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }
}
