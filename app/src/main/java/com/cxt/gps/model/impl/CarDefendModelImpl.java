package com.cxt.gps.model.impl;

import android.text.TextUtils;

import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.CarDefendModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class CarDefendModelImpl implements CarDefendModel{
    private String response;
    private List<HashMap<String,String>>list;
    @Override
    public void loadListData(final String msgType, final String account, final int pageNum, final int num, final OnListCarListener onListCarListener) {
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

    @Override
    public void cancelDefend(final String msgType, final String account, final String carNum, final OnReturnStringListener onReturnStringListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getQueryRequest(msgType,account,carNum));
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
    public void setControl(final String msgType, final String account, final String carNum, final OnReturnStringListener onReturnStringListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getQueryRequest(msgType,account,carNum));
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
