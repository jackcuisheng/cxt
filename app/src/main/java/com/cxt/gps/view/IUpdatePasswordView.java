package com.cxt.gps.view;

import java.util.Map;

public interface IUpdatePasswordView {


    public void toMainActivity(Map<String,String> map);


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
