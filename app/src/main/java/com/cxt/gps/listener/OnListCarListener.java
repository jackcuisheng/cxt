package com.cxt.gps.listener;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OnListCarListener {
    void successed(List<HashMap<String, String>> list);
    void failed();
}
