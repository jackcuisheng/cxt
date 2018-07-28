package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface TrackModel {
    void loadData(String msgType, String account, String carNum, String sTime, String eTime, String deviceID, String locationType, OnListCarListener onListCarListener);
}
