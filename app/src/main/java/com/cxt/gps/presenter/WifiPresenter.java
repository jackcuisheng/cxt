package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.WifiModel;
import com.cxt.gps.model.impl.WifiModelImpl;
import com.cxt.gps.view.IWifiView;

import java.util.HashMap;
import java.util.List;


public class WifiPresenter {
    private WifiModel wifiModel;
    private IWifiView iWifiView;
    private Handler mHandler = new Handler(){};
    public WifiPresenter(IWifiView iWifiView) {
        this.iWifiView = iWifiView;
        this.wifiModel = new WifiModelImpl();
    }

    public void loadInItData(String msgType,String account,String carNum) {
        iWifiView.showLoading();
        wifiModel.loadInItData(msgType, account, carNum, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iWifiView.toMainActivity(list);
                        iWifiView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iWifiView.showFailedError();
                         iWifiView.hideLoading();
                        }
                });
            }
        });
    }
}
