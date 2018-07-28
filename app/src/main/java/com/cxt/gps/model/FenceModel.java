package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.listener.OnReturnMapListener;

public interface FenceModel {
    void loadDeviceData(String msgType, String account, String carNum, OnMapLocationListener onMapLocationListener);

    void saveFence(String msgType,String account,String carNum,String fenceName,String address,double radius,String fenceType,double latitude,double longitude,int condition, OnReturnMapListener onReturnMapListener);

    void deleteFence(String msgType, String account, String fenceID, OnReturnMapListener onReturnMapListener);

    void loadFenceList(String msgType, String account, String carNum, String searchValue, String pageNum, OnListCarListener onListCarListener);
}
