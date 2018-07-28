package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.RiskControlModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class RiskControlModelImpl implements RiskControlModel{
    private String response;
    @Override
    public void loadListData(final String account, final String query, final int pageNum, final String requestType, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getRiskListDataRequest(account,requestType,pageNum);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>>list = ParseJsonUtil.getRiskControl(response);
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
    public void initData(final String msgType, final String account, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getRiskNumRequest(msgType,account));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>>list = ParseJsonUtil.getRiskControlNum(response);
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
    public void query(final String msgType, final int requestType, final String account, final String query, final OnListCarListener onListCarListener) {
        new Thread(){
                public void run(){
                    try{
                        String request = Constants.getRiskCarQueryRequest(msgType,requestType,account,1,query);
                        response = GetPostUtil.sendPost(request);
                        response = response.trim();
                        response = UnicodeUtils.unicode2String(response);
                        List<HashMap<String,String>>list = ParseJsonUtil.getRiskControlList(response);
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
