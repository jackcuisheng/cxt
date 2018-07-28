package com.cxt.gps.view;

import java.util.Map;

public interface IUpdateFenceView {
    /**
     * View层成功的方法
     * @param map
     */
    void showEditResult(Map<String,String> map);
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
