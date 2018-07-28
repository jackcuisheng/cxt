package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.FindPasswordModel;
import com.cxt.gps.model.impl.FindPAsswordModelImpl;
import com.cxt.gps.view.IFindPasswordView;

import java.util.Map;

public class FindPasswordPresenter {
    private FindPasswordModel findPasswordModel;
    private IFindPasswordView iFindPasswordView;
    private Handler handler = new Handler(){};

    public FindPasswordPresenter(IFindPasswordView iFindPasswordView) {
        this.iFindPasswordView = iFindPasswordView;
        this.findPasswordModel = new FindPAsswordModelImpl();
    }

    public void findPassword(String msgType,String username,String code,String password) {
        findPasswordModel.findPassword(msgType,username,code,password, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFindPasswordView.toMainActivity(map);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iFindPasswordView.showFailedError();
                    }
                });
            }
        });
    }
}
