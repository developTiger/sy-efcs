package com.wxzd.efcs.business.application.realtime.dto;

import com.wxzd.configration.catlConfig.RealTimeConfig;
import com.wxzd.efcs.business.application.realtime.DeviceRealtimeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 记录每个设备的最近一次心跳时间，查询时通过判定当前时间和上一次心跳时间的差额确定设备是否在线。
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public class HeartbeatRealtimeInfo extends DeviceRealtimeInfo {

    HashMap<String, Date> deviceHeartbeat;

    public HeartbeatRealtimeInfo(String houseNo) {
        setHouseNo(houseNo);
        deviceHeartbeat = new HashMap<>();
    }

    public void heartbeat(String deviceNo) {
        deviceHeartbeat.put(deviceNo, new Date());
    }

    public boolean isOnline(String deviceNo) {
        if (!deviceHeartbeat.containsKey(deviceNo)) {
            return false;
        }
        Date old = deviceHeartbeat.get(deviceNo);
        long l = new Date().getTime() - old.getTime();
        if (l > RealTimeConfig.Heartbeat_Interval) {
            return false;
        }
        return true;
    }

    public List<HeartbeatStatus> getStatus() {
        List<HeartbeatStatus> list = new ArrayList<>();
        for (String key : deviceHeartbeat.keySet()) {
            HeartbeatStatus heartbeatStatus = new HeartbeatStatus();
            heartbeatStatus.setDeviceNo(key);
            heartbeatStatus.setOnline(isOnline(key));

            list.add(heartbeatStatus);
        }
        return list;
    }
}
