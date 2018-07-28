package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnListCarListener;
import com.cxt.gps.model.TrackModel;
import com.cxt.gps.model.impl.TrackModelImpl;
import com.cxt.gps.view.ITrackView;

import java.util.HashMap;
import java.util.List;

public class TrackPresenter {
    private TrackModel trackModel;
    private ITrackView iTrackView;
    private Handler handler = new Handler(){};

    public TrackPresenter(ITrackView iTrackView) {
        this.iTrackView = iTrackView;
        this.trackModel = new TrackModelImpl();
    }

    public void loadData(String msgType,String account,String carNum,String sTime,String eTime,String deviceID,String locationType) {
        iTrackView.showLoading();
        trackModel.loadData(msgType,account,carNum,sTime,eTime,deviceID,locationType, new OnListCarListener() {
            @Override
            public void successed(final List<HashMap<String, String>> list) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iTrackView.toMainActivity(list);
                        iTrackView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iTrackView.showFailedError();
                        iTrackView.hideLoading();
                    }
                });
            }
        });
    }
}
