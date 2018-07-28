package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.TrackModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class TrackModelImpl implements TrackModel {
    private String response;
    @Override
    public void loadData(final String msgType, final String account, final String carNum, final String sTime, final String eTime, final String deviceID, final String locationType, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getTrackRequest(msgType,account,carNum,sTime,eTime,deviceID,locationType));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>> list = ParseJsonUtil.getTrackData(response);
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
