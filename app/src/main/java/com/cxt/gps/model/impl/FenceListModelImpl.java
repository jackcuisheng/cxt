package com.cxt.gps.model.impl;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FenceListModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class FenceListModelImpl implements FenceListModel {
    private String response;
    @Override
    public void loadFenceList(final String msgType, final String account, final String carNum, final String searchValue, final String pageNum, final OnListCarListener onListCarListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getFenceListRequest(msgType,account,carNum,searchValue,pageNum);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    List<HashMap<String,String>> list = ParseJsonUtil.getFenceListData(response);
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
    public void removeFence(final String msgType, final String account, final String fenceID, final OnReturnMapListener onReturnMapListener) {
        new Thread(){
            public void run(){
                try{
                    String request = Constants.getRemoveFenceRequest(msgType,account,fenceID);
                    response = GetPostUtil.sendPost(request);
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    Map<String ,String>map = ParseJsonUtil.getEditFenceResult(response);
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
