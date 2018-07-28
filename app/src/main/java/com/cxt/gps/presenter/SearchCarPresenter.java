package com.cxt.gps.presenter;

import com.cxt.gps.entity.DeviceData;
import com.cxt.gps.entity.GpsData;
import com.cxt.gps.listener.OnDeviceDataListener;
import com.cxt.gps.listener.OnMapLocationListener;
import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.SearchCarModel;
import com.cxt.gps.model.impl.SearchCarModelImpl;
import com.cxt.gps.view.ISearchCarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchCarPresenter {
    private SearchCarModel searchCarModel;
    private ISearchCarView iSearchCarView;
    private android.os.Handler mHandler = new android.os.Handler() {
    };

    public SearchCarPresenter(ISearchCarView iSearchCarView) {
        this.searchCarModel = new SearchCarModelImpl();
        this.iSearchCarView = iSearchCarView;
    }
    public void queryData(){
        iSearchCarView.showLoading();
        searchCarModel.getAllCarList(iSearchCarView.getQuery(), iSearchCarView.getAccount(), new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.toMainActivity(list);
                        iSearchCarView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.showFailedError();
                        iSearchCarView.hideLoading();
                    }
                });
            }
        });
    }
    public void getLastLocation(String carNum,String account){
        iSearchCarView.showLoading();
        searchCarModel.getLastLocationData(carNum,account, new OnMapLocationListener() {
            @Override
            public void successed(final GpsData data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.toShowMapMaker(data);
                        iSearchCarView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.showFailedError();
                        iSearchCarView.hideLoading();
                    }
                });
            }
        });

    }

    public void getCarDetail(String msgType,String account,String carNum) {
        searchCarModel.getCarDetail(msgType,account,carNum, new OnDeviceDataListener() {
            @Override
            public void successed(final List<DeviceData> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.showCarDetail(list);
                    }
                });
            }

            @Override
            public void failed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iSearchCarView.showFailedError();
                    }
                });
            }
        });
    }
}
