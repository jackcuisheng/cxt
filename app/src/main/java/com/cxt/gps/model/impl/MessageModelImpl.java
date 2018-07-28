package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.MessageModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class MessageModelImpl implements MessageModel{
    private String response;
    @Override
    public void loadDataList(final String account, final String requestType, final int pageNum, final int num, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getAlarmMessageRequest(account,requestType,pageNum,num);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>> list = ParseJsonUtil.getAlarmMessageList(response);
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
    public void query(final String msgType, final String account, final String query, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getQueryAlarmMessageRequest(msgType,account,query);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>>list = ParseJsonUtil.getAlarmMessageList(response);
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
