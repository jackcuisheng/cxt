package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.entity.GpsData;
import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FenceModel;
import com.cxt.gps.model.impl.FenceModelImpl;
import com.cxt.gps.view.IFenceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FencePresenter {
    private FenceModel fenceModel;
    private IFenceView iFenceView;
    private Handler handler = new Handler(){};

    public FencePresenter(IFenceView iFenceView) {
        this.iFenceView = iFenceView;
        this.fenceModel = new FenceModelImpl();
    }

    public void loadDeviceData(String msgType,String account,String carNum) {
        iFenceView.showLoading();
        fenceModel.loadDeviceData(msgType,account,carNum, new OnMapLocationListener() {
            @Override
            public void successed(final GpsData data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.toShowMapMaker(data);
                        iFenceView.hideLoading();
                    }
                });

            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.showFailedError();
                        iFenceView.hideLoading();
                    }
                });
            }
        });
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

    public void deleteFence(String msgType,String account,String fenceID) {
        fenceModel.deleteFence(msgType,account,fenceID, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.toshowDeleteResult(map);
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

    public void loadFenceList(String msgType, String account, String carNum, String searchValue, String pageNum) {
        fenceModel.loadFenceList(msgType,account,carNum,searchValue,pageNum, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceView.showAllFence(list);
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
