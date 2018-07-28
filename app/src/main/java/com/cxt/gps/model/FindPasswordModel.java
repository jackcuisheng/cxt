package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnMapListener;

public interface FindPasswordModel {
    void findPassword(String msgType, String username, String code, String password, OnReturnMapListener onReturnMapListener);
}
