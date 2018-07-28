package com.cxt.gps.listener;

import com.cxt.gps.entity.GpsData;


public interface OnMapLocationListener {
    void successed(GpsData data);
    void failed();
}
