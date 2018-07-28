package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.model.AddDeviceModel;
import com.cxt.gps.model.impl.AddDeviceModelImpl;
import com.cxt.gps.view.IAddDeviceView;

import java.util.Map;


public class AddDevicePresenter {
    private AddDeviceModel addDeviceModel;
    private IAddDeviceView iAddDeviceView;
    private Handler mHandler = new Handler(){};

    public AddDevicePresenter(IAddDeviceView iAddDeviceView) {
        this.iAddDeviceView = iAddDeviceView;
        this.addDeviceModel = new AddDeviceModelImpl();
    }

    public void checkDeviceState(String MsgType,String account,String deviceID) {
        addDeviceModel.checkDeviceState(MsgType,account,deviceID, new OnReturnMapListener() {


            @Override
            public void successed(final Map<String, String> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iAddDeviceView.toShowDeviceStatus(map);
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

    public void bindDevice(String msgType,String account,String vin,String carNum,String deviceID) {
        addDeviceModel.bindDevice(msgType,account,vin,carNum,deviceID, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String,String> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iAddDeviceView.toMainActivity(map);
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
