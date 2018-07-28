package com.cxt.gps.view;

public interface IDeviceInstallView {
    /**
     * View层登录成功的方法
     * @param result user对象
     */
    void toMainActivity(String result);

    /**
     * View层显示登录失败的方法
     */
    void showFailedError();

    void toShowUploadActivity(String result);
}
