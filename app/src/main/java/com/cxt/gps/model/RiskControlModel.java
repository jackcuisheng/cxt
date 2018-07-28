package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface RiskControlModel {
    void loadListData(String account, String query,int pageNum, String requestType, OnListCarListener onListCarListener);

    void initData(String msgType, String account, OnListCarListener onListCarListener);

    void query(String msgType, int requestType,String account, String query, OnListCarListener onListCarListener);

}
