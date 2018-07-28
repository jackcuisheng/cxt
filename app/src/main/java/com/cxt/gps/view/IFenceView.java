package com.cxt.gps.view;

import com.cxt.gps.entity.GpsData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IFenceView {
    void toShowSaveFenceResult(Map<String,String> map);
    /**
     * View层成功的方法
     * @param data
     */
    void toShowMapMaker(GpsData data);
    /**
     * View层显示登录失败的方法
     */
    void showFailedError();
    /**
     * View层显示进度条的方法
     */
    public void showLoading();

    /**
     * View层隐藏进度条的方法
     */
    public void hideLoading();

    void toshowDeleteResult(Map<String, String> map);

    void showAllFence(List<HashMap<String, String>> list);
}
