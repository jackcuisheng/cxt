package com.cxt.gps.model;

import com.cxt.gps.listener.OnDeviceDataListener;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.listener.OnListCarListener;

public interface SearchCarModel {
    void getAllCarList(String query, String account, OnListCarListener onListCarListener);
    void getLastLocationData(String carNum,String account,OnMapLocationListener onMapLocationListener);
    void getCarDetail(String msgType, String account, String carNum, OnDeviceDataListener onDeviceDataListener);
}
