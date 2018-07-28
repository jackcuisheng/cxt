package com.cxt.gps.listener;

import com.cxt.gps.entity.DeviceData;

import java.util.List;

public interface OnDeviceDataListener {
    void successed(List<DeviceData> list);
    void failed();
}
