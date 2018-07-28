package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.HotPointModel;
import com.cxt.gps.model.impl.HotPointModelImpl;
import com.cxt.gps.view.IResidentPointView;

import java.util.HashMap;
import java.util.List;

public class HotPointPresenter {
    private HotPointModel hotPointModel;
    private IResidentPointView iResidentPointView;
    private Handler handler = new Handler(){};

    public HotPointPresenter(IResidentPointView iResidentPointView) {
        this.iResidentPointView = iResidentPointView;
        this.hotPointModel = new HotPointModelImpl();
    }

    public void loadData(String msgType,String account,String carNum,String sTime,String eTime,String timeType,String locationType) {
        hotPointModel.loadData(msgType,account,carNum,sTime,eTime,timeType,locationType, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iResidentPointView.toMainActivity(list);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iResidentPointView.showFailedError();
                    }
                });
            }
        });
    }
}
