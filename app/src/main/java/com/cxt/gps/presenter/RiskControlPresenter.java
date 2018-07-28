package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.RiskControlModel;
import com.cxt.gps.model.impl.RiskControlModelImpl;
import com.cxt.gps.view.IRiskControlView;

import java.util.HashMap;
import java.util.List;


public class RiskControlPresenter {
    private RiskControlModel riskControlModel;
    private IRiskControlView iRiskControlView;
    private Handler mHandler = new Handler() {};

    public RiskControlPresenter(IRiskControlView iRiskControlView) {
        this.iRiskControlView = iRiskControlView;
        this.riskControlModel = new RiskControlModelImpl();
    }

    public void loadListData(String account,String query,int pageNum,String requestType) {
        iRiskControlView.showLoading();
        riskControlModel.loadListData(account,query,pageNum,requestType, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.toShowListActivity(list);
                        iRiskControlView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.showFailedError();
                        iRiskControlView.hideLoading();
                    }
                });
            }
        });
    }

    public void initData(String msgType,String account) {
        iRiskControlView.showLoading();
        riskControlModel.initData(msgType,account, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.toShowDataNumActivity(list);
                        iRiskControlView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.showFailedError();
                        iRiskControlView.hideLoading();
                    }
                });
            }
        });
    }

    public void queryData(String msgType,int requestType,String account,String query) {
        iRiskControlView.showLoading();
        riskControlModel.query(msgType,requestType,account,query, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.toShowListActivity(list);
                        iRiskControlView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRiskControlView.showFailedError();
                        iRiskControlView.hideLoading();
                    }
                });
            }
        });
    }


}
