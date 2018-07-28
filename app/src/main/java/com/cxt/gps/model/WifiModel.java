package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface WifiModel {
    void loadInItData(String msgType, String account, String carNum, OnListCarListener onListCarListener);
}
