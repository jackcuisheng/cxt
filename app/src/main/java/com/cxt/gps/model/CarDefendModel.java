package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.listener.OnListCarListener;

public interface CarDefendModel {
    void loadListData(String msgType, String account, int pageNum, int num, OnListCarListener onListCarListener);

    void cancelDefend(String msgType, String account, String carNum, OnReturnStringListener onReturnStringListener);

    void setControl(String msgType, String account, String carNum, OnReturnStringListener onReturnStringListener);
}
