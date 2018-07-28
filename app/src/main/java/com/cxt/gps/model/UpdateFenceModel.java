package com.cxt.gps.model;

import com.cxt.gps.listener.OnReturnMapListener;

public interface UpdateFenceModel {
    void editFence(String msgType, String account, String fenceId, String radius, String fenceName, int condition, OnReturnMapListener onReturnMapListener);
}
