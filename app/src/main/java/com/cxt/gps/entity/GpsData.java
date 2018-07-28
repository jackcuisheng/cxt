package com.cxt.gps.entity;

public class GpsData {
    private String vin;
    private Double latitude;
    private Double longitude;
    private String locateMthod;
    private String locateTime;
    private String quantityOfElectricity;
    private String currentLocation;
    private String setDenfendStatus;
    private String controlStatus;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocateMthod() {
        return locateMthod;
    }

    public void setLocateMthod(String locateMthod) {
        this.locateMthod = locateMthod;
    }

    public String getLocateTime() {
        return locateTime;
    }

    public void setLocateTime(String locateTime) {
        this.locateTime = locateTime;
    }

    public String getQuantityOfElectricity() {
        return quantityOfElectricity;
    }

    public void setQuantityOfElectricity(String quantityOfElectricity) {
        this.quantityOfElectricity = quantityOfElectricity;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getSetDenfendStatus() {
        return setDenfendStatus;
    }

    public void setSetDenfendStatus(String setDenfendStatus) {
        this.setDenfendStatus = setDenfendStatus;
    }

    public String getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
