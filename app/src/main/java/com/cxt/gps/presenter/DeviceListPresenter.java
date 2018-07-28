package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.DeviceListModel;
import com.cxt.gps.model.impl.DeviceListModelImpl;
import com.cxt.gps.view.IDeviceListView;

import java.util.HashMap;
import java.util.List;

public class DeviceListPresenter {
    private DeviceListModel deviceListModel;
    private IDeviceListView iDeviceListView;
    private Handler handler = new Handler();

    public DeviceListPresenter(IDeviceListView iDeviceListView) {
        this.iDeviceListView = iDeviceListView;
        this.deviceListModel = new DeviceListModelImpl();
    }

    public void loadInItData(String msgType,String account,String carNum) {
        deviceListModel.loadInItData(msgType,account,carNum, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDeviceListView.toMainActivity(list);
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
