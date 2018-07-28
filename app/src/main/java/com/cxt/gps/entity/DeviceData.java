package com.cxt.gps.entity;

public class DeviceData {
   private String deviceID;
   private String currentLocation;
   private double carLng;
   private double carLat;
   private String carSpeed;
   private String carDirection;
   private String carState;
   private String deviceBatteryLevel;
   private String locateTime;
   private String alarmState;
   private String positioningType;


    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getCarLng() {
        return carLng;
    }

    public void setCarLng(double carLng) {
        this.carLng = carLng;
    }

    public double getCarLat() {
        return carLat;
    }

    public void setCarLat(double carLat) {
        this.carLat = carLat;
    }

    public String getCarSpeed() {
        return carSpeed;
    }

    public void setCarSpeed(String carSpeed) {
        this.carSpeed = carSpeed;
    }

    public String getCarDirection() {
        return carDirection;
    }

    public void setCarDirection(String carDirection) {
        this.carDirection = carDirection;
    }

    public String getCarState() {
        return carState;
    }

    public void setCarState(String carState) {
        this.carState = carState;
    }

    public String getDeviceBatteryLevel() {
        return deviceBatteryLevel;
    }

    public void setDeviceBatteryLevel(String deviceBatteryLevel) {
        this.deviceBatteryLevel = deviceBatteryLevel;
    }

    public String getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(String locateTime) {
        this.locateTime = locateTime;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getPositioningType() {
        return positioningType;
    }

    public void setPositioningType(String positioningType) {
        this.positioningType = positioningType;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
