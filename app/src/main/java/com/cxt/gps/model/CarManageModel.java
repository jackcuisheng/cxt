package com.cxt.gps.model;


import com.cxt.gps.listener.OnListCarListener;

public interface CarManageModel{

    void getAllCarData(String account,int pageNum,String query,OnListCarListener onListCarListener);

    void getRiskCarData(String msgType, String account, int pageNum, int num, OnListCarListener onListCarListener);
}
