package com.cxt.gps.listener;

import com.cxt.gps.entity.User;

public interface OnLoginListener {
    /**
     * 登录成功
     * @param user user对象
     */
    void loginSuccess(User user);

    /**
     * 登录失败
     */
    void loginFailed();
}
