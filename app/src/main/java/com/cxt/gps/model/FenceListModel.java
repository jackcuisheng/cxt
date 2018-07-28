package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnReturnMapListener;

public interface FenceListModel {
    void loadFenceList(String msgType, String account, String carNum, String searchValue, String pageNum, OnListCarListener onListCarListener);

    void removeFence(String msgType, String account, String fenceID, OnReturnMapListener onReturnMapListener);
}
