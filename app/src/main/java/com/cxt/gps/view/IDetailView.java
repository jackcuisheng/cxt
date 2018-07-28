package com.cxt.gps.view;

import java.util.Map;

public interface IDetailView {
    /**
     * View层登录成功的方法
     * @param map
     */
    void toMainActivity(Map<String, String> map);

    /**
     * View层显示登录失败的方法
     */
    void showFailedError();

    void showDenfendResult(Map<String, String> map);

    void shoControlResult(Map<String, String> map);
}
