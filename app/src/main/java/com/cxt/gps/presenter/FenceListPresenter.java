package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FenceListModel;
import com.cxt.gps.model.impl.FenceListModelImpl;
import com.cxt.gps.view.IFenceListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FenceListPresenter {
    private FenceListModel fenceListModel;
    private IFenceListView iFenceListView;
    private Handler handler = new Handler(){};
    public FenceListPresenter(IFenceListView iFenceListView) {
        this.iFenceListView = iFenceListView;
        this.fenceListModel = new FenceListModelImpl();
    }

    public void loadFenceList(String msgType,String account,String carNum,String searchValue,String pageNum) {
        iFenceListView.showLoading();
        fenceListModel.loadFenceList(msgType,account,carNum,searchValue,pageNum, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceListView.toShowFenceListActivity(list);
                        iFenceListView.hideLoading();
                    }
                });

            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceListView.showFailedError();
                        iFenceListView.hideLoading();
                    }
                });
            }
        });
    }



    public void removeFence(String msgType,String account,String fenceID) {
        fenceListModel.removeFence(msgType,account,fenceID, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFenceListView.showEditResult(map);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
}
