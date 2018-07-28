package com.cxt.gps.view;

import com.cxt.gps.entity.User;

public interface IUserLoginView {
        /**
         * View层接口获取用户名的方法
         * @return 输入的用户名
         */
        public String getUserName();

        /**
         * View层接口获取密码的方法
         * @return 输入的密码
         */
        public String getPassword();

        /**
         * View层登录成功的方法
         * @param user user对象
         */
        public void toMainActivity(User user);

        /**
         * View层显示登录失败的方法
         */
        public void showFailedError();

        /**
         * View层显示进度条的方法
         */
        public void showLoading();

        /**
         * View层隐藏进度条的方法
         */
        public void hideLoading();
    }
