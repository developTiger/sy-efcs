package com.wxzd.efcs.business.application.realtime.dto;

/**
 * 设备心跳状态类
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
public class HeartbeatStatus {
    private String deviceNo;
    private boolean online;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
