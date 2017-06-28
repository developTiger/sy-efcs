package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.configration.catlConfig.RealTimeConfig;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * 一个DTS设备的状态类，保存设备名和通道状态描述
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
public class DtsStatus {

    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 通道状态
     */
    private HashMap<Integer, String> channelEvent;

    public DtsStatus(String deviceNo) {
        this.deviceNo = deviceNo;
        this.channelEvent = new HashMap<>();
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public HashMap<Integer, String> getChannelEvent() {
        return channelEvent;
    }

    public void updateChannelStatus(int channelId, String status) {
        channelEvent.put(channelId, status);
    }

    public void resetStatus() {
        Collection<Integer> keys = channelEvent.keySet();
        for (Integer i : keys) {
            channelEvent.put(i, RealTimeConfig.DTS_Normal_Status);
        }
    }
}
