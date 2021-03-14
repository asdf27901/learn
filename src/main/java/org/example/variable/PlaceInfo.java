package org.example.variable;

import com.google.gson.annotations.SerializedName;

public class PlaceInfo {

    @SerializedName("placeNo")
    private Integer placeNo;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("antenna")
    private String antenna;
    @SerializedName("time")
    private Integer time;

    public Integer getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(Integer placeNo) {
        this.placeNo = placeNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAntenna() {
        return antenna;
    }

    public void setAntenna(String antenna) {
        this.antenna = antenna;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "placeNo=" + placeNo +
                ", startTime='" + startTime + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", antenna='" + antenna + '\'' +
                ", time=" + time +
                '}';
    }
}
