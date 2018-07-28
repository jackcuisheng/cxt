package com.cxt.gps.model;

import com.cxt.gps.listener.OnMapLocationListener;

public interface NavigateModel {
    void loadData(String msgType, String account, String carNum, OnMapLocationListener onMapLocationListener);
}
