package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.model.CarControlModel;
import com.cxt.gps.model.impl.CarControlModelImpl;
import com.cxt.gps.view.ICarControlView;

import java.util.HashMap;
import java.util.List;

public class CarControlPresenter {
    private ICarControlView iCarControlView;
    private CarControlModel carControlModel;
    private Handler mHandler = new Handler() {};
    public CarControlPresenter(ICarControlView iCarControlView) {
        this.iCarControlView = iCarControlView;
        this.carControlModel = new CarControlModelImpl();
    }

    public void loadListData(String msgType, String account, int pageNum, int num) {
        iCarControlView.showLoading();
        carControlModel.loadListData(msgType,account,pageNum,num, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarControlView.toActivityShowRisk(list);
                        iCarControlView.hideLoading();
                    }
                });

            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarControlView.showFailedError();
                        iCarControlView.hideLoading();
                    }
                });
            }
        });
    }

    public void cancelControl(String msgType, String account, String carNum) {
        carControlModel.cancelControl(msgType,account,carNum, new OnReturnStringListener() {
            @Override
            public void successed(final String result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarControlView.toActivityShowResult(result);
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
