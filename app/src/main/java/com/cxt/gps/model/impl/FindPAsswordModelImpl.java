package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FindPasswordModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.Map;

import timber.log.Timber;

public class FindPAsswordModelImpl implements FindPasswordModel {
    private String response;
    @Override
    public void findPassword(final String msgType, final String username, final String code, final String password, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getFindPasswrodRequest(msgType,username,code,password));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String,String> map = ParseJsonUtil.getFindPasswordResult(response);
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
