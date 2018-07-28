package com.cxt.gps.view;

import java.util.HashMap;
import java.util.List;

public interface ICarControlView {
    void toActivityShowRisk(List<HashMap<String,String>> list);
    /**
     * View层显示登录失败的方法
     */
    void showFailedError();

    /**
     * View层显示进度条的方法
     */
    void showLoading();

    /**
     * View层隐藏进度条的方法
     */
    void hideLoading();

    void toActivityShowResult(String result);

}
