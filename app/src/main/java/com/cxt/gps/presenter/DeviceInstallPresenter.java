package com.cxt.gps.presenter;

import android.content.Context;
import android.os.Handler;

import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.model.DeviceInstallModel;
import com.cxt.gps.model.impl.DeviceInstallModelImpl;
import com.cxt.gps.view.IDeviceInstallView;

import java.util.LinkedList;


public class DeviceInstallPresenter {
    private DeviceInstallModel deviceInstallModel;
    private IDeviceInstallView iDeviceInstallView;
    private Handler mHandler = new Handler(){};

    public DeviceInstallPresenter(IDeviceInstallView iDeviceInstallView) {
        this.iDeviceInstallView = iDeviceInstallView;
        this.deviceInstallModel = new DeviceInstallModelImpl();
    }

    public void bindData(String msgType,String account,String vin,String carNum) {
        deviceInstallModel.bindData(msgType,account,vin,carNum, new OnReturnStringListener() {
            @Override
            public void successed(final String result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDeviceInstallView.toMainActivity(result);
                    }
                });

            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });

    }

    public void uploadImage(Context context, LinkedList<String> list,String msgType, String account, String vin, int num) {
        deviceInstallModel.uploadImage(context,list,msgType,account,vin,num, new OnReturnStringListener() {
            @Override
            public void successed(final String result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDeviceInstallView.toShowUploadActivity(result);
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }
}
