package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.CarManageModel;
import com.cxt.gps.model.impl.CarManageModelImpl;
import com.cxt.gps.view.ICarManageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarManagePresenter {
    private CarManageModel carManageModel;
    private ICarManageView iCarManageView;
    private Handler mHandle = new Handler(){};
    public CarManagePresenter(ICarManageView iCarManageView){
        this.carManageModel = new CarManageModelImpl();
        this.iCarManageView = iCarManageView;
    }

    public void getInitData(String query,String account,int pageNum) {
        iCarManageView.showLoading();
        carManageModel.getAllCarData(account,pageNum,query,new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarManageView.toShowCarsActivity(list);
                        iCarManageView.hideLoading();
                    }
                });

            }

            @Override
            public void failed() {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarManageView.showFailedError();
                        iCarManageView.hideLoading();
                    }
                });

            }
        });

    }

    public void getRiskCarData(String MsgType,String account, int pageNum, int num) {
        carManageModel.getRiskCarData(MsgType,account, pageNum, num,
        new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarManageView.toShowRiskCar(list);
                    }
                });

            }

            @Override
            public void failed() {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarManageView.showFailedError();
                    }
                });
            }
        });
    }
}
