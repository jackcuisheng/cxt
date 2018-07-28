package com.cxt.gps.view;

import java.util.HashMap;
import java.util.List;

public interface ITrackView {
    /**
     * View层登录成功的方法
     * @param map
     */
    void toMainActivity(List<HashMap<String, String>> map);
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
}
