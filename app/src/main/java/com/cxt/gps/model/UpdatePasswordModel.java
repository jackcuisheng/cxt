package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnMapListener;

public interface UpdatePasswordModel {
    void updatePassword(String msgType, String account, String oldPassword, String newPassword, OnReturnMapListener onReturnMapListener);
}
