package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnReturnStringListener;

public interface CarControlModel {
    void loadListData(String msgType, String account, int pageNum, int num, OnListCarListener onListCarListener);

    void cancelControl(String msgType, String account, String carNum, OnReturnStringListener onReturnStringListener);
}
