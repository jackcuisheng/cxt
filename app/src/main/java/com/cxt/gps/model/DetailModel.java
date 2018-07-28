package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnMapListener;

public interface DetailModel {
    void setLoadData(String msgType, String account, String carNum, OnReturnMapListener onReturnMapListener);

    void setDefend(String msgtype, String account, String carNum, String reason, OnReturnMapListener onReturnMapListener);

    void setControl(String msgType, String account, String carNum, OnReturnMapListener onReturnMapListener);
}
