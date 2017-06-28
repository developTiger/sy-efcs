package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfo;

import java.util.HashMap;

/**
 * DTS设备的实时信息管理，按设备号管理设备的实时信息
 * @author Leon Regulus on 2017/4/25.
 * @version 1.0
 * @since 1.0
 */
public class DtsStatusRealtimeInfo extends DeviceRealtimeInfo {

    HashMap<String, DtsStatus> deviceStatusMap = new HashMap<>();

    public void setDeviceStatus(String deviceNo, int channelId, String status) {
        getDeviceStatus(deviceNo).updateChannelStatus(channelId, status);
    }

    public void resetDeviceStatus(String deviceNo) {
        getDeviceStatus(deviceNo).resetStatus();
    }

    public DtsStatus getDeviceStatus(String deviceNo) {
        if (deviceStatusMap.containsKey(deviceNo)) {
            return deviceStatusMap.get(deviceNo);
        } else {
            DtsStatus dts = new DtsStatus(deviceNo);
            deviceStatusMap.put(deviceNo, dts);
            return dts;
        }
    }
}
