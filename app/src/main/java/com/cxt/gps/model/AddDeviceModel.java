package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.listener.OnReturnStringListener;

public interface AddDeviceModel {
    void checkDeviceState(String msgType, String account, String deviceID, OnReturnMapListener onReturnMapListener);

    void bindDevice(String msgType, String account, String vin, String carNum, String deviceID, OnReturnMapListener onReturnMapListener);
}
