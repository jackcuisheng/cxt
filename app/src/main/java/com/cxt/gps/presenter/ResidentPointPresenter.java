package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.ResidentPointModel;
import com.cxt.gps.model.impl.ResidentPointModelImpl;
import com.cxt.gps.view.IResidentPointView;

import java.util.HashMap;
import java.util.List;


public class ResidentPointPresenter {
    private ResidentPointModel residentPointModel;
    private IResidentPointView iResidentPointView;
    private Handler mHandler = new Handler(){};

    public ResidentPointPresenter(IResidentPointView iResidentPointView) {
        this.iResidentPointView = iResidentPointView;
        this.residentPointModel = new ResidentPointModelImpl();
    }

    public void loadData(String msgType,String account,String carNum,String timeType,String locationType) {
        iResidentPointView.showLoading();
        residentPointModel.loadData(msgType, account, carNum, timeType, locationType, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iResidentPointView.toMainActivity(list);
                        iResidentPointView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iResidentPointView.showFailedError();
                        iResidentPointView.hideLoading();
                    }
                });
            }
        });
    }


}
