package com.cxt.gps.model.impl;

import com.cxt.gps.entity.User;
import com.cxt.gps.listener.OnLoginListener;
import com.cxt.gps.model.IUserModel;
import com.cxt.gps.util.Constants;
import com.cxt.gps.util.GetPostUtil;
import com.cxt.gps.util.ParseJsonUtil;
import com.cxt.gps.util.UnicodeUtils;

import java.util.Map;

import timber.log.Timber;

public class UserModelImpl implements IUserModel{
    private String response;
    private Map<String,String> map = null;
    private String account;
    private String result;
    @Override
    public void login(final String username, final String password, final OnLoginListener onLoginListener) {
        new Thread(){
            public void run(){
                try{
                    response = GetPostUtil.sendPost(Constants.getLoginRequest("REQUEST_LOGIN",username,password));
                    response = response.trim();
                    response = UnicodeUtils.unicode2String(response);
                    map = ParseJsonUtil.getLoginResult(response);
                    account = map.get("account").toString().trim();
                    result = map.get("result").toString().trim();
                    if (result.equals("1")){
                        User user = new User(username,password,account);
                        onLoginListener.loginSuccess(user);
                    }else{
                        onLoginListener.loginFailed();
                    }
                }catch (Exception e){
                    Timber.d(e.getMessage());
                }
            }
        }.start();
    }
}
