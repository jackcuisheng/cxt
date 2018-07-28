package com.cxt.gps.view;

import java.util.Map;

public interface IFindPasswordView {

    /**
     * @param map map
     */
    public void toMainActivity(Map<String,String> map);

    /**
     * View层显示失败的方法
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
}
