package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnStringListener;
import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.CarDefendModel;
import com.cxt.gps.model.impl.CarDefendModelImpl;
import com.cxt.gps.view.ICarDefendView;

import java.util.HashMap;
import java.util.List;


public class CarDefendPresenter {
    private CarDefendModel carDefendModel;
    private ICarDefendView iCarDefendView;
    private Handler mHandler = new Handler(){};

    public CarDefendPresenter(ICarDefendView iCarDefendView) {
        this.iCarDefendView = iCarDefendView;
        this.carDefendModel = new CarDefendModelImpl();
    }

    public void loadListData(String MsgType,String account,int pageNum, int num) {
        iCarDefendView.showLoading();
        carDefendModel.loadListData(MsgType, account, pageNum, num, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarDefendView.toActivityShowRisk(list);
                        iCarDefendView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarDefendView.showFailedError();
                        iCarDefendView.hideLoading();
                    }
                });
            }
        });
    }


    public void cancelDefend(String MsgType, String account, String carNum) {
        carDefendModel.cancelDefend(MsgType,account,carNum, new OnReturnStringListener() {
            @Override
            public void successed(final String result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarDefendView.toActivityShowResult(result);
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


    public void setControl(final String msgType, String account, String carNum) {
        carDefendModel.setControl(msgType,account,carNum, new OnReturnStringListener() {
            @Override
            public void successed(final String result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iCarDefendView.toActivityShowControlResult(result);
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
