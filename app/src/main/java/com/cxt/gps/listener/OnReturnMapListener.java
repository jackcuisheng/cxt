package com.cxt.gps.listener;

import java.util.Map;

public interface OnReturnMapListener {
    void successed(Map<String, String>map);
    void failed();
}
