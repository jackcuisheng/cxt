package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.listener.OnReturnMapListener;
import com.cxt.gps.model.UpdatePasswordModel;
import com.cxt.gps.model.impl.UpdatePasswordModelImpl;
import com.cxt.gps.view.IUpdatePasswordView;

import java.util.Map;

public class UpdatePasswordPresenter {
    private UpdatePasswordModel updatePasswordModel;
    private IUpdatePasswordView iUpdatePasswordView;
    private Handler handler = new Handler(){};

    public UpdatePasswordPresenter(IUpdatePasswordView iUpdatePasswordView) {
        this.iUpdatePasswordView = iUpdatePasswordView;
        this.updatePasswordModel = new UpdatePasswordModelImpl();
    }

    public void updatePassword(String msgType,String account,String oldPassword,String newPassword) {
        updatePasswordModel.updatePassword(msgType,account,oldPassword,newPassword, new OnReturnMapListener() {
            @Override
            public void successed(final Map<String, String> map) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUpdatePasswordView.toMainActivity(map);
                    }
                });
            }

            @Override
            public void failed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUpdatePasswordView.showFailedError();
                    }
                });
            }
        });
    }
}
