package com.cxt.gps.presenter;

import android.os.Handler;

import com.cxt.gps.entity.User;
import com.cxt.gps.listener.OnLoginListener;
import com.cxt.gps.model.IUserModel;
import com.cxt.gps.model.impl.UserModelImpl;
import com.cxt.gps.view.IUserLoginView;

public class UserLoginPresenter {
    private IUserModel userModel;
    private IUserLoginView userLoginView;
    private Handler mHandler = new Handler() {
    };


    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userModel = new UserModelImpl();
        this.userLoginView = userLoginView;
    }

    /**
     * 登录操作
     */
    public void login() {
//      设置进度条可见

        userLoginView.showLoading();
        //数据控制器控制model操作登录
        userModel.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(final User user) {
                //需要在UI线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.toMainActivity(user);
                        userLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed() {
                //需要在UI线程执行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.showFailedError();
                        //userLoginView.hideLoading();
                    }
                });
            }
        });
    }

}