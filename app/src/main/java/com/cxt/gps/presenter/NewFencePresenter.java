package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FenceModel;
import com.cxt.gps.model.impl.FenceModelImpl;
import com.cxt.gps.view.IFenceView;

import java.util.Map;

public class NewFencePresenter {
    private FenceModel fenceModel;
    private IFenceView iFenceView;
    private Handler handler = new Handler(){};

    public NewFencePresenter(IFenceView iFenceView) {
        this.iFenceView = iFenceView;
        this.fenceModel = new FenceModelImpl();
    }
    public void saveFence(String msgType,String account,String carNum,String fenceName,String address,double radius,String fenceType,double latitude,double longitude,int condition) {
        fenceModel.saveFence(msgType,account,carNum,fenceName,address,radius,fenceType,latitude,longitude,condition, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.toShowSaveFenceResult(map);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.showFailedError();
                    }
                });
            }
        });
    }
}
