package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface HotPointModel {
    void loadData(String msgType, String account, String carNum, String sTime, String eTime, String timeType, String locationType, OnListCarListener onListCarListener);
}
