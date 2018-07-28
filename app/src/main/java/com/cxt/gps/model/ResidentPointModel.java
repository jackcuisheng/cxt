package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface ResidentPointModel {
    void loadData(String msgType, String account, String carNum, String timeType, String loactionType, OnListCarListener onListCarListener);
}
