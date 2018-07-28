package com.cxt.gps.view;


import com.cxt.gps.entity.DeviceData;
import com.cxt.gps.entity.GpsData;

import java.util.HashMap;
import java.util.List;

public interface ISearchCarView {
    /**
     * 获取搜索框数据
     * @return
     */
    String getQuery();
    /**
     * 获取账户
     */
    String getAccount();
    /**
     * 回到主线程
     * @param list
     */
    void toMainActivity(List<HashMap<String, String>> list);
    void toShowMapMaker(GpsData data);
    /**
     */
    public void showFailedError();
    /**
     * View层显示进度条的方法
     */
    public void showLoading();

    /**
     * View层隐藏进度条的方法
     */
    public void hideLoading();

    void showCarDetail(List<DeviceData> data);
}
