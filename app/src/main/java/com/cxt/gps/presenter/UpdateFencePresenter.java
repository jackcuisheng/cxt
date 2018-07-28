package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.UpdateFenceModel;
import com.cxt.gps.model.impl.UpdateFenceModelImpl;
import com.cxt.gps.view.IUpdateFenceView;

import java.util.Map;

public class UpdateFencePresenter {
    private IUpdateFenceView iUpdateFenceView;
    private UpdateFenceModel updateFenceModel;
    private Handler handler = new Handler(){};

    public UpdateFencePresenter(IUpdateFenceView iUpdateFenceView) {
        this.iUpdateFenceView = iUpdateFenceView;
        this.updateFenceModel = new UpdateFenceModelImpl();
    }
    public void editFence(String msgType,String account,String fenceId,String radius,String fenceName,int condition) {
        iUpdateFenceView.showLoading();
        updateFenceModel.editFence(msgType,account,fenceId,radius,fenceName,condition, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUpdateFenceView.showEditResult(map);
                        iUpdateFenceView.hideLoading();
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUpdateFenceView.showFailedError();
                    }
                });
            }
        });
    }
}
