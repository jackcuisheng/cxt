package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.entity.GpsData;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.model.NavigateModel;
import com.cxt.gps.model.impl.NavigateModelImpl;
import com.cxt.gps.view.INavigateView;

public class NavigatePresenter {
    private NavigateModel navigateModel;
    private INavigateView iNavigateView;
    private Handler handler = new Handler(){};

    public NavigatePresenter(INavigateView iNavigateView) {
        this.iNavigateView = iNavigateView;
        this.navigateModel = new NavigateModelImpl();
    }

    public void loadData(String msgType,String account,String carNum) {
        navigateModel.loadData(msgType, account, carNum, new OnMapLocationListener() {
            @Override
            public void successed(final GpsData data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iNavigateView.toShowMapMaker(data);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
}
