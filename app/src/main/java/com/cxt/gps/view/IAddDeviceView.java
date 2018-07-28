package com.cxt.gps.view;

import java.util.Map;

public interface IAddDeviceView {
    /**
     * View层登录成功的方法
     * @param map
     */
    void toMainActivity(Map<String, String> map);

    /**
     * View层显示登录失败的方法
     */
    void showFailedError();

    void toShowDeviceStatus(Map<String, String> map);
}
