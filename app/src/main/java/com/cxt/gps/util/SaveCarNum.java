package com.cxt.gps.util;

public class SaveCarNum {
    private static String CarNum = "";

    public static synchronized String getCarNum() {
        return CarNum;
    }

    public static synchronized void setCarNum(String carNum) {
        CarNum = carNum;
    }
}
