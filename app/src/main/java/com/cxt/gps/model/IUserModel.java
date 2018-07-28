package com.cxt.gps.model;

import com.cxt.gps.listener.OnLoginListener;

public interface IUserModel {
    /**
     * 登录数据模块的操作
     * @param username 用户名
     * @param password 密码
     * @param onLoginListener 登录监听
     */
    void login(String username,String password,OnLoginListener onLoginListener);
}
