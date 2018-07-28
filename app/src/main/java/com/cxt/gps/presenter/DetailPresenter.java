package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.DetailModel;
import com.cxt.gps.model.impl.DetailModelImpl;
import com.cxt.gps.view.IDetailView;

import java.util.Map;

public class DetailPresenter {
    private DetailModel detailModel;
    private IDetailView iDetailView;
    private Handler mHandler = new Handler(){};

    public DetailPresenter(IDetailView iDetailView) {
        this.iDetailView = iDetailView;
        this.detailModel = new DetailModelImpl();
    }

    public void setLoadData(String msgType,String account,String carNum) {
        detailModel.setLoadData(msgType,account,carNum, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.toMainActivity(map);
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

    public void setDefend(String msgtype,String account,String carNum,String reason) {
        detailModel.setDefend(msgtype,account,carNum,reason, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.showDenfendResult(map);
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

    public void setControl(String msgType,String account,String carNum) {
        detailModel.setControl(msgType,account,carNum, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.shoControlResult(map);
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
