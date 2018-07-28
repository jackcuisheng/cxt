package com.cxt.gps.model.impl;



import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.CarManageModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.List;

import timber.log.Timber;


public class CarManageModelImpl implements CarManageModel {
    private String response;
    private List list;

    @Override
    public void getAllCarData(final String account, final int pageNum, final String query, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getCarRequest("REQUEST_ALL_CARS_INFO",account,pageNum,query));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    list = ParseJsonUtil.getCarData(response);
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

    @Override
    public void getRiskCarData(final String msgType, final String account, final int pageNum, final int num, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getRiskCarRequest(msgType,account,pageNum,num));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    list = ParseJsonUtil.getRiskCarData(response);
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
