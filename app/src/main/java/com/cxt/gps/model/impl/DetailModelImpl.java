package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.DetailModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.Map;

import timber.log.Timber;

public class DetailModelImpl implements DetailModel {
    private String response;
    @Override
    public void setLoadData(final String msgType, final String account, final String carNum, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getBIndDeviceListRequest(msgType,account,carNum));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getCarDetailData(response);
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
    public void setDefend(final String msgtype, final String account, final String carNum, final String reason, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getSetDefendRequest(msgtype,account,carNum,reason));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getSetDefendResult(response);
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
    public void setControl(final String msgType, final String account, final String carNum, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getQueryRequest(msgType,account,carNum));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getSetDefendResult(response);
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
