package com.cxt.gps.model;

import com.cxt.gps.listener.OnListCarListener;

public interface MessageModel {
    void loadDataList(String account, String requestType, int pageNum, int num, OnListCarListener onListCarListener);

    void query(String msgType, String account, String query ,OnListCarListener onListCarListener);
}
