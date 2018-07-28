package com.cxt.gps.view;

import java.util.HashMap;
import java.util.List;

public interface IMessageView {
    /**
     * 显示列表数据
     */
    void toShowListActivity(List<HashMap<String,String>> list);
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


    void toShowQueryListActivity(List<HashMap<String, String>> list);
}
