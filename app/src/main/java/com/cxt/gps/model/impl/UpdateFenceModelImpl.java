package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.UpdateFenceModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.Map;

import timber.log.Timber;

public class UpdateFenceModelImpl implements UpdateFenceModel {
    private String response;
    @Override
    public void editFence(final String msgType, final String account, final String fenceId, final String radius, final String fenceName, final int condition, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getEditFenceRequest(msgType,account,fenceId,radius,fenceName,condition);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getEditFenceResult(response);
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
