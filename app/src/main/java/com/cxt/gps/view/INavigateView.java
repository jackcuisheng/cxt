package com.cxt.gps.view;

import com.amap.api.maps.model.LatLng;
import com.cxt.gps.entity.GpsData;

public interface INavigateView {
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
}
