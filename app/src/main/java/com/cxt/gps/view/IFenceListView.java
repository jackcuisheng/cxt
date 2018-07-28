package com.cxt.gps.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IFenceListView {
    void toShowFenceListActivity(List<HashMap<String, String>> list);
    /**
     * View层显示登录失败的方法
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

    void showEditResult(Map<String, String> map);
}
