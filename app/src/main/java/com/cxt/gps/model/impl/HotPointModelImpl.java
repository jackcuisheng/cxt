package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.HotPointModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class HotPointModelImpl implements HotPointModel{
    private String response;
    @Override
    public void loadData(final String msgType, final String account, final String carNum, final String sTime, final String eTime, final String timeType, final String locationType, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getHotPointRequest(msgType,account,carNum,sTime,eTime,timeType,locationType);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>> list = ParseJsonUtil.getResidentPointData(response);
                    if (list != null){
                        if (list.size() == 1 && list.get(0).get("count").equals("0")){
                            onListCarListener.failed();
                        }else{
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
}
